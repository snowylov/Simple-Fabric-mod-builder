package com.alex.universalwrenchapi;

import com.alex.universalwrenchapi.item.UniversalWrenchItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UniversalWrenchApiMod implements ModInitializer {
    public static final String MOD_ID = "universal-wrench-api";
    public static final Map<String, Item> WRENCHES = new LinkedHashMap<>();

    private static final List<String> METAL_WRENCHES = List.of(
            "copper_wrench_tool", "exposed_copper_wrench_tool", "weathered_copper_wrench_tool",
            "oxidized_copper_wrench_tool", "iron_wrench_tool", "gold_wrench_tool", "netherite_wrench_tool"
    );
    private static final List<String> STONE_WRENCHES = List.of(
            "amethyst_wrench_tool", "diamond_wrench_tool", "emerald_wrench_tool",
            "lapis_wrench_tool", "quartz_wrench_tool"
    );
    private static final List<String> OTHER_WRENCHES = List.of(
            "wood_wrench_tool", "bone_wrench_tool", "blaze_wrench_tool",
            "redstone_wrench_tool", "carbon_fiber_wrench_tool"
    );

    @Override
    public void onInitialize() {
        METAL_WRENCHES.forEach(UniversalWrenchApiMod::registerWrench);
        STONE_WRENCHES.forEach(UniversalWrenchApiMod::registerWrench);
        OTHER_WRENCHES.forEach(UniversalWrenchApiMod::registerWrench);

        registerTab("metal_wrenches", METAL_WRENCHES, "iron_wrench_tool");
        registerTab("stone_wrenches", STONE_WRENCHES, "diamond_wrench_tool");
        registerTab("other_wrenches", OTHER_WRENCHES, "wood_wrench_tool");
    }

    private static Item registerWrench(String path) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = new UniversalWrenchItem(new Item.Properties().stacksTo(1).setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        WRENCHES.put(path, item);
        return item;
    }

    private static void registerTab(String name, List<String> contents, String iconItem) {
        Identifier id = id(name);
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id);
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + MOD_ID + "." + name))
                .icon(() -> new ItemStack(WRENCHES.get(iconItem)))
                .displayItems((parameters, output) -> contents.forEach(path -> output.accept(WRENCHES.get(path))))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
