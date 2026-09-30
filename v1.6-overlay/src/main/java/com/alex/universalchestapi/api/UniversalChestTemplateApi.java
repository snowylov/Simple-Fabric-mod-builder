package com.alex.universalchestapi.api;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class UniversalChestTemplateApi {
    public static final String API_ID = "universal-chest-api";

    private static final Map<Identifier, ChestMaterial> MATERIALS = new LinkedHashMap<>();
    private static final Map<Identifier, LockMaterial> LOCKS = new LinkedHashMap<>();

    private UniversalChestTemplateApi() {}

    public static void bootstrapDefaults() {
        if (!MATERIALS.isEmpty()) return;

        for (String wood : List.of(
                "acacia", "bamboo", "beech", "birch", "cherry", "crimson", "dark_oak",
                "jungle", "mangrove", "oak", "pale_oak", "spruce", "warped"
        )) {
            registerMaterial(Identifier.fromNamespaceAndPath("minecraft", wood),
                    new ChestMaterial(Identifier.fromNamespaceAndPath("minecraft", "block/" + wood + "_planks"), MaterialKind.WOOD));
        }

        for (String stone : List.of("stone", "cobblestone", "deepslate", "blackstone")) {
            registerMaterial(Identifier.fromNamespaceAndPath("minecraft", stone),
                    new ChestMaterial(Identifier.fromNamespaceAndPath("minecraft", "block/" + stone), MaterialKind.STONE));
        }

        registerMaterial(Identifier.fromNamespaceAndPath("minecraft", "prismarine"),
                new ChestMaterial(Identifier.fromNamespaceAndPath("minecraft", "block/prismarine"), MaterialKind.SPECIAL));
        registerMaterial(Identifier.fromNamespaceAndPath("minecraft", "purpur"),
                new ChestMaterial(Identifier.fromNamespaceAndPath("minecraft", "block/purpur_block"), MaterialKind.SPECIAL));

        for (String lock : List.of("iron", "gold", "copper", "cobblestone", "stone", "diamond", "netherite", "amethyst")) {
            String texturePath = switch (lock) {
                case "gold" -> "block/gold_block";
                case "copper" -> "block/copper_block";
                case "diamond" -> "block/diamond_block";
                case "netherite" -> "block/netherite_block";
                case "amethyst" -> "block/amethyst_block";
                case "cobblestone" -> "block/cobblestone";
                case "stone" -> "block/stone";
                default -> "block/iron_block";
            };
            registerLock(Identifier.fromNamespaceAndPath("minecraft", lock),
                    new LockMaterial(Identifier.fromNamespaceAndPath("minecraft", texturePath)));
        }
    }

    public static void registerMaterial(Identifier id, ChestMaterial material) {
        MATERIALS.put(Objects.requireNonNull(id), Objects.requireNonNull(material));
    }

    public static void registerLock(Identifier id, LockMaterial material) {
        LOCKS.put(Objects.requireNonNull(id), Objects.requireNonNull(material));
    }

    public static Optional<ChestMaterial> material(Identifier id) {
        return Optional.ofNullable(MATERIALS.get(id));
    }

    public static Optional<LockMaterial> lock(Identifier id) {
        return Optional.ofNullable(LOCKS.get(id));
    }

    public static Map<Identifier, ChestMaterial> materials() {
        return Collections.unmodifiableMap(MATERIALS);
    }

    public static Map<Identifier, LockMaterial> locks() {
        return Collections.unmodifiableMap(LOCKS);
    }

    public static Identifier modelForState(ChestState state) {
        return switch (state) {
            case CLOSED -> Identifier.fromNamespaceAndPath("universal-chest-api", "template/universal_chest_closed");
            case PARTIALLY_OPEN -> Identifier.fromNamespaceAndPath("universal-chest-api", "template/universal_chest_partially_open");
            case OPEN -> Identifier.fromNamespaceAndPath("universal-chest-api", "template/universal_chest_open");
        };
    }

    public enum ChestState {
        CLOSED,
        PARTIALLY_OPEN,
        OPEN
    }

    public enum MaterialKind {
        WOOD,
        STONE,
        SPECIAL
    }

    public record ChestMaterial(Identifier bodyTexture, MaterialKind kind) {
        public ChestMaterial {
            Objects.requireNonNull(bodyTexture, "bodyTexture");
            Objects.requireNonNull(kind, "kind");
        }
    }

    public record LockMaterial(Identifier texture) {
        public LockMaterial {
            Objects.requireNonNull(texture, "texture");
        }
    }

    public record ChestTemplateDefinition(
            Identifier material,
            Identifier lock,
            int rows,
            Identifier firGui,
            boolean animateWhenViewed
    ) {
        public ChestTemplateDefinition {
            if (rows < 1 || rows > 6) throw new IllegalArgumentException("rows must be 1..6");
        }
    }
}
