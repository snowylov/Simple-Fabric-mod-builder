package com.alex.shelffaces.client;

import net.minecraft.client.render.block.entity.state.BlockEntityRenderState;
import net.minecraft.util.math.Direction;

import java.util.EnumMap;
import java.util.Map;

public final class FaceShelfRenderState extends BlockEntityRenderState {
    public String woodId = "oak";
    public final Map<Direction, Boolean> enabled = new EnumMap<>(Direction.class);
    public final Map<Direction, Integer> overlayCursor = new EnumMap<>(Direction.class);

    public FaceShelfRenderState() {
        for (Direction direction : Direction.values()) {
            enabled.put(direction, false);
            overlayCursor.put(direction, 0);
        }
    }
}
