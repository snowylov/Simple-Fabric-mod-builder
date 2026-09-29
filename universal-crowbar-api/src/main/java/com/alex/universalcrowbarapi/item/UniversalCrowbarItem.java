package com.alex.universalcrowbarapi.item;
import com.alex.universalcrowbarapi.api.CrowbarActionResult;
import com.alex.universalcrowbarapi.api.CrowbarApi;
import com.alex.universalcrowbarapi.api.CrowbarContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
public final class UniversalCrowbarItem extends Item {
    public UniversalCrowbarItem(Properties properties) { super(properties); }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (context.getPlayer() != null) {
            CrowbarActionResult result = CrowbarApi.invoke(new CrowbarContext(
                level, pos, state, context.getPlayer(), context.getHand(),
                context.getItemInHand(), context.getPlayer().isShiftKeyDown()));
            if (result == CrowbarActionResult.SUCCESS) return InteractionResult.SUCCESS;
            if (result == CrowbarActionResult.FAIL) return InteractionResult.FAIL;
        }
        Rotation rotation = context.getPlayer() != null && context.getPlayer().isShiftKeyDown()
            ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
        BlockState rotated = state.rotate(rotation);
        if (rotated == state || rotated.equals(state)) return InteractionResult.PASS;
        if (!level.isClientSide()) level.setBlockAndUpdate(pos, rotated);
        return InteractionResult.SUCCESS;
    }
}
