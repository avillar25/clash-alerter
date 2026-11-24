#!/usr/bin/env python3
"""Genera el icono (anillo de progreso + martillo de construcción + reloj de arena) y la pantalla de inicio."""
import math
from pathlib import Path
from PIL import Image, ImageDraw

root = Path(__file__).resolve().parent.parent
res = root / "android/app/src/main/res"
BG = (29, 36, 51)            # azul noche
TRACK = (58, 69, 96)
GOLD = (242, 182, 50)
WOOD = (168, 107, 53); WOOD_D = (122, 74, 34); STEEL = (213, 218, 226); STEEL_D = (150, 158, 172)

def round_cap(d, c, r, w, ang):
    a = math.radians(ang); x = c + (r - w / 2) * math.cos(a); y = c + (r - w / 2) * math.sin(a)
    d.ellipse([x - w / 2, y - w / 2, x + w / 2, y + w / 2], fill=GOLD)

def hourglass(size):
    """Reloj de arena dibujado (insignia redonda)."""
    s = size; im = Image.new("RGBA", (s, s), (0, 0, 0, 0)); d = ImageDraw.Draw(im)
    d.ellipse([0, 0, s - 1, s - 1], fill=GOLD)
    p = s * 0.07; d.ellipse([p, p, s - 1 - p, s - 1 - p], fill=BG)
    cx = s / 2; top = s * 0.24; bot = s * 0.76; hw = s * 0.2; bar = s * 0.07
    glass = (244, 227, 193); sand = GOLD; wood = (168, 107, 53)
    d.polygon([(cx - hw, top), (cx + hw, top), (cx + s * 0.03, s / 2), (cx - s * 0.03, s / 2)], fill=glass)
    d.polygon([(cx - s * 0.03, s / 2), (cx + s * 0.03, s / 2), (cx + hw, bot), (cx - hw, bot)], fill=glass)
    d.polygon([(cx - hw * 0.55, top + (s / 2 - top) * 0.45), (cx + hw * 0.55, top + (s / 2 - top) * 0.45), (cx, s / 2 - s * 0.01)], fill=sand)
    d.polygon([(cx - hw * 0.85, bot - s * 0.01), (cx + hw * 0.85, bot - s * 0.01), (cx, bot - s * 0.13)], fill=sand)
    d.line([(cx, s / 2), (cx, bot - s * 0.1)], fill=sand, width=max(1, int(s * 0.02)))
    d.rounded_rectangle([cx - hw - s * 0.05, top - bar, cx + hw + s * 0.05, top], radius=bar / 3, fill=wood)
    d.rounded_rectangle([cx - hw - s * 0.05, bot, cx + hw + s * 0.05, bot + bar], radius=bar / 3, fill=wood)
    return im

def builder_hammer(T):
    """Martillo de constructor dibujado: mango de madera y cabeza de acero, inclinado."""
    L = Image.new("RGBA", (T, T), (0, 0, 0, 0)); d = ImageDraw.Draw(L); c = T / 2
    hw = T * 0.042; hl = T * 0.40                                     # mango
    d.rounded_rectangle([c - hw, c - T * 0.06, c + hw, c - T * 0.06 + hl], radius=hw, fill=WOOD)
    d.rounded_rectangle([c - hw, c - T * 0.06, c - hw * 0.25, c - T * 0.06 + hl], radius=hw * 0.6, fill=WOOD_D)
    d.rounded_rectangle([c - hw * 1.25, c + T * 0.25, c + hw * 1.25, c + T * 0.30], radius=hw * 0.5, fill=WOOD_D)   # agarre
    w2 = T * 0.17; h2 = T * 0.075; top = c - T * 0.155                 # cabeza
    d.rounded_rectangle([c - w2, top, c + w2, top + h2 * 2], radius=h2 * 0.45, fill=STEEL)
    d.rounded_rectangle([c - w2, top + h2 * 1.35, c + w2, top + h2 * 2], radius=h2 * 0.45, fill=STEEL_D)
    d.rounded_rectangle([c - w2 - T * 0.03, top + h2 * 0.15, c - w2 + T * 0.01, top + h2 * 1.85], radius=T * 0.012, fill=STEEL_D)  # boca
    d.rounded_rectangle([c - hw * 1.4, top - T * 0.005, c + hw * 1.4, top + h2 * 2 + T * 0.005], radius=hw * 0.4, fill=WOOD_D)    # cuña
    return L.rotate(-35, resample=Image.BICUBIC, center=(c, c))

