package com.alex.universalshearsapi.item;

import com.alex.universalshearsapi.api.ShearsActionResult;
import com.alex.universalshearsapi.api.ShearsContext;
import com.alex.universalshearsapi.api.UniversalShearsApi;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UniversalShearsItem extends ShearsItem {
    public UniversalShearsItem(Properties properties) {
        super(properties);
        UniversalShearsApi.registerShears(this);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);
            ShearsActionResult result = UniversalShearsApi.invoke(new ShearsContext(
                    level,
                    pos,
                    state,
                    context.getPlayer(),
                    context.getHand(),
                    context.getItemInHand(),
                    context.getPlayer().isShiftKeyDown()
            ));
            if (result == ShearsActionResult.SUCCESS) {
                return InteractionResult.SUCCESS;
            }
            if (result == ShearsActionResult.FAIL) {
                return InteractionResult.FAIL;
            }
        }
        return super.useOn(context);
    }
}
