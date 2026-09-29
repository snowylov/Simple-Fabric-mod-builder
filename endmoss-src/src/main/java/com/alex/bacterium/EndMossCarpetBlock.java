package com.alex.bacterium;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public final class EndMossCarpetBlock extends CarpetBlock implements Fertilizable {
    public EndMossCarpetBlock(Settings settings) { super(settings); }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) { return true; }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) { return true; }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        for (int attempt = 0; attempt < 64; attempt++) {
            BlockPos support = pos.down().add(random.nextBetween(-4, 4), random.nextBetween(-1, 1), random.nextBetween(-4, 4));
            BlockState supportState = world.getBlockState(support);
            BlockPos above = support.up();
            if (!world.getBlockState(above).isAir()) continue;

            if (EndMossBlock.canBecomeEndMoss(supportState)) {
                world.setBlockState(support, EndMossContent.END_MOSS.getDefaultState(), Block.NOTIFY_ALL);
                supportState = EndMossContent.END_MOSS.getDefaultState();
            }

            if (supportState.isOf(EndMossContent.END_MOSS)) {
                if (random.nextFloat() < 0.28f) {
                    world.setBlockState(above, EndMossContent.randomFlower(random).getDefaultState(), Block.NOTIFY_ALL);
                } else {
                    world.setBlockState(above, EndMossContent.END_MOSS_CARPET.getDefaultState(), Block.NOTIFY_ALL);
                }
            }
        }
    }
}
