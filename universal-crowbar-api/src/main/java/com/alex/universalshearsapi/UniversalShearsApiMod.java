package com.alex.universalshearsapi;

import com.alex.universalshearsapi.item.UniversalShearsItem;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class UniversalShearsApiMod implements ModInitializer {
    public static final String MOD_ID = "universal-shears-api";
    public static final Map<String, Item> SHEARS = new LinkedHashMap<>();

    private static final List<ShearDefinition> DEFINITIONS = List.of(
            new ShearDefinition("wood_shears", ShearDurabilities.WOOD),
            new ShearDefinition("stone_shears", ShearDurabilities.STONE),
            new ShearDefinition("copper_shears", ShearDurabilities.COPPER),
            new ShearDefinition("iron_shears", ShearDurabilities.IRON),
            new ShearDefinition("gold_shears", ShearDurabilities.GOLD),
            new ShearDefinition("diamond_shears", ShearDurabilities.DIAMOND),
            new ShearDefinition("netherite_shears", ShearDurabilities.NETHERITE),
            new ShearDefinition("quartz_shears", ShearDurabilities.QUARTZ),
            new ShearDefinition("lapis_shears", ShearDurabilities.LAPIS),
            new ShearDefinition("amethyst_shears", ShearDurabilities.AMETHYST),
            new ShearDefinition("ruby_shears", ShearDurabilities.RUBY)
    );

    @Override
    public void onInitialize() {
        DEFINITIONS.forEach(UniversalShearsApiMod::registerShears);
        registerCreativeTab();
    }

    public static Item registerShears(String path, int durability) {
        Identifier id = id(path);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item item = new UniversalShearsItem(new Item.Properties()
                .durability(durability)
                .setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        SHEARS.put(path, item);
        return item;
    }

    private static void registerShears(ShearDefinition definition) {
        registerShears(definition.id(), definition.durability());
    }

    private static void registerCreativeTab() {
        Identifier id = id("shears");
        ResourceKey<CreativeModeTab> key = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), id);
        CreativeModeTab tab = FabricItemGroup.builder()
                .title(Component.translatable("itemGroup." + MOD_ID + ".shears"))
                .icon(() -> new ItemStack(SHEARS.get("diamond_shears")))
                .displayItems((parameters, output) -> DEFINITIONS.forEach(definition ->
                        output.accept(SHEARS.get(definition.id()))))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, key, tab);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    private record ShearDefinition(String id, int durability) {
    }
}
