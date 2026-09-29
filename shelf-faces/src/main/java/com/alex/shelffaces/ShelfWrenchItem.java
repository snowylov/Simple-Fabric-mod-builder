package com.alex.shelffaces;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public final class ShelfWrenchItem extends Item {
    public ShelfWrenchItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
        if (!(blockEntity instanceof FaceShelfBlockEntity shelf)) {
            return ActionResult.PASS;
        }

        PlayerEntity player = context.getPlayer();
        Direction face = context.getSide();

        if (!world.isClient()) {
            if (player != null && player.isSneaking()) {
                if (!shelf.isFaceOn(face)) {
                    player.sendMessage(Text.translatable("message.shelffaces.overlay_requires_on"), true);
                    return ActionResult.SUCCESS;
                }
                shelf.nextOverlay(face);
                player.sendMessage(Text.translatable("message.shelffaces.overlay_changed", face.asString()), true);
            } else {
                shelf.toggleFace(face);
                if (player != null) {
                    player.sendMessage(
                            Text.translatable(
                                    shelf.isFaceOn(face) ? "message.shelffaces.face_on" : "message.shelffaces.face_off",
                                    face.asString()
                            ),
                            true
                    );
                }
            }
        }

        return ActionResult.SUCCESS;
    }
}
