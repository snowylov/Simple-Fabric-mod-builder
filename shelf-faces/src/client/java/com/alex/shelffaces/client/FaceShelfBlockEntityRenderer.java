package com.alex.shelffaces.client;

import com.alex.shelffaces.FaceShelfBlock;
import com.alex.shelffaces.FaceShelfBlockEntity;
import com.alex.shelffaces.ShelfFacesMod;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public final class FaceShelfBlockEntityRenderer implements BlockEntityRenderer<FaceShelfBlockEntity, FaceShelfRenderState> {
    private static final float EPSILON = 0.0015f;

    public FaceShelfBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public FaceShelfRenderState createRenderState() {
        return new FaceShelfRenderState();
    }

    @Override
    public void updateRenderState(
            FaceShelfBlockEntity blockEntity,
            FaceShelfRenderState state,
            float tickProgress,
            Vec3d cameraPos,
            @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlay
    ) {
        BlockEntityRenderer.super.updateRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        if (blockEntity.getCachedState().getBlock() instanceof FaceShelfBlock block) {
            state.woodId = block.woodId();
        }
        for (Direction direction : Direction.values()) {
            state.enabled.put(direction, blockEntity.isFaceOn(direction));
            state.overlayCursor.put(direction, blockEntity.overlayCursor(direction));
        }
    }

    @Override
    public void render(FaceShelfRenderState state, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState) {
        Identifier onTexture = ShelfFacesMod.id("textures/block/" + state.woodId + "_shelf_face_toggled_on.png");

        for (Direction direction : Direction.values()) {
            if (!Boolean.TRUE.equals(state.enabled.get(direction))) {
                continue;
            }

            queue.submitCustom(matrices, RenderLayers.entityCutout(onTexture), (entry, vertices) ->
                    emitFace(entry, vertices, direction, EPSILON, state.lightmapCoordinates)
            );

            Identifier overlay = OverlayRegistry.forCursor(state.overlayCursor.get(direction));
            if (overlay != null) {
                queue.submitCustom(matrices, RenderLayers.entityCutout(overlay), (entry, vertices) ->
                        emitFace(entry, vertices, direction, EPSILON * 2.0f, state.lightmapCoordinates)
                );
            }
        }
    }

    private static void emitFace(MatrixStack.Entry entry, VertexConsumer vertices, Direction direction, float offset, int light) {
        float min = -offset;
        float max = 1.0f + offset;
        int color = 0xFFFFFFFF;
        int overlay = 0;
        float nx = direction.getOffsetX();
        float ny = direction.getOffsetY();
        float nz = direction.getOffsetZ();

        switch (direction) {
            case NORTH -> quad(entry, vertices,
                    min, min, min, 0, 1,
                    min, max, min, 0, 0,
                    max, max, min, 1, 0,
                    max, min, min, 1, 1,
                    color, overlay, light, nx, ny, nz);
            case SOUTH -> quad(entry, vertices,
                    max, min, max, 0, 1,
                    max, max, max, 0, 0,
                    min, max, max, 1, 0,
                    min, min, max, 1, 1,
                    color, overlay, light, nx, ny, nz);
            case WEST -> quad(entry, vertices,
                    min, min, max, 0, 1,
                    min, max, max, 0, 0,
                    min, max, min, 1, 0,
                    min, min, min, 1, 1,
                    color, overlay, light, nx, ny, nz);
            case EAST -> quad(entry, vertices,
                    max, min, min, 0, 1,
                    max, max, min, 0, 0,
                    max, max, max, 1, 0,
                    max, min, max, 1, 1,
                    color, overlay, light, nx, ny, nz);
            case UP -> quad(entry, vertices,
                    min, max, min, 0, 0,
                    min, max, max, 0, 1,
                    max, max, max, 1, 1,
                    max, max, min, 1, 0,
                    color, overlay, light, nx, ny, nz);
            case DOWN -> quad(entry, vertices,
                    min, min, max, 0, 0,
                    min, min, min, 0, 1,
                    max, min, min, 1, 1,
                    max, min, max, 1, 0,
                    color, overlay, light, nx, ny, nz);
        }
    }

    private static void quad(
            MatrixStack.Entry entry,
            VertexConsumer vertices,
            float x1, float y1, float z1, float u1, float v1,
            float x2, float y2, float z2, float u2, float v2,
            float x3, float y3, float z3, float u3, float v3,
            float x4, float y4, float z4, float u4, float v4,
            int color, int overlay, int light, float nx, float ny, float nz
    ) {
        vertex(entry, vertices, x1, y1, z1, color, u1, v1, overlay, light, nx, ny, nz);
        vertex(entry, vertices, x2, y2, z2, color, u2, v2, overlay, light, nx, ny, nz);
        vertex(entry, vertices, x3, y3, z3, color, u3, v3, overlay, light, nx, ny, nz);
        vertex(entry, vertices, x4, y4, z4, color, u4, v4, overlay, light, nx, ny, nz);
    }

    private static void vertex(
            MatrixStack.Entry entry,
            VertexConsumer vertices,
            float x, float y, float z,
            int color, float u, float v,
            int overlay, int light,
            float nx, float ny, float nz
    ) {
        vertices.vertex(entry, x, y, z)
                .color(color)
                .texture(u, v)
                .overlay(overlay)
                .light(light)
                .normal(nx, ny, nz);
    }
}
