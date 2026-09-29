package com.alex.universalcrowbarapi.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;

public final class FourWayRailBlock extends RailBlock {
    public FourWayRailBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void entityInside(
            BlockState state,
            Level level,
            BlockPos pos,
            Entity entity,
            InsideBlockEffectApplier effectApplier,
            boolean pastEdges
    ) {
        if (!level.isClientSide() && entity instanceof AbstractMinecart minecart) {
            Vec3 movement = minecart.getDeltaMovement();
            if (movement.horizontalDistanceSqr() > 1.0E-6) {
                RailShape desired = Math.abs(movement.x) >= Math.abs(movement.z)
                        ? RailShape.EAST_WEST
                        : RailShape.NORTH_SOUTH;
                if (state.getValue(SHAPE) != desired) {
                    level.setBlock(pos, state.setValue(SHAPE, desired), 2);
                }
            }
        }
        super.entityInside(state, level, pos, entity, effectApplier, pastEdges);
    }
}
