package com.alex.universaltagapi.api;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class UniversalTagApi {
    public static final String API_ID = "universal-tag-api";
    private static final Map<TagKey, TagPatch> PATCHES = new LinkedHashMap<>();

    private UniversalTagApi() {}

    public static void create(TagType type, Identifier tag) {
        PATCHES.computeIfAbsent(new TagKey(type, tag), ignored -> new TagPatch());
    }

    public static void add(TagType type, Identifier tag, Identifier value) {
        TagPatch patch = PATCHES.computeIfAbsent(new TagKey(type, tag), ignored -> new TagPatch());
        patch.removed.remove(value);
        patch.added.add(value);
    }

    public static void remove(TagType type, Identifier tag, Identifier value) {
        TagPatch patch = PATCHES.computeIfAbsent(new TagKey(type, tag), ignored -> new TagPatch());
        patch.added.remove(value);
        patch.removed.add(value);
    }

    public static boolean containsOverlay(TagType type, Identifier tag, Identifier value, boolean vanillaContains) {
        TagPatch patch = PATCHES.get(new TagKey(type, tag));
        if (patch == null) return vanillaContains;
        if (patch.removed.contains(value)) return false;
        if (patch.added.contains(value)) return true;
        return vanillaContains;
    }

    public static Map<TagKey, TagPatchView> patches() {
        Map<TagKey, TagPatchView> copy = new LinkedHashMap<>();
        PATCHES.forEach((key, value) -> copy.put(key, new TagPatchView(Set.copyOf(value.added), Set.copyOf(value.removed))));
        return Collections.unmodifiableMap(copy);
    }

    public enum TagType {
        ITEM,
        BLOCK,
        FLUID,
        ENTITY_TYPE,
        BIOME,
        OTHER
    }

    public record TagKey(TagType type, Identifier id) {}

    public record TagPatchView(Set<Identifier> added, Set<Identifier> removed) {}

    private static final class TagPatch {
        private final Set<Identifier> added = new LinkedHashSet<>();
        private final Set<Identifier> removed = new LinkedHashSet<>();
    }
}
