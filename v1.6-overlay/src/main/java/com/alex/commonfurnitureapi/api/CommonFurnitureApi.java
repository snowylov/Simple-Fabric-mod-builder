package com.alex.commonfurnitureapi.api;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CommonFurnitureApi {
    public static final String API_ID = "common-furniture-api";
    private static final Map<Identifier, FurnitureDefinition> DEFINITIONS = new LinkedHashMap<>();

    private CommonFurnitureApi() {}

    public static void register(Identifier id, FurnitureDefinition definition) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(definition, "definition");
        DEFINITIONS.put(id, definition);
    }

    public static Optional<FurnitureDefinition> get(Identifier id) {
        return Optional.ofNullable(DEFINITIONS.get(id));
    }

    public static Map<Identifier, FurnitureDefinition> definitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
    }

    public static FurnitureDefinition table(Identifier leg, Identifier top, Identifier cover, int variant) {
        return new FurnitureDefinition(FurnitureKind.TABLE, leg, top, cover, clampVariant(variant), true, 4, false, false);
    }

    public static FurnitureDefinition chair(Identifier leg, Identifier body, Identifier cover, int variant) {
        return new FurnitureDefinition(FurnitureKind.CHAIR, leg, body, cover, clampVariant(variant), true, 4, true, true);
    }

    public static FurnitureDefinition bench(Identifier leg, Identifier body, Identifier cover, int variant) {
        return new FurnitureDefinition(FurnitureKind.BENCH, leg, body, cover, clampVariant(variant), true, 4, true, false);
    }

    private static int clampVariant(int variant) {
        return Math.max(0, Math.min(4, variant));
    }

    public enum FurnitureKind { TABLE, CHAIR, BENCH }

    public record FurnitureDefinition(
            FurnitureKind kind,
            Identifier legTexture,
            Identifier topOrBodyTexture,
            Identifier coverTexture,
            int modelVariant,
            boolean connectable,
            int maxConnectAxis,
            boolean hasBack,
            boolean hasArmRests
    ) {
        public FurnitureDefinition {
            Objects.requireNonNull(kind, "kind");
            Objects.requireNonNull(legTexture, "legTexture");
            Objects.requireNonNull(topOrBodyTexture, "topOrBodyTexture");
            coverTexture = coverTexture == null ? Identifier.fromNamespaceAndPath("minecraft", "block/white_wool") : coverTexture;
            modelVariant = clampVariant(modelVariant);
            maxConnectAxis = Math.max(1, Math.min(4, maxConnectAxis));
        }

        public boolean allowsFootprint(int width, int depth) {
            return width >= 1 && depth >= 1 && width <= maxConnectAxis && depth <= maxConnectAxis;
        }
    }
}
