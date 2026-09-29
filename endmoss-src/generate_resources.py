from pathlib import Path
import json

root = Path("src/main/resources")
assets = root / "assets/bacterium"
data = root / "data"
flowers = [8,12,15,16,18,22,23,34,37,38]
ids = [f"end_flower_{n:02d}" for n in flowers]

def write(path, obj):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(obj, indent=2) + "\n")

# Blockstates and block models
write(assets/"blockstates/end_moss.json", {"variants":{"":{"model":"bacterium:block/end_moss"}}})
write(assets/"models/block/end_moss.json", {"parent":"minecraft:block/cube_all","textures":{"all":"bacterium:block/end_moss_top"}})
write(assets/"blockstates/end_moss_carpet.json", {"variants":{"":{"model":"bacterium:block/end_moss_carpet"}}})
write(assets/"models/block/end_moss_carpet.json", {
  "textures":{"particle":"bacterium:block/end_moss_top","top":"bacterium:block/end_moss_top","side":"bacterium:block/end_moss_side"},
  "elements":[{
    "from":[0,0,0],"to":[16,1,16],
    "faces":{
      "north":{"uv":[0,0,16,1],"texture":"#side"},
      "east":{"uv":[0,0,16,1],"texture":"#side"},
      "south":{"uv":[0,0,16,1],"texture":"#side"},
      "west":{"uv":[0,0,16,1],"texture":"#side"},
      "up":{"uv":[0,0,16,16],"texture":"#top"},
      "down":{"uv":[0,0,16,16],"texture":"#top"}
    }
  }]
})
for name in ids:
    write(assets/f"blockstates/{name}.json", {"variants":{"":{"model":f"bacterium:block/{name}"}}})
    write(assets/f"models/block/{name}.json", {"parent":"minecraft:block/cross","textures":{"cross":f"bacterium:block/{name}"}})

# 1.21.11 item definitions and compatibility item models
for name in ["end_moss","end_moss_carpet",*ids]:
    write(assets/f"items/{name}.json", {"model":{"type":"minecraft:model","model":f"bacterium:block/{name}"}})
    write(assets/f"models/item/{name}.json", {"parent":f"bacterium:block/{name}"})

# Language
lang = {
  "block.bacterium.end_moss":"End Moss",
  "block.bacterium.end_moss_carpet":"End Moss Carpet",
  "biome.bacterium.end_moss_gardens":"End Moss Gardens"
}
for n,name in zip(flowers,ids):
    lang[f"block.bacterium.{name}"] = f"End Flower {n:02d}"
write(assets/"lang/en_us.json", lang)

# Common tags and vanilla flower tag.
tag_values=[f"bacterium:{name}" for name in ids]
write(data/"c/tags/block/end_moss_flower.json", {"replace":False,"values":tag_values})
write(data/"c/tags/item/end_moss_flower.json", {"replace":False,"values":tag_values})
write(data/"minecraft/tags/block/flowers.json", {"replace":False,"values":tag_values})
write(data/"minecraft/tags/block/mineable/hoe.json", {"replace":False,"values":["bacterium:end_moss","bacterium:end_moss_carpet"]})

# Loot tables
for name in ["end_moss","end_moss_carpet",*ids]:
    write(data/f"bacterium/loot_table/blocks/{name}.json", {
      "type":"minecraft:block",
      "pools":[{
        "rolls":1,
        "entries":[{"type":"minecraft:item","name":f"bacterium:{name}"}],
        "conditions":[{"condition":"minecraft:survives_explosion"}]
      }]
    })

# Biome and feature.
write(data/"bacterium/worldgen/biome/end_moss_gardens.json", {
  "has_precipitation":False,
  "temperature":0.5,
  "downfall":0.0,
  "effects":{
    "fog_color":10518688,"sky_color":0,"water_color":4159204,"water_fog_color":329011,
    "mood_sound":{"sound":"minecraft:ambient.cave","tick_delay":6000,"block_search_extent":8,"offset":2.0}
  },
  "carvers":[],
  "features":[[],[],[],[],[],[],[],[],[],[],["bacterium:end_moss_surface"]],
  "spawn_costs":{},
  "spawners":{
    "ambient":[],"axolotls":[],"creature":[],"misc":[],
    "monster":[{"type":"minecraft:enderman","weight":10,"minCount":4,"maxCount":4}],
    "underground_water_creature":[],"water_ambient":[],"water_creature":[]
  }
})
write(data/"bacterium/worldgen/configured_feature/end_moss_surface.json", {"type":"bacterium:end_moss_surface","config":{}})
write(data/"bacterium/worldgen/placed_feature/end_moss_surface.json", {
  "feature":"bacterium:end_moss_surface",
  "placement":[
    {"type":"minecraft:count","count":1},
    {"type":"minecraft:in_square"},
    {"type":"minecraft:heightmap","heightmap":"WORLD_SURFACE_WG"}
  ]
})
