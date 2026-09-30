package com.alex.universalexpansionapi.data;

import com.alex.advanceddimensionapi.api.AdvancedDimensionApi;
import com.alex.projectileweaponapi.api.ProjectileWeaponApi;
import com.alex.universalchestapi.api.UniversalChestTemplateApi;
import com.alex.universalportalapi.api.UniversalPortalApi;
import com.alex.universaltagapi.api.UniversalTagApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class DataDrivenApiJson {
    private DataDrivenApiJson() {}

    public static AdvancedDimensionApi.DimensionVisualDefinition parseDimensionVisual(JsonObject json) {
        Identifier sun = optionalId(json, "sun_texture");
        Identifier moon = optionalId(json, "moon_texture");
        List<Identifier> skybox = ids(json.getAsJsonArray("skybox_textures"));
        int skyColor = parseColor(json, "sky_color", 0x77ADFF);
        int fogColor = parseColor(json, "fog_color", 0xC0D8FF);
        float starBrightness = json.has("star_brightness") ? json.get("star_brightness").getAsFloat() : 1.0F;
        boolean vanillaClouds = !json.has("vanilla_clouds") || json.get("vanilla_clouds").getAsBoolean();
        return new AdvancedDimensionApi.DimensionVisualDefinition(sun, moon, skybox, skyColor, fogColor, starBrightness, vanillaClouds);
    }

    public static AdvancedDimensionApi.IllusionSurfaceDefinition parseIllusionSurface(JsonObject json) {
        return new AdvancedDimensionApi.IllusionSurfaceDefinition(
                Identifier.parse(json.get("texture").getAsString()),
                optionalId(json, "viewed_scene"),
                json.has("parallax_depth") ? json.get("parallax_depth").getAsFloat() : 1.0F,
                json.has("animated") && json.get("animated").getAsBoolean(),
                json.has("render_entities") && json.get("render_entities").getAsBoolean()
        );
    }

    public static UniversalPortalApi.PortalDefinition parsePortal(JsonObject json) {
        UniversalPortalApi.PortalKind kind = UniversalPortalApi.PortalKind.valueOf(
                json.get("kind").getAsString().toUpperCase()
        );
        JsonObject shape = json.getAsJsonObject("shape");
        UniversalPortalApi.Orientation orientation = UniversalPortalApi.Orientation.valueOf(
                shape.get("orientation").getAsString().toUpperCase()
        );
        UniversalPortalApi.PortalShape portalShape = new UniversalPortalApi.PortalShape(
                shape.get("min_width").getAsInt(),
                shape.get("min_height").getAsInt(),
                shape.get("max_width").getAsInt(),
                shape.get("max_height").getAsInt(),
                shape.has("thickness") ? shape.get("thickness").getAsInt() : 1,
                orientation
        );

        JsonObject texture = json.getAsJsonObject("texture");
        UniversalPortalApi.PortalTexture portalTexture = new UniversalPortalApi.PortalTexture(
                Identifier.parse(texture.get("source").getAsString()),
                !texture.has("grayscale") || texture.get("grayscale").getAsBoolean(),
                texture.has("red") ? texture.get("red").getAsFloat() : 1.0F,
                texture.has("green") ? texture.get("green").getAsFloat() : 1.0F,
                texture.has("blue") ? texture.get("blue").getAsFloat() : 1.0F
        );

        Set<Identifier> required = new HashSet<>(ids(json.getAsJsonArray("required_visited_dimensions")));
        return new UniversalPortalApi.PortalDefinition(
                Identifier.parse(json.get("destination_dimension").getAsString()),
                kind,
                portalShape,
                portalTexture,
                optionalId(json, "portal_sound"),
                required,
                json.has("warmup_ticks") ? json.get("warmup_ticks").getAsInt() : 0
        );
    }

    public static void applyTagPatch(JsonObject json) {
        UniversalTagApi.TagType type = UniversalTagApi.TagType.valueOf(json.get("type").getAsString().toUpperCase());
        Identifier tag = Identifier.parse(json.get("tag").getAsString());
        UniversalTagApi.create(type, tag);
        for (Identifier id : ids(json.getAsJsonArray("add"))) UniversalTagApi.add(type, tag, id);
        for (Identifier id : ids(json.getAsJsonArray("remove"))) UniversalTagApi.remove(type, tag, id);
    }

    public static void applyChestMaterial(JsonObject json) {
        Identifier id = Identifier.parse(json.get("id").getAsString());
        Identifier texture = Identifier.parse(json.get("texture").getAsString());
        UniversalChestTemplateApi.MaterialKind kind = UniversalChestTemplateApi.MaterialKind.valueOf(
                json.get("kind").getAsString().toUpperCase()
        );
        UniversalChestTemplateApi.registerMaterial(id, new UniversalChestTemplateApi.ChestMaterial(texture, kind));
    }

    public static void applyChestLock(JsonObject json) {
        UniversalChestTemplateApi.registerLock(
                Identifier.parse(json.get("id").getAsString()),
                new UniversalChestTemplateApi.LockMaterial(Identifier.parse(json.get("texture").getAsString()))
        );
    }

    public static ProjectileWeaponApi.ProjectileWeaponProfile parseProjectileWeapon(JsonObject json) {
        ProjectileWeaponApi.OverlayRotation rotation = ProjectileWeaponApi.OverlayRotation.valueOf(
                json.has("overlay_rotation") ? json.get("overlay_rotation").getAsString().toUpperCase() : "LEFT_90"
        );
        return new ProjectileWeaponApi.ProjectileWeaponProfile(
                json.get("charge_ticks").getAsInt(),
                json.has("repeat_delay_ticks") ? json.get("repeat_delay_ticks").getAsInt() : 1,
                json.has("base_damage") ? json.get("base_damage").getAsDouble() : 2.0,
                optionalId(json, "arrow_overlay_texture"),
                rotation,
                !json.has("require_full_charge") || json.get("require_full_charge").getAsBoolean()
        );
    }

    private static List<Identifier> ids(JsonArray array) {
        if (array == null) return List.of();
        List<Identifier> result = new ArrayList<>();
        for (JsonElement element : array) result.add(Identifier.parse(element.getAsString()));
        return result;
    }

    private static Identifier optionalId(JsonObject json, String key) {
        return json.has(key) && !json.get(key).isJsonNull() ? Identifier.parse(json.get(key).getAsString()) : null;
    }

    private static int parseColor(JsonObject json, String key, int fallback) {
        if (!json.has(key)) return fallback;
        JsonElement value = json.get(key);
        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()) return value.getAsInt();
        String text = value.getAsString().replace("#", "").replace("0x", "");
        return Integer.parseInt(text, 16);
    }
}
