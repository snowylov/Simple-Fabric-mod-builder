#!/usr/bin/env bash
set -euo pipefail

workspace="${1:?workspace path required}"

python3 - "$workspace" <<'PY'
from pathlib import Path
import sys

root = Path(sys.argv[1])

entity_path = root / "src/main/java/com/noobvill/celegans/entity/CElegansEntity.java"
entity = entity_path.read_text(encoding="utf-8")
entity = entity.replace("public WormBrain getBrain()", "public WormBrain getWormBrain()")
entity_path.write_text(entity, encoding="utf-8")

brain_path = root / "src/main/java/com/noobvill/celegans/ai/WormBrain.java"
brain = brain_path.read_text(encoding="utf-8")
brain = brain.replace("child.getBrain()", "child.getWormBrain()")
brain = brain.replace(".getPos()", ".getEntityPos()")
brain_path.write_text(brain, encoding="utf-8")
PY
