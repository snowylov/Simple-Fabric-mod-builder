package com.alex.shelffaces.client;

import com.alex.shelffaces.ShelfFacesMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public final class ShelfFacesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(ShelfFacesMod.SHELF_BLOCK_ENTITY, FaceShelfBlockEntityRenderer::new);

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return ShelfFacesMod.id("overlays");
            }

            @Override
            public void reload(ResourceManager manager) {
                OverlayRegistry.reload(manager);
            }
        });
    }
}
