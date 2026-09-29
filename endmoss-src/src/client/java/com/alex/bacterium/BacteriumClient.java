package com.alex.bacterium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.render.chunk.ChunkSectionLayer;

public final class BacteriumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(EndMossContent.END_MOSS_CARPET, ChunkSectionLayer.CUTOUT);
        for (EndMossFlowerBlock flower : EndMossContent.END_FLOWERS) {
            BlockRenderLayerMap.putBlock(flower, ChunkSectionLayer.CUTOUT);
        }
    }
}
