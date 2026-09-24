#!/usr/bin/env bash
set -euo pipefail

workspace="${1:?workspace path required}"

# Intentionally empty on the first build.
# If a Minecraft/Fabric API mismatch is exposed by CI, this script is updated
# with a narrow source fix and the workflow is re-run without re-uploading the archive.
test -d "$workspace/src"
