package com.alex.bacterium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public final class EndMossBlock extends Block implements Fertilizable {
    public EndMossBlock(Settings settings) { super(settings); }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) { return true; }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) { return true; }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        for (int attempt = 0; attempt < 80; attempt++) {
            BlockPos target = pos.add(random.nextBetween(-4, 4), random.nextBetween(-1, 1), random.nextBetween(-4, 4));
            BlockState targetState = world.getBlockState(target);
            if (canBecomeEndMoss(targetState)) {
                world.setBlockState(target, EndMossContent.END_MOSS.getDefaultState(), Block.NOTIFY_ALL);
                maybeDecorate(world, random, target.up());
            } else if (targetState.isOf(EndMossContent.END_MOSS)) {
                maybeDecorate(world, random, target.up());
            }
        }
    }

    static boolean canBecomeEndMoss(BlockState state) {
        return state.isOf(Blocks.END_STONE)
                || state.isIn(BlockTags.BASE_STONE_OVERWORLD)
                || state.isIn(BlockTags.DIRT);
    }

    static void maybeDecorate(ServerWorld world, Random random, BlockPos pos) {
        if (!world.getBlockState(pos).isAir()) return;
        float roll = random.nextFloat();
        if (roll < 0.10f) {
            world.setBlockState(pos, EndMossContent.randomFlower(random).getDefaultState(), Block.NOTIFY_ALL);
        } else if (roll < 0.28f) {
            world.setBlockState(pos, EndMossContent.END_MOSS_CARPET.getDefaultState(), Block.NOTIFY_ALL);
        }
    }
}
