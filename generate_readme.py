"""
generate_readme.py
Genera automáticamente tres secciones del README:
  1. Árbol de estructura del proyecto
  2. Diagrama Mermaid: Feature → Scenario → Step → Page method → BasePage method
  3. Tabla de métodos de BasePage con Javadoc y columna "# Veces Utilizado"

Uso: python3 generate_readme.py
"""

import re
from pathlib import Path
from collections import defaultdict

# ── Configuración ─────────────────────────────────────────────────────────────
SRC_ROOTS  = [Path("src/main/java"), Path("src/test/java"), Path("src/test/resources")]
MAIN_ROOT  = Path("src/main/java")   # BasePage, pages, driverManager, model...
TEST_ROOT  = Path("src/test/java")   # steps, hooks, runner
README_FILE = Path("README.md")

TREE_START    = "<!-- TREE:START -->"
TREE_END      = "<!-- TREE:END -->"
MERMAID_START = "<!-- MERMAID:START -->"
MERMAID_END   = "<!-- MERMAID:END -->"
METHODS_START = "<!-- METHODS:START -->"
METHODS_END   = "<!-- METHODS:END -->"

# Colores por capa (POM)
COLOR_BASEPAGE = "#0F172A"
COLOR_PAGE     = "#1D4ED8"   # azul
COLOR_STEP     = "#15803D"   # verde
COLOR_FEATURE  = "#7E22CE"   # morado


# ── Árbol de directorios ───────────────────────────────────────────────────────

def build_tree(root: Path, prefix: str = "") -> list[str]:
    lines = []
    entries = sorted(root.iterdir(), key=lambda p: (p.is_file(), p.name.lower()))
    for i, entry in enumerate(entries):
        is_last  = i == len(entries) - 1
        connector = "└── " if is_last else "├── "
        lines.append(f"{prefix}{connector}{entry.name}")
        if entry.is_dir():
            lines.extend(build_tree(entry, prefix + ("    " if is_last else "│   ")))
    return lines

def build_tree_section() -> str:
    blocks = []
    for root in SRC_ROOTS:
        if not root.exists():
            continue
        blocks.append("\n".join([str(root) + "/"] + build_tree(root)))
    if not blocks:
        return "_No se encontraron directorios en src/_\n"
    return "```\n" + "\n\n".join(blocks) + "\n```\n"


# ── Parsing de Javadoc ─────────────────────────────────────────────────────────

def parse_javadoc_blocks(source: str, filename: str) -> list[dict]:
    pattern = re.compile(
        r'/\*\*(.*?)\*/'
        r'\s*(private|public|protected)?'
        r'\s+(\w[\w<>\[\]]*)'
        r'\s+(\w+)'
        r'\s*\(([^)]*)\)',
        re.DOTALL
    )
    methods = []
    for m in pattern.finditer(source):
        javadoc_raw, visibility, return_type, method_name, raw_params = m.groups()
        if return_type in ("class", "interface", "enum"):
            continue
        javadoc_lines = [re.sub(r'^\s*\*\s?', '', l).strip()
                         for l in javadoc_raw.strip().splitlines()]
        desc, param_docs, returns_doc = [], {}, ""
        for line in javadoc_lines:
            if line.startswith("@param"):
                parts = line[7:].strip().split(None, 1)
                param_docs[parts[0]] = parts[1] if len(parts) == 2 else ""
            elif line.startswith("@return"):
                returns_doc = line[7:].strip()
            elif not line.startswith("@") and line:
                desc.append(line)
        params = []
        for p in raw_params.split(","):
            p = p.strip()
            if p:
                parts = p.rsplit(None, 1)
                params.append({"type": parts[0], "name": parts[1]} if len(parts) == 2
                              else {"type": p, "name": "?"})
        methods.append({
            "file": filename, "visibility": visibility or "package-private",
            "return_type": return_type, "name": method_name,
            "params": params, "param_docs": param_docs,
            "description": " ".join(desc), "returns_doc": returns_doc,
        })
    return methods

def collect_basepage_methods() -> list[dict]:
    methods = []
    for f in sorted(MAIN_ROOT.rglob("BasePage.java")):
        methods.extend(parse_javadoc_blocks(f.read_text(encoding="utf-8"), f.name))
    return methods


# ── Extracción de cuerpos de métodos ──────────────────────────────────────────

