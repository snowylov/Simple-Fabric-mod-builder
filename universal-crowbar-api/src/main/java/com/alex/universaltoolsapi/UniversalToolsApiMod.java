package com.alex.universaltoolsapi;

import com.alex.universaltoolsapi.item.ConfigurationToolItem;
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

public final class UniversalToolsApiMod implements ModInitializer {
    public static final String MOD_ID = "universal-tools-api";

    public static Item CONFIGURATION_TOOL;
    public static Item CONFIGURATION_TOOL_ACTIVE;

    @Override
    public void onInitialize() {
        CONFIGURATION_TOOL = registerItem(
                "configuration_tool",
                new ConfigurationToolItem(properties("configuration_tool"), false)
        );
        CONFIGURATION_TOOL_ACTIVE = registerItem(
                "configuration_tool_active",
                new ConfigurationToolItem(properties("configuration_tool_active"), true)
        );
        registerCreativeTab();
    }

    private static Item.Properties properties(String path) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        return new Item.Properties().stacksTo(1).setId(key);
    }

    private static Item registerItem(String path, Item item) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static void registerCreativeTab() {
        Identifier id = id("configuration_tools");
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id);
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + MOD_ID + ".configuration_tools"))
                .icon(() -> new ItemStack(CONFIGURATION_TOOL))
                .displayItems((parameters, output) -> {
                    output.accept(CONFIGURATION_TOOL);
                    output.accept(CONFIGURATION_TOOL_ACTIVE);
                })
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
