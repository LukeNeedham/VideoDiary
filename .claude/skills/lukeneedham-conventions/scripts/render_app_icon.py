#!/usr/bin/env python3
"""Render app-icon.png from an Android app's adaptive launcher icon.

The result is what a circular launcher mask shows: a 512x512 PNG, transparent outside a circle that
holds the icon's background, with the foreground at the same size and position as in the adaptive
icon (the visible circle is the centre 72dp of the 108dp adaptive canvas). See docs/app-icon.md in
LukeNeedham/ci-workflows.

Usage:  python3 render_app_icon.py [APP_DIR] [-o OUTPUT]
        APP_DIR defaults to the current directory, OUTPUT to APP_DIR/app-icon.png.
Needs:  pip install cairosvg pillow
"""
import argparse
import base64
import io
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ANDROID = "{http://schemas.android.com/apk/res/android}"
AAPT = "{http://schemas.android.com/aapt}"
CANVAS = 108  # dp, the adaptive icon canvas
VISIBLE = 72  # dp, the circular mask's diameter
SIZE = 512  # px, the output
DENSITIES = ["xxxhdpi", "xxhdpi", "xhdpi", "hdpi", "mdpi", "nodpi", "anydpi"]


def attr(el, name, default=None):
    return el.get(ANDROID + name, default)


def number(value, default=0.0):
    return default if value is None else float(re.sub(r"[a-z]+$", "", value))


def res_dirs(app_dir):
    return sorted(p for p in app_dir.rglob("res") if p.is_dir() and "build" not in p.relative_to(app_dir).parts)


def find_resource(dirs, kind, name):
    """The best file for @kind/name: the highest density, vectors and XML last resort."""
    found = []
    for res in dirs:
        for folder in res.glob(kind + "*"):
            for f in folder.glob(name + ".*"):
                dens = next((i for i, d in enumerate(DENSITIES) if folder.name.endswith(d)), len(DENSITIES))
                found.append((f.suffix == ".xml", dens, str(f), f))
    return min(found)[3] if found else None


def color_values(dirs):
    colors = {}
    for res in dirs:
        for f in res.glob("values*/*.xml"):
            for c in ET.parse(f).getroot().iter("color"):
                colors[c.get("name")] = (c.text or "").strip()
    for _ in range(5):  # colors can refer to colors
        for k, v in colors.items():
            if v.startswith("@color/"):
                colors[k] = colors.get(v[7:], v)
    return colors


def paint(value, colors):
    """An Android color (#RRGGBB, #AARRGGBB or @color/x) as (svg colour, opacity)."""
    if value.startswith("@color/"):
        value = colors[value[7:]]
    value = value.lstrip("#")
    if len(value) in (3, 4):
        value = "".join(c * 2 for c in value)
    if len(value) == 8:
        return "#" + value[2:], int(value[:2], 16) / 255
    return "#" + value, 1.0


class Converter:
    def __init__(self, colors):
        self.colors = colors
        self.defs = []
        self.n = 0

    def uid(self, prefix):
        self.n += 1
        return f"{prefix}{self.n}"

    def gradient(self, g):
        gid = self.uid("g")
        kind = attr(g, "type", "linear")
        stops = [(number(attr(i, "offset")), attr(i, "color")) for i in g.iter("item")]
        if not stops:
            stops = [(0, attr(g, "startColor", "#000000")), (1, attr(g, "endColor", "#000000"))]
            if attr(g, "centerColor"):
                stops.insert(1, (0.5, attr(g, "centerColor")))
        body = ""
        for off, col in stops:
            c, a = paint(col, self.colors)
            body += f'<stop offset="{off}" stop-color="{c}" stop-opacity="{a}"/>'
        if kind == "radial":
            head = (f'<radialGradient id="{gid}" gradientUnits="userSpaceOnUse" cx="{number(attr(g, "centerX"))}" '
                    f'cy="{number(attr(g, "centerY"))}" r="{number(attr(g, "gradientRadius"))}">')
            tail = "</radialGradient>"
        else:  # sweep gradients have no SVG equivalent: they are drawn as a linear one
            head = (f'<linearGradient id="{gid}" gradientUnits="userSpaceOnUse" x1="{number(attr(g, "startX"))}" '
                    f'y1="{number(attr(g, "startY"))}" x2="{number(attr(g, "endX"))}" y2="{number(attr(g, "endY"))}">')
            tail = "</linearGradient>"
        self.defs.append(head + body + tail)
        return f"url(#{gid})"

    def fill_or_stroke(self, el, name):
        for a in el.findall(AAPT + "attr"):
            if a.get("name") == "android:" + name:
                for g in a.iter("gradient"):
                    return self.gradient(g), 1.0
        value = attr(el, name)
        return paint(value, self.colors) if value else (None, 1.0)

    def path(self, el):
        fill, fill_op = self.fill_or_stroke(el, "fillColor")
        stroke, stroke_op = self.fill_or_stroke(el, "strokeColor")
        fill_alpha = number(attr(el, "fillAlpha"), 1.0) * fill_op
        stroke_alpha = number(attr(el, "strokeAlpha"), 1.0) * stroke_op
        rule = "evenodd" if attr(el, "fillType") == "evenOdd" else "nonzero"
        s = f'<path d="{attr(el, "pathData")}" fill="{fill or "none"}" fill-opacity="{fill_alpha}" fill-rule="{rule}"'
        if stroke:
            s += (f' stroke="{stroke}" stroke-opacity="{stroke_alpha}" stroke-width="{number(attr(el, "strokeWidth"), 0)}"'
                  f' stroke-linecap="{attr(el, "strokeLineCap", "butt")}" stroke-linejoin="{attr(el, "strokeLineJoin", "miter")}"'
                  f' stroke-miterlimit="{attr(el, "strokeMiterLimit", "4")}"')
        return s + "/>"

    def children(self, el):
        out, closers = "", 0
        for child in el:
            tag = child.tag
            if tag == "path":
                out += self.path(child)
            elif tag == "group":
                out += self.group(child)
            elif tag == "clip-path":  # clips everything after it in the same group
                cid = self.uid("c")
                self.defs.append(f'<clipPath id="{cid}"><path d="{attr(child, "pathData")}"/></clipPath>')
                out += f'<g clip-path="url(#{cid})">'
                closers += 1
        return out + "</g>" * closers

    def group(self, g):
        px, py = number(attr(g, "pivotX")), number(attr(g, "pivotY"))
        t = (f'translate({number(attr(g, "translateX"))} {number(attr(g, "translateY"))}) translate({px} {py}) '
             f'rotate({number(attr(g, "rotation"))}) scale({number(attr(g, "scaleX"), 1.0)} {number(attr(g, "scaleY"), 1.0)}) '
             f'translate({-px} {-py})')
        return f'<g transform="{t}">{self.children(g)}</g>'

    def vector(self, root):
        vw, vh = number(attr(root, "viewportWidth")), number(attr(root, "viewportHeight"))
        w, h = number(attr(root, "width"), CANVAS), number(attr(root, "height"), CANVAS)
        return f'<g transform="scale({w / vw} {h / vh})">{self.children(root)}</g>'


