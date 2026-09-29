package com.alex.shelffaces.client;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class OverlayRegistry {
    private static volatile List<Identifier> overlays = List.of();

    private OverlayRegistry() {
    }

    public static void reload(ResourceManager manager) {
        List<Identifier> found = new ArrayList<>();
        manager.findResources("textures/overlays", id -> id.getPath().endsWith(".png")).keySet().forEach(id -> {
            String path = id.getPath();
            String texturePath = path.substring("textures/".length());
            found.add(Identifier.of(id.getNamespace(), "textures/" + texturePath));
        });
        found.sort(Comparator.comparing(Identifier::toString));
        overlays = List.copyOf(found);
    }

    public static Identifier forCursor(int cursor) {
        List<Identifier> current = overlays;
        if (current.isEmpty()) {
            return null;
        }
        return current.get(Math.floorMod(cursor, current.size()));
    }

    public static int size() {
        return overlays.size();
    }
}
