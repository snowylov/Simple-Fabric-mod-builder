package com.alex.shelffaces;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ShelfFacesMod implements ModInitializer {
    public static final String MOD_ID = "shelffaces";

    public static final String[] WOODS = {
            "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove",
            "cherry", "pale_oak", "bamboo", "crimson", "warped", "beech", "poplar", "red_cherry"
    };

    public static final Map<String, FaceShelfBlock> SHELVES = new LinkedHashMap<>();
    public static Item SHELF_WRENCH;
    public static BlockEntityType<FaceShelfBlockEntity> SHELF_BLOCK_ENTITY;

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        registerBlocksAndItems();
        registerBlockEntity();
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(SHELF_WRENCH);
            SHELVES.values().forEach(entries::add);
        });
    }

    private static void registerBlocksAndItems() {
        for (String wood : WOODS) {
            String name = wood + "_shelf";
            Identifier id = id(name);
            RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
            FaceShelfBlock block = new FaceShelfBlock(
                    wood,
                    AbstractBlock.Settings.copy(Blocks.OAK_PLANKS).registryKey(blockKey)
            );
            Registry.register(Registries.BLOCK, id, block);
            SHELVES.put(wood, block);

            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
            Registry.register(Registries.ITEM, id, blockItem);
        }

        Identifier wrenchId = id("shelf_wrench");
        RegistryKey<Item> wrenchKey = RegistryKey.of(RegistryKeys.ITEM, wrenchId);
        SHELF_WRENCH = Registry.register(
                Registries.ITEM,
                wrenchId,
                new ShelfWrenchItem(new Item.Settings().registryKey(wrenchKey).maxCount(1))
        );
    }

    private static void registerBlockEntity() {
        SHELF_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                id("shelf"),
                BlockEntityType.Builder.create(FaceShelfBlockEntity::new, SHELVES.values().toArray(Block[]::new)).build(null)
        );
    }
}
