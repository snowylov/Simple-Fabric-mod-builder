package com.alex.universalchestapi.api;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class UniversalChestApi {
    public static final String API_ID = "universal-chest-api";
    private static final Map<Identifier, ChestDefinition> DEFINITIONS = new LinkedHashMap<>();

    private UniversalChestApi() {}

    public static void register(Identifier id, ChestDefinition definition) {
        DEFINITIONS.put(Objects.requireNonNull(id), Objects.requireNonNull(definition));
    }

    public static Optional<ChestDefinition> get(Identifier id) {
        return Optional.ofNullable(DEFINITIONS.get(id));
    }

    public static Map<Identifier, ChestDefinition> definitions() {
        return Collections.unmodifiableMap(DEFINITIONS);
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
