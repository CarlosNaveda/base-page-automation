"""
generate_readme.py
Genera automáticamente tres secciones del README:
  1. Árbol de estructura del proyecto (src/test/java y src/test/resources)
  2. Diagrama Mermaid: relación Scenario → Step → Método de Page → BasePage
  3. Tabla de métodos de BasePage con Javadoc y columna "# Veces Utilizado"

Uso: python3 generate_readme.py
"""

import re
from pathlib import Path
from collections import defaultdict

# ── Configuración ────────────────────────────────────────────────────────────
SRC_ROOTS    = [Path("src/test/java"), Path("src/test/resources")]
JAVA_ROOT    = Path("src/test/java")
README_FILE  = Path("README.md")

TREE_START    = "<!-- TREE:START -->"
TREE_END      = "<!-- TREE:END -->"
MERMAID_START = "<!-- MERMAID:START -->"
MERMAID_END   = "<!-- MERMAID:END -->"
METHODS_START = "<!-- METHODS:START -->"
METHODS_END   = "<!-- METHODS:END -->"


# ── Árbol de directorios ──────────────────────────────────────────────────────

def build_tree(root: Path, prefix: str = "") -> list[str]:
    lines = []
    entries = sorted(root.iterdir(), key=lambda p: (p.is_file(), p.name.lower()))
    for i, entry in enumerate(entries):
        is_last = i == len(entries) - 1
        connector = "└── " if is_last else "├── "
        lines.append(f"{prefix}{connector}{entry.name}")
        if entry.is_dir():
            extension = "    " if is_last else "│   "
            lines.extend(build_tree(entry, prefix + extension))
    return lines


def build_tree_section() -> str:
    blocks = []
    for root in SRC_ROOTS:
        if not root.exists():
            continue
        lines = [str(root) + "/"] + build_tree(root)
        blocks.append("\n".join(lines))
    if not blocks:
        return "_No se encontraron directorios en src/test/_\n"
    return f"```\n{chr(10).join(blocks)}\n```\n"


# ── Parsing de Javadoc (BasePage) ─────────────────────────────────────────────

def parse_javadoc_blocks(source: str, filename: str) -> list[dict]:
    pattern = re.compile(
        r'/\*\*(.*?)\*/'
        r'\s*'
        r'(private|public|protected)?'
        r'\s+'
        r'(\w[\w<>\[\]]*)'
        r'\s+'
        r'(\w+)'
        r'\s*\(([^)]*)\)',
        re.DOTALL
    )
    methods = []
    for match in pattern.finditer(source):
        javadoc_raw, visibility, return_type, method_name, raw_params = match.groups()
        if return_type in ("class", "interface", "enum"):
            continue
        javadoc_lines = [
            re.sub(r'^\s*\*\s?', '', line).strip()
            for line in javadoc_raw.strip().splitlines()
        ]
        description_lines, param_docs, returns_doc = [], {}, ""
        for line in javadoc_lines:
            if line.startswith("@param"):
                parts = line[len("@param"):].strip().split(None, 1)
                if len(parts) == 2:
                    param_docs[parts[0]] = parts[1]
                elif len(parts) == 1:
                    param_docs[parts[0]] = ""
            elif line.startswith("@return"):
                returns_doc = line[len("@return"):].strip()
            elif not line.startswith("@") and line:
                description_lines.append(line)
        params = []
        if raw_params.strip():
            for p in raw_params.split(","):
                p = p.strip()
                if p:
                    parts = p.rsplit(None, 1)
                    if len(parts) == 2:
                        params.append({"type": parts[0], "name": parts[1]})
                    else:
                        params.append({"type": p, "name": "?"})
        methods.append({
            "file": filename, "visibility": visibility or "package-private",
            "return_type": return_type, "name": method_name,
            "params": params, "param_docs": param_docs,
            "description": " ".join(description_lines), "returns_doc": returns_doc,
        })
    return methods


