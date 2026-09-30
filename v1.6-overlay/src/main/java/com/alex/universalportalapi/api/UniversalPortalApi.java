package com.alex.universalportalapi.api;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class UniversalPortalApi {
    public static final String API_ID = "universal-portal-api";
    private static final Map<Identifier, PortalDefinition> PORTALS = new LinkedHashMap<>();
    private static final Map<Identifier, List<PortalPredicate>> PREDICATES = new LinkedHashMap<>();

    private UniversalPortalApi() {}

    public static void register(Identifier id, PortalDefinition definition) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(definition, "definition");
        if (PORTALS.putIfAbsent(id, definition) != null) {
            throw new IllegalStateException("Portal already registered: " + id);
        }
    }

    public static void addPredicate(Identifier portalId, PortalPredicate predicate) {
        PREDICATES.computeIfAbsent(portalId, ignored -> new ArrayList<>()).add(Objects.requireNonNull(predicate));
    }

    public static boolean canEnter(Identifier portalId, PortalContext context) {
        PortalDefinition definition = PORTALS.get(portalId);
        if (definition == null) return false;
        if (!context.visitedDimensions().containsAll(definition.requiredVisitedDimensions())) return false;
        for (PortalPredicate predicate : PREDICATES.getOrDefault(portalId, List.of())) {
            if (!predicate.test(context)) return false;
        }
        return true;
    }

    public static Optional<PortalDefinition> get(Identifier id) {
        return Optional.ofNullable(PORTALS.get(id));
    }

    public static Map<Identifier, PortalDefinition> portals() {
        return Collections.unmodifiableMap(PORTALS);
    }

    public enum PortalKind {
        NETHER_STYLE,
        TWILIGHT_FOREST_STYLE,
        END_STYLE,
        SQUARE
    }

    public enum Orientation {
        VERTICAL,
        FLAT,
        EITHER
    }

    public record PortalShape(
            int minWidth,
            int minHeight,
            int maxWidth,
            int maxHeight,
            int thickness,
            Orientation orientation
    ) {
        public PortalShape {
            if (minWidth < 1 || minHeight < 1 || maxWidth < minWidth || maxHeight < minHeight || thickness < 1) {
                throw new IllegalArgumentException("Invalid portal shape bounds");
            }
            Objects.requireNonNull(orientation, "orientation");
        }
    }

    public record PortalTexture(
            Identifier sourceTexture,
            boolean grayscaleSource,
            float red,
            float green,
            float blue
    ) {
        public PortalTexture {
            Objects.requireNonNull(sourceTexture, "sourceTexture");
            red = clamp(red);
            green = clamp(green);
            blue = clamp(blue);
        }

        private static float clamp(float value) {
            return Math.max(0.0F, Math.min(1.0F, value));
        }
    }

    public record PortalDefinition(
            Identifier destinationDimension,
            PortalKind kind,
            PortalShape shape,
            PortalTexture texture,
            Identifier portalSound,
            Set<Identifier> requiredVisitedDimensions,
            int warmupTicks
    ) {
        public PortalDefinition {
            Objects.requireNonNull(destinationDimension, "destinationDimension");
            Objects.requireNonNull(kind, "kind");
            Objects.requireNonNull(shape, "shape");
            Objects.requireNonNull(texture, "texture");
            requiredVisitedDimensions = requiredVisitedDimensions == null ? Set.of() : Set.copyOf(requiredVisitedDimensions);
            if (warmupTicks < 0) throw new IllegalArgumentException("warmupTicks must be >= 0");
        }
    }

    public record PortalContext(
            Level level,
            Entity entity,
            Set<Identifier> visitedDimensions
    ) {
        public PortalContext {
            visitedDimensions = visitedDimensions == null ? Set.of() : Set.copyOf(visitedDimensions);
        }
    }

    @FunctionalInterface
    public interface PortalPredicate {
        boolean test(PortalContext context);
    }
}