def extract_method_bodies(source: str) -> dict[str, str]:
    """
    Extrae { nombre_metodo: cuerpo } para todos los métodos del archivo.
    Maneja llaves anidadas correctamente.
    """
    result = {}
    header_re = re.compile(
        r'(?:public|private|protected)\s+\w[\w<>\[\]]*\s+(\w+)\s*\([^)]*\)\s*\{'
    )
    for match in header_re.finditer(source):
        method_name = match.group(1)
        start = match.end()
        depth = 1
        i = start
        while i < len(source) and depth > 0:
            if source[i] == '{':
                depth += 1
            elif source[i] == '}':
                depth -= 1
            i += 1
        result[method_name] = source[start:i - 1]
    return result


# ── Calls dentro de un cuerpo ─────────────────────────────────────────────────

SKIP_KEYWORDS = {"if", "for", "while", "switch", "return", "super",
                 "this", "new", "catch", "try", "else"}

def extract_calls(body: str) -> list[str]:
    """Extrae nombres de métodos llamados en un cuerpo (sin prefijo de instancia)."""
    calls = re.findall(r'(?<![.\w])([a-z]\w*)\s*\(', body)
    return [c for c in calls if c not in SKIP_KEYWORDS]


# ── Datos de Pages ─────────────────────────────────────────────────────────────

def collect_pages_data(basepage_method_names: set) -> dict[str, dict[str, list[str]]]:
    """
    { NombreClase: { metodo_page: [metodos_basepage_llamados] } }
    Solo registra calls que existan en BasePage.
    Busca recursivamente en MAIN_ROOT/pages para cubrir subdirectorios
    (alertFrame, bookStoreApplication, elements, forms, interactions, widgets…)
    """
    pages = {}
    pages_dir = MAIN_ROOT / "pages"
    if not pages_dir.exists():
        return pages
    for java_file in sorted(pages_dir.rglob("*.java")):
        if java_file.name == "BasePage.java":
            continue
        source = java_file.read_text(encoding="utf-8")
        cm = re.search(r'public\s+class\s+(\w+)', source)
        if not cm:
            continue
        class_name = cm.group(1)
        bodies = extract_method_bodies(source)
        class_map = {}
        for method, body in bodies.items():
            bp_calls = [c for c in extract_calls(body) if c in basepage_method_names]
            if bp_calls:
                class_map[method] = list(dict.fromkeys(bp_calls))
        pages[class_name] = class_map
    return pages


# ── Conteo de usos ────────────────────────────────────────────────────────────

def count_basepage_usage(basepage_methods: list[dict], pages_data: dict) -> dict[str, int]:
    """
    - Métodos públicos de BasePage: contar cuántas veces los llaman pages/*.java
    - Métodos privados de BasePage: contar cuántas veces los llaman otros métodos de BasePage
    """
    usage = defaultdict(int)
    bp_method_names = {m["name"] for m in basepage_methods}

    # Usos desde pages
    for page_class, methods in pages_data.items():
        for page_method, calls in methods.items():
            for call in calls:
                if call in bp_method_names:
                    usage[call] += 1

    # Usos internos en BasePage (privados llamados por públicos/privados)
    for f in sorted(MAIN_ROOT.rglob("BasePage.java")):
        source = f.read_text(encoding="utf-8")
        bodies = extract_method_bodies(source)
        for method_name, body in bodies.items():
            for call in extract_calls(body):
                if call in bp_method_names and call != method_name:
                    usage[call] += 1

    return dict(usage)


# ── Datos de Steps ────────────────────────────────────────────────────────────

def parse_steps(source: str) -> list[dict]:
    """Extrae steps con sus llamadas a métodos de page."""
    pattern = re.compile(
        r'@(?:Given|When|Then|And|But)\s*\(\s*"([^"]+)"\s*\)\s*'
        r'public\s+void\s+(\w+)\s*\(\s*\)\s*\{',
        re.DOTALL
    )
    bodies = extract_method_bodies(source)
    steps = []
    for m in pattern.finditer(source):
        step_text, method_name = m.group(1), m.group(2)
        body = bodies.get(method_name, "")
        instance_calls = re.findall(r'\w+\.(\w+)\s*\(', body)
        direct_calls = extract_calls(body)
        all_calls = list(dict.fromkeys(instance_calls + direct_calls))
        steps.append({
            "step_text":  step_text,
            "method_name": method_name,
            "page_calls": all_calls,
        })
    return steps

def collect_steps_data() -> dict[str, list[dict]]:
    steps_data = {}
    steps_dir = TEST_ROOT / "steps"
    if not steps_dir.exists():
        return steps_data
    for java_file in sorted(steps_dir.glob("*.java")):
        source = java_file.read_text(encoding="utf-8")
        cm = re.search(r'public\s+class\s+(\w+)', source)
        if cm:
            steps_data[cm.group(1)] = parse_steps(source)
    return steps_data


