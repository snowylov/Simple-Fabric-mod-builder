package com.alex.shelffaces;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public final class FaceShelfBlockEntity extends BlockEntity {
    private final Map<Direction, FaceData> faces = new EnumMap<>(Direction.class);

    public FaceShelfBlockEntity(BlockPos pos, BlockState state) {
        super(ShelfFacesMod.SHELF_BLOCK_ENTITY, pos, state);
        for (Direction direction : Direction.values()) {
            faces.put(direction, new FaceData(false, 0));
        }
    }

    public boolean isFaceOn(Direction face) {
        return faces.get(face).on;
    }

    public int overlayCursor(Direction face) {
        return faces.get(face).overlayCursor;
    }

    public void toggleFace(Direction face) {
        FaceData data = faces.get(face);
        data.on = !data.on;
        sync();
    }

    public void nextOverlay(Direction face) {
        FaceData data = faces.get(face);
        data.overlayCursor = data.overlayCursor == Integer.MAX_VALUE ? 0 : data.overlayCursor + 1;
        sync();
    }

    private void sync() {
        markDirty();
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getChunkManager().markForUpdate(pos);
        }
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        for (Direction direction : Direction.values()) {
            String key = direction.asString();
            FaceData data = faces.get(direction);
            data.on = view.getBoolean(key + "_on", false);
            data.overlayCursor = Math.max(0, view.getInt(key + "_overlay", 0));
        }
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        for (Direction direction : Direction.values()) {
            String key = direction.asString();
            FaceData data = faces.get(direction);
            view.putBoolean(key + "_on", data.on);
            view.putInt(key + "_overlay", data.overlayCursor);
        }
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public net.minecraft.nbt.NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }

    private static final class FaceData {
        private boolean on;
        private int overlayCursor;

        private FaceData(boolean on, int overlayCursor) {
            this.on = on;
            this.overlayCursor = overlayCursor;
        }
    }
}
