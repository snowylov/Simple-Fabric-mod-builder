package com.alex.universalcrowbarapi;

import com.alex.universalcrowbarapi.block.FourWayRailBlock;
import com.alex.universalcrowbarapi.item.UniversalCrowbarItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UniversalCrowbarApiMod implements ModInitializer {
    public static final String MOD_ID = "universal-crowbar-api";
    public static final Map<String, Item> CROWBARS = new LinkedHashMap<>();
    public static final Map<String, Item> GEARS = new LinkedHashMap<>();
    public static FourWayRailBlock FOUR_WAY_RAIL;
    private static final List<String> CROWBAR_IDS = List.of(
            "amethyst_crowbar_tool", "certus_quartz_crowbar_tool", "copper_crowbar_tool",
            "diamond_crowbar_tool", "gold_crowbar_tool", "iron_crowbar_tool", "lapis_crowbar_tool",
            "netherite_crowbar_tool", "quartz_crowbar_tool", "redstone_crowbar_tool",
            "ruby_crowbar_tool", "stone_crowbar_tool", "wood_crowbar_tool"
    );
    private static final List<String> GEAR_IDS = List.of(
            "acacia_small_standard_gear","acacia_standard_gear","amethyst_small_standard_gear","amethyst_standard_gear",
            "bamboo_small_standard_gear","bamboo_standard_gear","beech_small_standard_gear","beech_standard_gear",
            "birch_small_standard_gear","birch_standard_gear","blackstone_small_standard_gear","blackstone_standard_gear",
            "blaze_small_standard_gear","blaze_standard_gear","bone_small_standard_gear","bone_standard_gear",
            "cherry_small_standard_gear","cherry_standard_gear","cobblestone_small_standard_gear","cobblestone_standard_gear",
            "copper_small_standard_gear","copper_standard_gear","crimson_small_standard_gear","crimson_standard_gear",
            "crying_obsidian_small_standard_gear","crying_obsidian_standard_gear","dark_oak_small_standard_gear","dark_oak_standard_gear",
            "dark_prismarine_small_standard_gear","dark_prismarine_standard_gear","deepslate_small_standard_gear","deepslate_standard_gear",
            "diamond_small_standard_gear","diamond_standard_gear","emerald_small_standard_gear","emerald_standard_gear",
            "end_stone_small_standard_gear","end_stone_standard_gear","exposed_copper_small_standard_gear","exposed_copper_standard_gear",
            "glowstone_small_standard_gear","glowstone_standard_gear","gold_small_standard_gear","gold_standard_gear",
            "iron_small_standard_gear","iron_standard_gear","jungle_small_standard_gear","jungle_standard_gear",
            "lapis_small_standard_gear","lapis_standard_gear","mangrove_small_standard_gear","mangrove_standard_gear",
            "netherite_small_standard_gear","netherite_standard_gear","oak_small_standard_gear","oak_standard_gear",
            "obsidian_small_standard_gear","obsidian_standard_gear","oxidized_copper_small_standard_gear","oxidized_copper_standard_gear",
            "pale_oak_small_standard_gear","pale_oak_standard_gear","prismarine_small_standard_gear","prismarine_standard_gear",
            "purpur_small_standard_gear","purpur_standard_gear","quartz_small_standard_gear","quartz_standard_gear",
            "redstone_small_standard_gear","redstone_standard_gear","spruce_small_standard_gear","spruce_standard_gear",
            "stone_small_standard_gear","stone_standard_gear","terracotta_small_standard_gear","terracotta_standard_gear",
            "warped_small_standard_gear","warped_standard_gear","weathered_copper_small_standard_gear","weathered_copper_standard_gear"
    );

    @Override
    public void onInitialize() {
        registerFourWayRail();
        CROWBAR_IDS.forEach(UniversalCrowbarApiMod::registerCrowbar);
        GEAR_IDS.forEach(UniversalCrowbarApiMod::registerGear);
        registerCrowbarTab();
        registerGearTab();
    }

    private static void registerFourWayRail() {
        Identifier id = id("four_way_rail");
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        FOUR_WAY_RAIL = new FourWayRailBlock(
                BlockBehaviour.Properties.of()
                        .noCollision()
                        .strength(0.7F)
                        .sound(SoundType.METAL)
                        .setId(key)
        );
        Registry.register(BuiltInRegistries.BLOCK, key, FOUR_WAY_RAIL);
    }

    private static void registerCrowbarTab() {
        Identifier id = id("crowbars");
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id);
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + MOD_ID + ".crowbars"))
                .icon(() -> new ItemStack(CROWBARS.get("iron_crowbar_tool")))
                .displayItems((parameters, output) -> CROWBAR_IDS.forEach(path -> output.accept(CROWBARS.get(path))))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    private static void registerGearTab() {
        Identifier id = id("gears");
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id);
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + MOD_ID + ".gears"))
                .icon(() -> new ItemStack(GEARS.get("iron_standard_gear")))
                .displayItems((parameters, output) -> GEAR_IDS.forEach(path -> output.accept(GEARS.get(path))))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    private static Item registerCrowbar(String path) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = new UniversalCrowbarItem(new Item.Properties().stacksTo(1).setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        CROWBARS.put(path, item);
        return item;
    }

    private static Item registerGear(String path) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = new Item(new Item.Properties().stacksTo(64).setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        GEARS.put(path, item);
        return item;
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