def collect_basepage_methods() -> list[dict]:
    """Solo extrae métodos documentados de BasePage.java."""
    all_methods = []
    for java_file in sorted(JAVA_ROOT.rglob("BasePage.java")):
        source = java_file.read_text(encoding="utf-8")
        all_methods.extend(parse_javadoc_blocks(source, java_file.name))
    return all_methods


# ── Parsing de Pages (qué métodos de BasePage usan) ──────────────────────────

def parse_page_methods(source: str) -> dict[str, list[str]]:
    """
    Retorna dict: { nombre_metodo_page: [metodos_basepage_llamados] }
    Busca métodos públicos y dentro de su cuerpo, llamadas directas a métodos conocidos.
    """
    # Extraer nombre de la clase
    class_match = re.search(r'public\s+class\s+(\w+)\s+extends\s+BasePage', source)
    if not class_match:
        return {}

    # Extraer métodos públicos con su cuerpo
    method_pattern = re.compile(
        r'public\s+\w[\w<>\[\]]*\s+(\w+)\s*\([^)]*\)\s*\{([^}]*)\}',
        re.DOTALL
    )
    result = {}
    for match in method_pattern.finditer(source):
        method_name = match.group(1)
        body = match.group(2)
        # Buscar llamadas a métodos (identificador seguido de paréntesis, sin "new" ni ".")
        calls = re.findall(r'(?<!new\s)(?<!\.)(\b[a-z]\w+)\s*\(', body)
        # Filtrar constructores y keywords comunes
        skip = {"if", "for", "while", "switch", "return", "super", "this"}
        calls = [c for c in calls if c not in skip]
        if calls:
            result[method_name] = list(dict.fromkeys(calls))  # deduplicar manteniendo orden
    return result


def collect_pages_data() -> dict[str, dict[str, list[str]]]:
    """Retorna { NombreClase: { metodo_page: [metodos_basepage] } }"""
    pages = {}
    pages_dir = JAVA_ROOT / "pages"
    if not pages_dir.exists():
        return pages
    for java_file in sorted(pages_dir.glob("*.java")):
        if java_file.name == "BasePage.java":
            continue
        source = java_file.read_text(encoding="utf-8")
        class_match = re.search(r'public\s+class\s+(\w+)', source)
        if class_match:
            class_name = class_match.group(1)
            pages[class_name] = parse_page_methods(source)
    return pages


# ── Parsing de Steps ──────────────────────────────────────────────────────────

def parse_steps(source: str) -> list[dict]:
    """
    Extrae steps: { annotation, step_text, method_name, page_calls }
    page_calls = lista de métodos de page que llama este step
    """
    # Buscar bloques: @Anotación("texto") \n public void método() { cuerpo }
    pattern = re.compile(
        r'@(Given|When|Then|And|But)\s*\(\s*"([^"]+)"\s*\)\s*'
        r'public\s+void\s+(\w+)\s*\(\s*\)\s*\{([^}]*)\}',
        re.DOTALL
    )
    steps = []
    for match in pattern.finditer(source):
        annotation, step_text, method_name, body = match.groups()
        # Buscar llamadas: instancia.metodo() o metodo()
        calls = re.findall(r'(?:\w+\.)?(\w+)\s*\(', body)
        skip = {"if", "for", "while", "super", "this"}
        calls = [c for c in calls if c not in skip and not c[0].isupper()]
        steps.append({
            "annotation": annotation,
            "step_text": step_text,
            "method_name": method_name,
            "page_calls": list(dict.fromkeys(calls)),
        })
    return steps


def collect_steps_data() -> dict[str, list[dict]]:
    """Retorna { NombreClaseStep: [steps] }"""
    steps_data = {}
    steps_dir = JAVA_ROOT / "steps"
    if not steps_dir.exists():
        return steps_data
    for java_file in sorted(steps_dir.glob("*.java")):
        source = java_file.read_text(encoding="utf-8")
        class_match = re.search(r'public\s+class\s+(\w+)', source)
        if class_match:
            steps_data[class_match.group(1)] = parse_steps(source)
    return steps_data


# ── Parsing de Features ───────────────────────────────────────────────────────