def emblem(T):
    """El dibujo sin fondo, centrado en un lienzo T×T (cabe en la zona segura de Android)."""
    im = Image.new("RGBA", (T, T), (0, 0, 0, 0)); d = ImageDraw.Draw(im)
    c = T / 2; r = T * 0.40; w = T * 0.065
    d.ellipse([c - r, c - r, c + r, c + r], outline=TRACK, width=int(w))
    d.arc([c - r, c - r, c + r, c + r], start=-90, end=200, fill=GOLD, width=int(w))
    round_cap(d, c, r, w, -90); round_cap(d, c, r, w, 200)
    im.alpha_composite(builder_hammer(T), (0, 0))
    b = hourglass(int(T * 0.30)); a = math.radians(45); bx = c + r * 0.80 * math.cos(a); by = c + r * 0.80 * math.sin(a)
    im.alpha_composite(b, (int(bx - b.width / 2), int(by - b.height / 2)))
    return im

S = 4  # dibujar a 4× y reducir para que quede suave
big = emblem(1024 * S // 2)

def save(img, path, size):
    path.parent.mkdir(parents=True, exist_ok=True); img.resize((size, size), Image.LANCZOS).save(path)

dens = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}
for name, k in dens.items():
    n = int(48 * k); fg = int(108 * k)
    # Primer plano adaptativo: el dibujo ocupa el 62 % central (zona segura 66 %)
    canvas = Image.new("RGBA", (big.width * 100 // 62,) * 2, (0, 0, 0, 0))
    canvas.alpha_composite(big, ((canvas.width - big.width) // 2,) * 2)
    save(canvas, res / f"mipmap-{name}/ic_launcher_foreground.png", fg)
    # Icono clásico (cuadrado redondeado) y redondo
    sq = Image.new("RGBA", (big.width,) * 2, (0, 0, 0, 0)); ImageDraw.Draw(sq).rounded_rectangle([0, 0, sq.width - 1, sq.width - 1], radius=sq.width * 0.22, fill=BG)
    inner = big.resize((int(big.width * 0.86),) * 2, Image.LANCZOS); sq.alpha_composite(inner, ((sq.width - inner.width) // 2,) * 2)
    save(sq, res / f"mipmap-{name}/ic_launcher.png", n)
    rd = sq   # contorno cuadrado también donde el móvil pide el icono "redondo"
    save(rd, res / f"mipmap-{name}/ic_launcher_round.png", n)

(res / "values/ic_launcher_background.xml").write_text('<?xml version="1.0" encoding="utf-8"?>\n<resources>\n    <color name="ic_launcher_background">#1D2433</color>\n</resources>\n')

# Pantallas de inicio: el icono centrado sobre el mismo fondo
for f in res.glob("drawable*/splash.png"):
    w, h = Image.open(f).size
    sp = Image.new("RGBA", (w, h), BG + (255,)); m = int(min(w, h) * 0.45)
    e = big.resize((m, m), Image.LANCZOS); sp.alpha_composite(e, ((w - m) // 2, (h - m) // 2)); sp.convert("RGB").save(f)

# Vista previa para revisarlo
prev = Image.new("RGBA", (big.width,) * 2, (0, 0, 0, 0)); prev.alpha_composite(Image.open(res / "mipmap-xxxhdpi/ic_launcher.png").resize(prev.size))
Image.open(res / "mipmap-xxxhdpi/ic_launcher.png").save(root / "assets-src/icon-preview.png")
print("Icono generado")
