package com.alex.universalchestapi.api;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class UniversalChestApi {
    public static final String API_ID = "universal-chest-api";

    private static final Map<Identifier, ChestDefinition> STYLE_DEFINITIONS = new LinkedHashMap<>();
    private static final Map<String, UniversalContainerDefinition> CONTAINER_DEFINITIONS = new LinkedHashMap<>();

    private UniversalChestApi() {}

    /**
     * v1.6 per-instance chest style registration.
     */
    public static void register(Identifier id, ChestDefinition definition) {
        STYLE_DEFINITIONS.put(Objects.requireNonNull(id), Objects.requireNonNull(definition));
    }

    /**
     * Compatibility with the v1.3-v1.5 Universal Chest API data loader.
     */
    public static void register(UniversalContainerDefinition definition) {
        Objects.requireNonNull(definition, "definition");
        CONTAINER_DEFINITIONS.put(definition.id(), definition);
    }

    public static UniversalContainerDefinition chest(String id, int slots) {
        UniversalContainerDefinition definition =
                new UniversalContainerDefinition(id, ContainerKind.CHEST, slots, 1, true, true);
        register(definition);
        return definition;
    }

    public static UniversalContainerDefinition barrel(String id, int slots) {
        UniversalContainerDefinition definition =
                new UniversalContainerDefinition(id, ContainerKind.BARREL, slots, 1, true, true);
        register(definition);
        return definition;
    }

    public static UniversalContainerDefinition crate(String id, int slots) {
        UniversalContainerDefinition definition =
                new UniversalContainerDefinition(id, ContainerKind.CRATE, slots, 16, true, true);
        register(definition);
        return definition;
    }

    public static Optional<ChestDefinition> get(Identifier id) {
        return Optional.ofNullable(STYLE_DEFINITIONS.get(id));
    }

    public static Map<Identifier, ChestDefinition> definitions() {
        return Collections.unmodifiableMap(STYLE_DEFINITIONS);
    }

    public static Map<String, UniversalContainerDefinition> containerDefinitions() {
        return Collections.unmodifiableMap(CONTAINER_DEFINITIONS);
    }

    public static ChestDefinition singleTexture(Identifier lock, Identifier base) {
        return new ChestDefinition(lock, base, base, 3, true, true);
    }

    public static ChestDefinition splitTexture(Identifier lock, Identifier top, Identifier bottom) {
        return new ChestDefinition(lock, top, bottom, 3, true, true);
    }

    public record ChestDefinition(
            Identifier lockTexture,
            Identifier topTexture,
            Identifier bottomTexture,
            int rows,
            boolean animateLid,
            boolean useFirGui
    ) {
        public ChestDefinition {
            Objects.requireNonNull(lockTexture, "lockTexture");
            Objects.requireNonNull(topTexture, "topTexture");
            Objects.requireNonNull(bottomTexture, "bottomTexture");
            if (rows < 1 || rows > 6) throw new IllegalArgumentException("rows must be 1..6");
        }
    }
}
