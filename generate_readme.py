"""
generate_readme.py
Genera automáticamente dos secciones del README:
  1. Árbol de estructura del proyecto
  2. Tabla de métodos de BasePage con Javadoc y columna "# Veces Utilizado"

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
METHODS_START = "<!-- METHODS:START -->"
METHODS_END   = "<!-- METHODS:END -->"


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
    Busca recursivamente en MAIN_ROOT/pages para cubrir subdirectorios.
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

def update_readme(tree_md, table_md, method_count):
    original = README_FILE.read_text(encoding="utf-8") if README_FILE.exists() else ""
    updated  = replace_section(original, TREE_START,    TREE_END,    "## 📁 Estructura del proyecto\n\n" + tree_md)
    updated  = replace_section(updated,  METHODS_START, METHODS_END, f"## 📋 Métodos disponibles ({method_count})\n\n" + table_md)
    README_FILE.write_text(updated, encoding="utf-8")
    print(f"✅ README actualizado — {method_count} método(s) de BasePage documentado(s).")


# ── Main ──────────────────────────────────────────────────────────────────────

def main():
    tree_md    = build_tree_section()
    bp_methods = collect_basepage_methods()
    bp_names   = {m["name"] for m in bp_methods}
    pages_data = collect_pages_data(bp_names)
    usage      = count_basepage_usage(bp_methods, pages_data)
    table_md   = build_methods_table(bp_methods, usage)
    update_readme(tree_md, table_md, len(bp_methods))

if __name__ == "__main__":
    main()