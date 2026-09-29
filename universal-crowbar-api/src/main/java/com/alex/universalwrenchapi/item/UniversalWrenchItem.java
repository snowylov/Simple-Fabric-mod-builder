package com.alex.universalwrenchapi.item;

import com.alex.universalwrenchapi.api.WrenchActionResult;
import com.alex.universalwrenchapi.api.WrenchApi;
import com.alex.universalwrenchapi.api.WrenchContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public final class UniversalWrenchItem extends Item {
    public UniversalWrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (context.getPlayer() != null) {
            WrenchActionResult apiResult = WrenchApi.invoke(new WrenchContext(
                    level,
                    pos,
                    state,
                    context.getPlayer(),
                    context.getHand(),
                    context.getItemInHand(),
                    context.getPlayer().isShiftKeyDown()
            ));

            if (apiResult == WrenchActionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }
            if (apiResult == WrenchActionResult.FAIL) {
                return InteractionResult.FAIL;
            }
        }

        Rotation rotation = context.getPlayer() != null && context.getPlayer().isShiftKeyDown()
                ? Rotation.COUNTERCLOCKWISE_90
                : Rotation.CLOCKWISE_90;
        BlockState rotated = state.rotate(rotation);
        if (rotated == state || rotated.equals(state)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, rotated);
        }
        return InteractionResult.SUCCESS;
    }
}
