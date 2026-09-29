package com.alex.bacterium;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public final class EndMossFlowerBlock extends PlantBlock {
    public EndMossFlowerBlock(Settings settings) { super(settings); }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(EndMossContent.END_MOSS);
    }
}