# ── Datos de Features ─────────────────────────────────────────────────────────

def parse_feature(source: str) -> dict:
    fm = re.search(r'Feature:\s*(.+)', source)
    feature_name = fm.group(1).strip() if fm else "Feature"
    scenarios = []
    for block in re.split(r'\n\s*Scenario(?:\s+Outline)?:', source)[1:]:
        lines = block.strip().splitlines()
        sc_name = lines[0].strip() if lines else "Scenario"
        step_lines = [re.sub(r'^\s*(Given|When|Then|And|But)\s+', '', l).strip()
                      for l in lines[1:]
                      if re.match(r'^\s*(Given|When|Then|And|But)\s', l)]
        scenarios.append({"name": sc_name, "steps": step_lines})
    return {"feature_name": feature_name, "scenarios": scenarios}

def collect_features_data() -> list[dict]:
    seen, unique = set(), []
    feature_files = list(Path("src/test/resources").rglob("*.feature"))
    # fallback: también buscar relativo a TEST_ROOT por si cambia la estructura
    feature_files += [f for f in TEST_ROOT.parent.rglob("*.feature")
                      if f not in feature_files]
    for fp in feature_files:
        source = fp.read_text(encoding="utf-8")
        parsed = parse_feature(source)
        if parsed["feature_name"] not in seen:
            seen.add(parsed["feature_name"])
            unique.append(parsed)
    return unique


# ── Normalización de steps para matching ──────────────────────────────────────

# Cucumber expressions → regex genérico
_CUCUMBER_EXPR = re.compile(r'\{(?:word|string|int|float|bigdecimal|biginteger|byte|short|long|double)\}')
# Gherkin Scenario Outline params: <param> o '<param>'
_OUTLINE_PARAM = re.compile(r"'?<[^>]+>'?")

def normalize_step(text: str) -> str:
    """
    Convierte tanto anotaciones Cucumber como textos de Scenario Outline
    a un patrón comparable común: reemplaza {word}/{string}/etc. y <param>
    por el placeholder '__ARG__', luego lower + strip.
    """
    t = _CUCUMBER_EXPR.sub("__ARG__", text)
    t = _OUTLINE_PARAM.sub("__ARG__", t)
    return t.lower().strip()


# ── Diagrama Mermaid ──────────────────────────────────────────────────────────

def make_id(*parts) -> str:
    raw = "_".join(str(p) for p in parts)
    return re.sub(r'\W+', '_', raw)[:80]

def safe_label(text: str) -> str:
    return text.replace('"', "\'")

def build_mermaid_section(features, steps_data, pages_data) -> str:
    if not features:
        return "_No se encontraron archivos .feature_\n"

    # Lookup normalizado: patrón comparable → step completo
    step_lookup: dict[str, dict] = {}
    for cls, steps in steps_data.items():
        for step in steps:
            step_lookup[normalize_step(step["step_text"])] = step

    page_to_bp: dict[str, list[str]] = {}
    for cls, methods in pages_data.items():
        for page_method, bp_calls in methods.items():
            page_to_bp.setdefault(page_method, [])
            page_to_bp[page_method] = list(dict.fromkeys(
                page_to_bp[page_method] + bp_calls))

    method_to_page: dict[str, str] = {}
    for cls, methods in pages_data.items():
        for page_method in methods:
            method_to_page[page_method] = cls

    lines  = ["```mermaid", "flowchart TD"]
    styles = []
    defined: set[str] = set()
    edges:   set[str] = set()

    def node(nid: str, label: str, color: str):
        if nid not in defined:
            defined.add(nid)
            lines.append(f'    {nid}["{safe_label(label)}"]')
            styles.append(f"style {nid} fill:{color},color:#fff,stroke:#fff")

    def edge(a: str, b: str):
        e = f"{a} --> {b}"
        if e not in edges:
            edges.add(e)
            lines.append(f"    {e}")

    step_annotation: dict[str, str] = {}
    steps_dir = TEST_ROOT / "steps"
    if steps_dir.exists():
        for java_file in steps_dir.glob("*.java"):
            src = java_file.read_text(encoding="utf-8")
            ann_re = re.compile(
                r'@(Given|When|Then|And|But)\s*\(\s*"([^"]+)"\s*\)',
                re.DOTALL
            )
            for m in ann_re.finditer(src):
                step_annotation[normalize_step(m.group(2))] = m.group(1)

    for feat in features:
        f_id = make_id("feat", feat["feature_name"])
        node(f_id, f"Feature: {feat['feature_name']}", COLOR_FEATURE)

        for sc in feat["scenarios"]:
            sc_id = make_id("sc", feat["feature_name"], sc["name"])
            node(sc_id, f"Feature | Scenario: {sc['name']}", COLOR_FEATURE)
            edge(f_id, sc_id)

            for step_text in sc["steps"]:
                key = normalize_step(step_text)
                step_data  = step_lookup.get(key)
                annotation = step_annotation.get(key, "Step")
                method_name = step_data["method_name"] + "()" if step_data else step_text

                st_id = make_id("st", sc["name"], step_text)
                node(st_id, f"Step | @{annotation}: {method_name}", COLOR_STEP)
                edge(sc_id, st_id)

                if not step_data:
                    continue

                for pc in step_data["page_calls"]:
                    page_name = method_to_page.get(pc, "Page")
                    pc_id = make_id("pm", pc)
                    node(pc_id, f"Page | {page_name}: {pc}()", COLOR_PAGE)
                    edge(st_id, pc_id)

                    for bp in page_to_bp.get(pc, []):
                        bp_id = make_id("bp", bp)
                        node(bp_id, f"Page | BasePage: {bp}()", COLOR_BASEPAGE)
                        edge(pc_id, bp_id)

    lines.extend(styles)
    lines.append("```")
    return "\n".join(lines) + "\n"


