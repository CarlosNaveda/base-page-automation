"""
generate_readme.py
Genera automáticamente dos secciones del README:
  1. Árbol de estructura del proyecto
  2. Tabla de métodos de BasePage con Javadoc y columna "# Veces Utilizado"
  3. Bubble chart de uso de métodos (docs/bubble_chart.png)

Uso: python3 generate_readme.py
"""

import re
import math
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.colors as mcolors
import numpy as np
from pathlib import Path
from collections import defaultdict

# ── Configuración ─────────────────────────────────────────────────────────────
SRC_ROOTS  = [Path("src/main/java"), Path("src/test/java"), Path("src/test/resources")]
MAIN_ROOT  = Path("src/main/java")
TEST_ROOT  = Path("src/test/java")
README_FILE = Path("README.md")
CHART_FILE  = Path("docs/bubble_chart.png")

TREE_START    = "<!-- TREE:START -->"
TREE_END      = "<!-- TREE:END -->"
METHODS_START = "<!-- METHODS:START -->"
METHODS_END   = "<!-- METHODS:END -->"
CHART_START   = "<!-- CHART:START -->"
CHART_END     = "<!-- CHART:END -->"


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
    Soporta tipos de retorno genéricos como List<Map<String,String>>.
    """
    result = {}
    header_re = re.compile(
        r'(?:public|private|protected)\s+'  # visibilidad
        r'[\w$]+(?:<[^{]+?>)?\s+'           # tipo retorno con opcional genérico
        r'(\w+)\s*\([^)]*\)\s*\{',          # nombre(params) {
        re.DOTALL
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


# ── Bubble chart ───────────────────────────────────────────────────────────────

def generate_bubble_chart(methods: list[dict], usage: dict[str, int]) -> None:
    """
    Genera un bubble chart circular donde cada burbuja es un método de BasePage.
    - Tamaño de burbuja proporcional al número de usos.
    - Color degradado: más usos → color más intenso (índigo/violeta).
    - Métodos sin uso → burbuja pequeña en gris.
    Guarda el resultado en CHART_FILE.
    """
    if not methods:
        return

    names  = [m["name"] for m in methods]
    counts = [usage.get(n, 0) for n in names]
    n      = len(names)

    # ── Layout en espiral/circular ──────────────────────────────────────────
    # Distribuimos los centros en un círculo, con algo de variación radial
    # para que no quede tan rígido.
    rng = np.random.default_rng(42)  # seed fijo para reproducibilidad

    max_count = max(counts) if max(counts) > 0 else 1

    # Radio de cada burbuja: mínimo 0.4, máximo 1.6 (en unidades del plot)
    min_r, max_r = 0.45, 1.7
    radii = [min_r + (c / max_count) * (max_r - min_r) for c in counts]

    # Posicionamiento: empacado en espiral usando golden angle
    golden_angle = math.pi * (3 - math.sqrt(5))
    positions = []
    spread = 1.15  # cuánto se expande la espiral
    for i in range(n):
        r_pos = spread * math.sqrt(i + 0.5)
        theta  = i * golden_angle
        positions.append((r_pos * math.cos(theta), r_pos * math.sin(theta)))

    xs = [p[0] for p in positions]
    ys = [p[1] for p in positions]

    # ── Colores ──────────────────────────────────────────────────────────────
    # Paleta: de gris (#9ca3af) para 0 usos hasta índigo intenso (#4338ca)
    color_zero  = np.array(mcolors.to_rgb("#6b7280"))  # gris medio
    color_low   = np.array(mcolors.to_rgb("#a5b4fc"))  # índigo claro
    color_high  = np.array(mcolors.to_rgb("#3730a3"))  # índigo oscuro

    def get_color(count):
        if count == 0:
            return color_zero
        t = count / max_count  # 0.0 → 1.0
        return color_low + t * (color_high - color_low)

    colors = [get_color(c) for c in counts]

    # ── Canvas ────────────────────────────────────────────────────────────────
    fig_size = max(14, n * 0.55)
    fig, ax = plt.subplots(figsize=(fig_size, fig_size * 0.78))
    fig.patch.set_facecolor("#0d1117")   # fondo oscuro estilo GitHub dark
    ax.set_facecolor("#0d1117")

    # ── Dibujar burbujas ──────────────────────────────────────────────────────
    for x, y, r, color, name, count in zip(xs, ys, radii, colors, names, counts):
        # Sombra sutil
        shadow = plt.Circle((x + 0.06, y - 0.06), r, color="#000000", alpha=0.25, zorder=1)
        ax.add_patch(shadow)

        # Burbuja principal
        circle = plt.Circle((x, y), r, color=color, alpha=0.92, zorder=2)
        ax.add_patch(circle)

        # Borde
        border_color = "#818cf8" if count > 0 else "#4b5563"
        border = plt.Circle((x, y), r, fill=False,
                            edgecolor=border_color, linewidth=1.2, alpha=0.7, zorder=3)
        ax.add_patch(border)

        # Texto: nombre del método
        font_size = max(5.5, min(9.5, r * 5.8))
        # Recortar nombre si es muy largo
        label = name if len(name) <= 22 else name[:20] + "…"
        ax.text(x, y + r * 0.18, label,
                ha="center", va="center", fontsize=font_size,
                color="#f1f5f9", fontweight="bold",
                zorder=4, wrap=False,
                fontfamily="monospace")

        # Texto: conteo de usos
        count_label = str(count) if count > 0 else "—"
        ax.text(x, y - r * 0.32, count_label,
                ha="center", va="center",
                fontsize=max(5, font_size * 0.82),
                color="#cbd5e1" if count > 0 else "#6b7280",
                zorder=4)

    # ── Leyenda ───────────────────────────────────────────────────────────────
    legend_patches = [
        mpatches.Patch(color=color_zero,                   label="Sin uso"),
        mpatches.Patch(color=color_low,                    label="Uso bajo"),
        mpatches.Patch(color=(color_low + color_high) / 2, label="Uso medio"),
        mpatches.Patch(color=color_high,                   label="Uso alto"),
    ]
    legend = ax.legend(
        handles=legend_patches,
        loc="lower right",
        framealpha=0.15,
        facecolor="#1e2533",
        edgecolor="#374151",
        labelcolor="#e2e8f0",
        fontsize=9,
        title="Intensidad de uso",
        title_fontsize=9,
    )
    legend.get_title().set_color("#94a3b8")

    # ── Título ────────────────────────────────────────────────────────────────
    ax.set_title(
        "BasePage — Mapa de uso de métodos",
        color="#e2e8f0", fontsize=15, fontweight="bold", pad=18
    )
    ax.text(
        0.5, 1.01,
        f"{n} métodos  ·  tamaño y color proporcional al número de usos",
        transform=ax.transAxes,
        ha="center", va="bottom",
        fontsize=8.5, color="#64748b"
    )

    # ── Ajustes de ejes ───────────────────────────────────────────────────────
    margin = max_r + 0.5
    all_x = [p[0] for p in positions]
    all_y = [p[1] for p in positions]
    ax.set_xlim(min(all_x) - margin, max(all_x) + margin)
    ax.set_ylim(min(all_y) - margin, max(all_y) + margin)
    ax.set_aspect("equal")
    ax.axis("off")

    plt.tight_layout()

    CHART_FILE.parent.mkdir(parents=True, exist_ok=True)
    fig.savefig(CHART_FILE, dpi=150, bbox_inches="tight",
                facecolor=fig.get_facecolor())
    plt.close(fig)
    print(f"📊 Bubble chart generado en {CHART_FILE}")


# ── Sección chart para README ─────────────────────────────────────────────────

def build_chart_section() -> str:
    return (
        "## 📊 Mapa de uso de métodos\n\n"
        "> Cada burbuja representa un método de `BasePage`.\n"
        "> El **tamaño** y la **intensidad de color** indican cuántas veces ha sido utilizado.\n\n"
        f"![BasePage Methods Usage Chart]({CHART_FILE})\n"
    )


# ── Actualización del README ───────────────────────────────────────────────────

def replace_section(content, start_marker, end_marker, new_body) -> str:
    new_section = f"{start_marker}\n{new_body}\n{end_marker}"
    if start_marker in content and end_marker in content:
        return re.sub(
            rf"{re.escape(start_marker)}.*?{re.escape(end_marker)}",
            new_section, content, flags=re.DOTALL)
    return content.rstrip() + "\n\n" + new_section + "\n"

def update_readme(tree_md, table_md, chart_md, method_count):
    original = README_FILE.read_text(encoding="utf-8") if README_FILE.exists() else ""
    updated  = replace_section(original, TREE_START,    TREE_END,    "## 📁 Estructura del proyecto\n\n" + tree_md)
    updated  = replace_section(updated,  METHODS_START, METHODS_END, f"## 📋 Métodos disponibles ({method_count})\n\n" + table_md)
    updated  = replace_section(updated,  CHART_START,   CHART_END,   chart_md)
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
    generate_bubble_chart(bp_methods, usage)
    chart_md   = build_chart_section()
    update_readme(tree_md, table_md, chart_md, len(bp_methods))

if __name__ == "__main__":
    main()