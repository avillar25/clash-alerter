#!/usr/bin/env python3
"""Genera www/index.html a partir de src/template.html y los datos del juego."""
from pathlib import Path
root = Path(__file__).resolve().parent.parent
src = root / "src"
t = (src / "template.html").read_text(encoding="utf-8")
for key, name in (("__CATALOG__", "catalog.json"), ("__MAXC__", "maxcount.json"), ("__EQ__", "equipment.json")):
    t = t.replace(key, (src / name).read_text(encoding="utf-8"))
import os, json, re
# Versión: la del archivo VERSION (1.1, 1.2…); el historial sale de CHANGELOG.md
version = (root / "VERSION").read_text(encoding="utf-8").strip()
log = []
for block in re.split(r"^## ", (root / "CHANGELOG.md").read_text(encoding="utf-8"), flags=re.M)[1:]:
    head, _, body = block.partition("\n")
    m = re.match(r"v?([\d.]+)\s*(?:—|-)?\s*(.*)", head.strip())
    if m: log.append({"v": "v" + m.group(1), "date": m.group(2).strip(), "text": body.strip()})
t = t.replace("__APP_VERSION__", "v" + version)
t = t.replace("__CHANGELOG__", json.dumps(log, ensure_ascii=False).replace("</", "<\\/"))
t = t.replace("__UPDATE_REPO__", os.environ.get("GITHUB_REPOSITORY", ""))
(root / "www").mkdir(exist_ok=True)
(root / "www" / "index.html").write_text(t, encoding="utf-8")
print("www/index.html generado")