# ── Tabla de métodos ──────────────────────────────────────────────────────────

def build_params_cell(params, param_docs) -> str:
    if not params:
        return "<sub>—</sub>"
    parts = []
    for p in params:
        doc = param_docs.get(p["name"], "")
        entry = f"`{p['type']} {p['name']}`" + (f": {doc}" if doc else "")
        parts.append(entry)
    return "<sub>" + "<br>".join(parts) + "</sub>"

def build_methods_table(methods: list[dict], usage: dict[str, int]) -> str:
    if not methods:
        return "_No hay métodos documentados todavía._\n"
    header = (
        "| Clase | Visibilidad | Método | Descripción | Parámetros | Retorna | # Usos |\n"
        "|-------|-------------|--------|-------------|------------|---------|--------|\n"
    )
    rows = []
    for m in methods:
        usos = usage.get(m["name"], 0)
        ret  = m["returns_doc"] if m["returns_doc"] else ("—" if m["return_type"] == "void" else f"`{m['return_type']}`")
        uso_cell = f"**{usos}**" if usos > 0 else "0"
        rows.append(
            f"| <sub>`{m['file']}`</sub> | <sub>`{m['visibility']}`</sub> | <sub>`{m['name']}()`</sub> "
            f"| <sub>{m['description'] or '—'}</sub> "
            f"| {build_params_cell(m['params'], m['param_docs'])} "
            f"| <sub>{ret}</sub> "
            f"| <sub>{uso_cell}</sub> |"
        )
    return header + "\n".join(rows) + "\n"


# ── Actualización del README ───────────────────────────────────────────────────

def replace_section(content, start_marker, end_marker, new_body) -> str:
    new_section = f"{start_marker}\n{new_body}\n{end_marker}"
    if start_marker in content and end_marker in content:
        return re.sub(
            rf"{re.escape(start_marker)}.*?{re.escape(end_marker)}",
            new_section, content, flags=re.DOTALL)
    return content.rstrip() + "\n\n" + new_section + "\n"

def update_readme(tree_md, mermaid_md, table_md, method_count):
    original = README_FILE.read_text(encoding="utf-8") if README_FILE.exists() else ""
    updated  = replace_section(original, TREE_START,    TREE_END,    "## 📁 Estructura del proyecto\n\n"        + tree_md)
    updated  = replace_section(updated,  MERMAID_START, MERMAID_END, "## 🔗 Relación Scenario → Métodos\n\n"   + mermaid_md)
    updated  = replace_section(updated,  METHODS_START, METHODS_END, f"## 📋 Métodos disponibles ({method_count})\n\n" + table_md)
    README_FILE.write_text(updated, encoding="utf-8")
    print(f"✅ README actualizado — {method_count} método(s) de BasePage documentado(s).")


# ── Main ──────────────────────────────────────────────────────────────────────

def main():
    tree_md    = build_tree_section()
    bp_methods = collect_basepage_methods()
    bp_names   = {m["name"] for m in bp_methods}
    pages_data = collect_pages_data(bp_names)
    steps_data = collect_steps_data()
    features   = collect_features_data()
    usage      = count_basepage_usage(bp_methods, pages_data)
    mermaid_md = build_mermaid_section(features, steps_data, pages_data)
    table_md   = build_methods_table(bp_methods, usage)
    update_readme(tree_md, mermaid_md, table_md, len(bp_methods))

if __name__ == "__main__":
    main()