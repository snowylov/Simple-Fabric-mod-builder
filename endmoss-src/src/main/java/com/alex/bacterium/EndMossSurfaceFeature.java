package com.alex.bacterium;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public final class EndMossSurfaceFeature extends Feature<DefaultFeatureConfig> {
    public EndMossSurfaceFeature(Codec<DefaultFeatureConfig> codec) { super(codec); }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        ChunkPos chunk = new ChunkPos(context.getOrigin());
        boolean changed = false;

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int x = chunk.getStartX() + localX;
                int z = chunk.getStartZ() + localZ;
                int y = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, x, z) - 1;
                BlockPos top = new BlockPos(x, y, z);
                RegistryEntry<Biome> biome = world.getBiome(top);
                if (!biome.matchesKey(EndMossContent.END_MOSS_GARDENS)) continue;

                BlockState topState = world.getBlockState(top);
                if (topState.isOf(Blocks.END_STONE)) {
                    world.setBlockState(top, EndMossContent.END_MOSS.getDefaultState(), Block.NOTIFY_ALL);
                    changed = true;
                }

                BlockPos above = top.up();
                if (world.getBlockState(above).isAir() && world.getBlockState(top).isOf(EndMossContent.END_MOSS)) {
                    float roll = random.nextFloat();
                    if (roll < 0.065f) {
                        world.setBlockState(above, EndMossContent.randomFlower(random).getDefaultState(), Block.NOTIFY_ALL);
                    } else if (roll < 0.18f) {
                        world.setBlockState(above, EndMossContent.END_MOSS_CARPET.getDefaultState(), Block.NOTIFY_ALL);
                    }
                }
            }
        }
        return changed;
    }
}