def parse_feature(source: str) -> dict:
    """Extrae { feature_name, scenarios: [{name, steps_text}] }"""
    feature_match = re.search(r'Feature:\s*(.+)', source)
    feature_name = feature_match.group(1).strip() if feature_match else "Feature"

    scenarios = []
    scenario_blocks = re.split(r'\n\s*(?:Scenario|Scenario Outline):', source)
    for block in scenario_blocks[1:]:
        lines = block.strip().splitlines()
        scenario_name = lines[0].strip() if lines else "Scenario"
        step_lines = [
            l.strip() for l in lines[1:]
            if re.match(r'^\s*(Given|When|Then|And|But)\s', l)
        ]
        scenarios.append({"name": scenario_name, "steps": step_lines})
    return {"feature_name": feature_name, "scenarios": scenarios}


def collect_features_data() -> list[dict]:
    features = []
    for feature_file in sorted(JAVA_ROOT.parent.rglob("*.feature")):
        source = feature_file.read_text(encoding="utf-8")
        features.append(parse_feature(source))
    # También buscar en resources
    for feature_file in sorted(Path("src/test/resources").rglob("*.feature")):
        source = feature_file.read_text(encoding="utf-8")
        features.append(parse_feature(source))
    # Deduplicar por nombre
    seen = set()
    unique = []
    for f in features:
        if f["feature_name"] not in seen:
            seen.add(f["feature_name"])
            unique.append(f)
    return unique


# ── Conteo de usos de métodos BasePage ───────────────────────────────────────

def count_basepage_usage(pages_data: dict, basepage_methods: list[dict]) -> dict[str, int]:
    """Cuenta cuántas veces cada método de BasePage es llamado desde pages/*.java"""
    basepage_names = {m["name"] for m in basepage_methods}
    usage = defaultdict(int)
    for page_class, methods in pages_data.items():
        for page_method, calls in methods.items():
            for call in calls:
                if call in basepage_names:
                    usage[call] += 1
    return dict(usage)


# ── Diagrama Mermaid ──────────────────────────────────────────────────────────

def sanitize_mermaid(text: str) -> str:
    """Escapa caracteres problemáticos para Mermaid."""
    return text.replace('"', "'").replace('(', '(').replace(')', ')')


def build_mermaid_section(features: list[dict], steps_data: dict, pages_data: dict) -> str:
    if not features:
        return "_No se encontraron archivos .feature_\n"

    # Construir lookup: texto del step → page_calls
    step_lookup = {}
    for class_name, steps in steps_data.items():
        for step in steps:
            key = step["step_text"].lower().strip()
            step_lookup[key] = step["page_calls"]

    lines = ["```mermaid", "flowchart TD"]

    for feat in features:
        feat_id = re.sub(r'\W+', '_', feat["feature_name"])
        feat_label = sanitize_mermaid(feat["feature_name"])
        lines.append(f'    {feat_id}["📄 {feat_label}"]')

        for sc in feat["scenarios"]:
            sc_id = re.sub(r'\W+', '_', f"{feat_id}_{sc['name']}")
            sc_label = sanitize_mermaid(sc["name"][:40])
            lines.append(f'    {sc_id}["🎬 {sc_label}"]')
            lines.append(f'    {feat_id} --> {sc_id}')

            for step_text in sc["steps"]:
                step_key = re.sub(r'^(Given|When|Then|And|But)\s+', '', step_text, flags=re.I).strip()
                step_id = re.sub(r'\W+', '_', f"{sc_id}_{step_key[:30]}")
                step_label = sanitize_mermaid(step_text[:50])
                lines.append(f'    {step_id}["⚡ {step_label}"]')
                lines.append(f'    {sc_id} --> {step_id}')

                # Buscar page_calls para este step
                page_calls = step_lookup.get(step_key.lower(), [])
                for page_call in page_calls:
                    pc_id = re.sub(r'\W+', '_', f"page_{page_call}")
                    lines.append(f'    {pc_id}["🔧 {page_call}()"]')
                    lines.append(f'    {step_id} --> {pc_id}')

                    # Buscar qué métodos de BasePage llama este método de page
                    for page_class, methods in pages_data.items():
                        if page_call in methods:
                            for bp_call in methods[page_call]:
                                bp_id = re.sub(r'\W+', '_', f"bp_{bp_call}")
                                lines.append(f'    {bp_id}["🏗️ BasePage::{bp_call}()"]')
                                lines.append(f'    {pc_id} --> {bp_id}')

    lines.append("```")
    return "\n".join(lines) + "\n"


