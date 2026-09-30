package com.alex.advanceddimensionapi.api;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AdvancedDimensionApi {
    public static final String API_ID = "advanced-dimension-api";
    private static final Map<Identifier, DimensionVisualDefinition> VISUALS = new LinkedHashMap<>();
    private static final Map<Identifier, IllusionSurfaceDefinition> ILLUSION_SURFACES = new LinkedHashMap<>();

    private AdvancedDimensionApi() {}

    public static void registerDimensionVisuals(Identifier dimension, DimensionVisualDefinition definition) {
        Objects.requireNonNull(dimension, "dimension");
        Objects.requireNonNull(definition, "definition");
        VISUALS.put(dimension, definition);
    }

    public static Optional<DimensionVisualDefinition> visuals(Identifier dimension) {
        return Optional.ofNullable(VISUALS.get(dimension));
    }

    public static void registerIllusionSurface(Identifier id, IllusionSurfaceDefinition definition) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(definition, "definition");
        ILLUSION_SURFACES.put(id, definition);
    }

    public static Optional<IllusionSurfaceDefinition> illusionSurface(Identifier id) {
        return Optional.ofNullable(ILLUSION_SURFACES.get(id));
    }

    public static Map<Identifier, DimensionVisualDefinition> dimensionVisuals() {
        return Collections.unmodifiableMap(VISUALS);
    }

    public record DimensionVisualDefinition(
            Identifier sunTexture,
            Identifier moonTexture,
            List<Identifier> skyboxTextures,
            int skyColor,
            int fogColor,
            float starBrightness,
            boolean vanillaClouds
    ) {
        public DimensionVisualDefinition {
            skyboxTextures = skyboxTextures == null ? List.of() : List.copyOf(skyboxTextures);
            if (starBrightness < 0) throw new IllegalArgumentException("starBrightness must be >= 0");
        }
    }

    public record IllusionSurfaceDefinition(
            Identifier texture,
            Identifier viewedScene,
            float parallaxDepth,
            boolean animated,
            boolean renderEntities
    ) {
        public IllusionSurfaceDefinition {
            Objects.requireNonNull(texture, "texture");
            if (parallaxDepth < 0) throw new IllegalArgumentException("parallaxDepth must be >= 0");
        }
    }
}
