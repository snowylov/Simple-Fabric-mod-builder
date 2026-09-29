package com.alex.universaltoolsapi.screwdriver.item;

import com.alex.universaltoolsapi.screwdriver.api.ScrewdriverActionResult;
import com.alex.universaltoolsapi.screwdriver.api.ScrewdriverApi;
import com.alex.universaltoolsapi.screwdriver.api.ScrewdriverContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Properties properties) {
        super(properties);
        ScrewdriverApi.registerScrewdriver(this);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (context.getPlayer() != null) {
            ScrewdriverActionResult apiResult = ScrewdriverApi.invoke(new ScrewdriverContext(
                    level,
                    pos,
                    state,
                    context.getPlayer(),
                    context.getHand(),
                    context.getItemInHand(),
                    context.getPlayer().isShiftKeyDown()
            ));
            if (apiResult == ScrewdriverActionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }
            if (apiResult == ScrewdriverActionResult.FAIL) {
                return InteractionResult.FAIL;
            }
        }

        Rotation rotation = context.getPlayer() != null && context.getPlayer().isShiftKeyDown()
                ? Rotation.COUNTERCLOCKWISE_90
                : Rotation.CLOCKWISE_90;
        BlockState rotated = state.rotate(rotation);
        if (rotated.equals(state)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, rotated);
        }
        return InteractionResult.SUCCESS;
    }
}