# ── Tabla de métodos ──────────────────────────────────────────────────────────

def build_params_cell(params: list[dict], param_docs: dict) -> str:
    if not params:
        return "—"
    parts = []
    for p in params:
        doc = param_docs.get(p["name"], "")
        entry = f"`{p['type']} {p['name']}`"
        if doc:
            entry += f": {doc}"
        parts.append(entry)
    return "<br>".join(parts)


def build_methods_table(methods: list[dict], usage: dict[str, int]) -> str:
    if not methods:
        return "_No hay métodos documentados todavía._\n"

    header = (
        "| Clase | Visibilidad | Método | Descripción | Parámetros | Retorna | # Usos |\n"
        "|-------|-------------|--------|-------------|------------|---------|--------|\n"
    )
    rows = []
    for m in methods:
        clase       = f"`{m['file']}`"
        visibility  = f"`{m['visibility']}`"
        method_sig  = f"`{m['name']}()`"
        description = m["description"] or "—"
        params_cell = build_params_cell(m["params"], m["param_docs"])
        returns     = (
            m["returns_doc"] if m["returns_doc"]
            else ("—" if m["return_type"] == "void" else f"`{m['return_type']}`")
        )
        usos = usage.get(m["name"], 0)
        usos_cell = f"**{usos}**" if usos > 0 else "0"
        rows.append(f"| {clase} | {visibility} | {method_sig} | {description} | {params_cell} | {returns} | {usos_cell} |")

    return header + "\n".join(rows) + "\n"


# ── Actualización del README ──────────────────────────────────────────────────

def replace_section(content: str, start_marker: str, end_marker: str, new_body: str) -> str:
    new_section = f"{start_marker}\n{new_body}\n{end_marker}"
    if start_marker in content and end_marker in content:
        return re.sub(
            rf"{re.escape(start_marker)}.*?{re.escape(end_marker)}",
            new_section,
            content,
            flags=re.DOTALL
        )
    return content.rstrip() + "\n\n" + new_section + "\n"


def update_readme(tree_md: str, mermaid_md: str, table_md: str, method_count: int):
    original = README_FILE.read_text(encoding="utf-8") if README_FILE.exists() else ""

    tree_body    = "## 📁 Estructura del proyecto\n\n" + tree_md
    mermaid_body = "## 🔗 Relación Scenario → Métodos\n\n" + mermaid_md
    methods_body = f"## 📋 Métodos disponibles ({method_count})\n\n" + table_md

    updated = replace_section(original, TREE_START,    TREE_END,    tree_body)
    updated = replace_section(updated,  MERMAID_START, MERMAID_END, mermaid_body)
    updated = replace_section(updated,  METHODS_START, METHODS_END, methods_body)

    README_FILE.write_text(updated, encoding="utf-8")
    print(f"✅ README actualizado — {method_count} método(s) de BasePage documentado(s).")


# ── Main ──────────────────────────────────────────────────────────────────────

def main():
    tree_md       = build_tree_section()
    bp_methods    = collect_basepage_methods()
    pages_data    = collect_pages_data()
    steps_data    = collect_steps_data()
    features      = collect_features_data()
    usage         = count_basepage_usage(pages_data, bp_methods)
    mermaid_md    = build_mermaid_section(features, steps_data, pages_data)
    table_md      = build_methods_table(bp_methods, usage)
    update_readme(tree_md, mermaid_md, table_md, len(bp_methods))


if __name__ == "__main__":
    main()