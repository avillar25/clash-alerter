#!/usr/bin/env python3
"""Genera www/index.html a partir de src/template.html y los datos del juego."""
from pathlib import Path
root = Path(__file__).resolve().parent.parent
src = root / "src"
t = (src / "template.html").read_text(encoding="utf-8")
for key, name in (("__CATALOG__", "catalog.json"), ("__MAXC__", "maxcount.json"), ("__EQ__", "equipment.json")):
    t = t.replace(key, (src / name).read_text(encoding="utf-8"))
import os
t = t.replace("__APP_VERSION__", "v" + os.environ.get("GITHUB_RUN_NUMBER", "0"))
t = t.replace("__UPDATE_REPO__", os.environ.get("GITHUB_REPOSITORY", ""))
(root / "www").mkdir(exist_ok=True)
(root / "www" / "index.html").write_text(t, encoding="utf-8")
print("www/index.html generado")
