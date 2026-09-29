package com.alex.bacterium;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import terrablender.api.EndBiomeRegistry;

import java.util.ArrayList;
import java.util.List;

public final class EndMossContent {
    public static final RegistryKey<Biome> END_MOSS_GARDENS = RegistryKey.of(
            RegistryKeys.BIOME, BacteriumMod.id("end_moss_gardens")
    );

    public static EndMossBlock END_MOSS;
    public static EndMossCarpetBlock END_MOSS_CARPET;
    public static final List<EndMossFlowerBlock> END_FLOWERS = new ArrayList<>();
    public static Feature<DefaultFeatureConfig> END_MOSS_SURFACE_FEATURE;

    private EndMossContent() {}

    public static void initialize() {
        END_MOSS = registerBlock("end_moss",
                new EndMossBlock(AbstractBlock.Settings.copy(Blocks.MOSS_BLOCK)
                        .strength(0.1f).registryKey(blockKey("end_moss"))));
        END_MOSS_CARPET = registerBlock("end_moss_carpet",
                new EndMossCarpetBlock(AbstractBlock.Settings.copy(Blocks.MOSS_CARPET)
                        .strength(0.1f).registryKey(blockKey("end_moss_carpet"))));

        int[] flowerNumbers = {8, 12, 15, 16, 18, 22, 23, 34, 37, 38};
        for (int number : flowerNumbers) {
            String id = String.format("end_flower_%02d", number);
            EndMossFlowerBlock flower = new EndMossFlowerBlock(
                    AbstractBlock.Settings.copy(Blocks.POPPY).registryKey(blockKey(id)));
            registerBlock(id, flower);
            END_FLOWERS.add(flower);
        }

        END_MOSS_SURFACE_FEATURE = Registry.register(
                Registries.FEATURE,
                BacteriumMod.id("end_moss_surface"),
                new EndMossSurfaceFeature(DefaultFeatureConfig.CODEC)
        );

        EndBiomeRegistry.registerHighlandsBiome(END_MOSS_GARDENS, 6);
        EndBiomeRegistry.registerMidlandsBiome(END_MOSS_GARDENS, 6);
        EndBiomeRegistry.registerEdgeBiome(END_MOSS_GARDENS, 4);
        EndBiomeRegistry.registerIslandBiome(END_MOSS_GARDENS, 4);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(END_MOSS);
            entries.add(END_MOSS_CARPET);
            END_FLOWERS.forEach(entries::add);
        });
    }

    public static Block randomFlower(Random random) {
        return END_FLOWERS.get(random.nextInt(END_FLOWERS.size()));
    }

    private static RegistryKey<Block> blockKey(String id) {
        return RegistryKey.of(RegistryKeys.BLOCK, BacteriumMod.id(id));
    }

    private static <T extends Block> T registerBlock(String name, T block) {
        Identifier id = BacteriumMod.id(name);
        Registry.register(Registries.BLOCK, id, block);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
        Registry.register(Registries.ITEM, id,
                new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey()));
        return block;
    }
}