def layer(dirs, colors, conv, drawable):
    """SVG for one adaptive icon layer (a colour, a vector or a raster), filling the 108dp canvas."""
    if drawable.startswith("#") or drawable.startswith("@color/"):
        c, a = paint(drawable, colors)
        return f'<rect x="0" y="0" width="{CANVAS}" height="{CANVAS}" fill="{c}" fill-opacity="{a}"/>'
    kind, name = drawable.lstrip("@").split("/")
    if kind == "color":
        return layer(dirs, colors, conv, "@color/" + name)
    f = find_resource(dirs, kind, name)
    if f is None:
        sys.exit(f"Cannot find {drawable}")
    if f.suffix == ".xml":
        root = ET.parse(f).getroot()
        if root.tag == "vector":
            return conv.vector(root)
        sys.exit(f"{f}: only vector drawables and images are supported, not <{root.tag}>")
    from PIL import Image
    buf = io.BytesIO()
    Image.open(f).convert("RGBA").save(buf, "PNG")
    data = base64.b64encode(buf.getvalue()).decode()
    return (f'<image x="0" y="0" width="{CANVAS}" height="{CANVAS}" preserveAspectRatio="none" '
            f'href="data:image/png;base64,{data}"/>')


def adaptive_icon(app_dir, dirs):
    manifests = [m for m in app_dir.rglob("AndroidManifest.xml") if "build" not in m.relative_to(app_dir).parts]
    name = None
    for m in manifests:
        icon = ET.parse(m).getroot().find("application")
        if icon is not None and attr(icon, "icon"):
            name = attr(icon, "icon").split("/")[-1]
            break
    if name is None:
        sys.exit("No android:icon on an <application> in any AndroidManifest.xml")
    for res in dirs:
        for folder in sorted(res.glob("mipmap-anydpi*"), reverse=True):  # -v26 before the bare one
            f = folder / (name + ".xml")
            if f.exists() and ET.parse(f).getroot().tag == "adaptive-icon":
                return ET.parse(f).getroot()
    sys.exit(f"@mipmap/{name} is not an adaptive icon (no mipmap-anydpi*/{name}.xml)")


def render(app_dir):
    import cairosvg
    dirs = res_dirs(app_dir)
    colors = color_values(dirs)
    conv = Converter(colors)
    icon = adaptive_icon(app_dir, dirs)
    layers = ""
    for tag in ("background", "foreground"):
        el = icon.find(tag)
        if el is None:
            sys.exit(f"The adaptive icon has no <{tag}>")
        layers += layer(dirs, colors, conv, attr(el, "drawable"))
    lo = (CANVAS - VISIBLE) / 2
    svg = (f'<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="{SIZE}" '
           f'height="{SIZE}" viewBox="{lo} {lo} {VISIBLE} {VISIBLE}"><defs>{"".join(conv.defs)}'
           f'<clipPath id="mask"><circle cx="{CANVAS / 2}" cy="{CANVAS / 2}" r="{VISIBLE / 2}"/></clipPath></defs>'
           f'<g clip-path="url(#mask)">{layers}</g></svg>')
    return cairosvg.svg2png(bytestring=svg.encode(), output_width=SIZE, output_height=SIZE)


if __name__ == "__main__":
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("app_dir", nargs="?", default=".")
    ap.add_argument("-o", "--output")
    args = ap.parse_args()
    app = Path(args.app_dir).resolve()
    out = Path(args.output) if args.output else app / "app-icon.png"
    out.write_bytes(render(app))
    print(f"Wrote {out}")
