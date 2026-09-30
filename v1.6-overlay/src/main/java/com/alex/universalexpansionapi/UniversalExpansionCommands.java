package com.alex.universalexpansionapi;

import com.alex.commonfurnitureapi.api.CommonFurnitureApi;
import com.alex.universalchestapi.api.UniversalChestApi;
import com.alex.universalportalapi.api.UniversalPortalApi;
import com.alex.universalportalapi.api.UniversalPortalDesignerApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.Commands;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.nio.file.Files;
import java.nio.file.Path;

public final class UniversalExpansionCommands {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private UniversalExpansionCommands() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("chest")
                .then(Commands.argument("lockTexture", StringArgumentType.word())
                .then(Commands.argument("baseTexture", StringArgumentType.word())
                .executes(c -> giveChest(c.getSource().getPlayerOrException(),
                        StringArgumentType.getString(c, "lockTexture"),
                        StringArgumentType.getString(c, "baseTexture"),
                        StringArgumentType.getString(c, "baseTexture")))
                .then(Commands.argument("bottomTexture", StringArgumentType.word())
                .executes(c -> giveChest(c.getSource().getPlayerOrException(),
                        StringArgumentType.getString(c, "lockTexture"),
                        StringArgumentType.getString(c, "baseTexture"),
                        StringArgumentType.getString(c, "bottomTexture")))))));

            dispatcher.register(Commands.literal("table")
                .then(Commands.literal("give")
                .then(Commands.argument("legTexture", StringArgumentType.word())
                .then(Commands.argument("topTexture", StringArgumentType.word())
                .then(Commands.argument("coverTexture", StringArgumentType.word())
                .then(Commands.argument("variant", IntegerArgumentType.integer(0, 4))
                .executes(c -> giveFurniture(c.getSource().getPlayerOrException(), "table",
                        StringArgumentType.getString(c, "legTexture"),
                        StringArgumentType.getString(c, "topTexture"),
                        StringArgumentType.getString(c, "coverTexture"),
                        IntegerArgumentType.getInteger(c, "variant")))))))));

            dispatcher.register(Commands.literal("chair")
                .then(Commands.literal("give")
                .then(Commands.argument("legTexture", StringArgumentType.word())
                .then(Commands.argument("bodyTexture", StringArgumentType.word())
                .then(Commands.argument("coverTexture", StringArgumentType.word())
                .then(Commands.argument("variant", IntegerArgumentType.integer(0, 3))
                .executes(c -> giveFurniture(c.getSource().getPlayerOrException(), "chair",
                        StringArgumentType.getString(c, "legTexture"),
                        StringArgumentType.getString(c, "bodyTexture"),
                        StringArgumentType.getString(c, "coverTexture"),
                        IntegerArgumentType.getInteger(c, "variant")))))))));

            dispatcher.register(Commands.literal("bench")
                .then(Commands.literal("give")
                .then(Commands.argument("legTexture", StringArgumentType.word())
                .then(Commands.argument("bodyTexture", StringArgumentType.word())
                .then(Commands.argument("coverTexture", StringArgumentType.word())
                .then(Commands.argument("variant", IntegerArgumentType.integer(0, 4))
                .executes(c -> giveFurniture(c.getSource().getPlayerOrException(), "bench",
                        StringArgumentType.getString(c, "legTexture"),
                        StringArgumentType.getString(c, "bodyTexture"),
                        StringArgumentType.getString(c, "coverTexture"),
                        IntegerArgumentType.getInteger(c, "variant")))))))));

            dispatcher.register(Commands.literal("portal")
                .then(Commands.literal("create")
                .then(Commands.argument("id", StringArgumentType.word())
                .then(Commands.argument("destination", StringArgumentType.word())
                .then(Commands.argument("size", IntegerArgumentType.integer(4, 6))
                .then(Commands.argument("red", IntegerArgumentType.integer(0, 255))
                .then(Commands.argument("green", IntegerArgumentType.integer(0, 255))
                .then(Commands.argument("blue", IntegerArgumentType.integer(0, 255))
                .executes(c -> {
                    Identifier id = Identifier.parse(StringArgumentType.getString(c, "id"));
                    Identifier dest = Identifier.parse(StringArgumentType.getString(c, "destination"));
                    int size = IntegerArgumentType.getInteger(c, "size");
                    float r = IntegerArgumentType.getInteger(c, "red") / 255.0F;
                    float g = IntegerArgumentType.getInteger(c, "green") / 255.0F;
                    float b = IntegerArgumentType.getInteger(c, "blue") / 255.0F;
                    UniversalPortalApi.PortalDefinition def = UniversalPortalDesignerApi.create(dest, size, r, g, b);
                    UniversalPortalApi.register(id, def);
                    writePortalDefinition(id, dest, size, r, g, b);
                    c.getSource().sendSuccess(() -> Component.literal("Created portal " + id + ". Restart/reload may be required for newly generated assets."), false);
                    return 1;
                })))))))));
        });
    }

    private static int giveChest(net.minecraft.server.level.ServerPlayer player, String lock, String top, String bottom) {
        Identifier lockId = normalizeTexture(lock);
        Identifier topId = normalizeTexture(top);
        Identifier bottomId = normalizeTexture(bottom);
        UniversalChestApi.ChestDefinition def = UniversalChestApi.splitTexture(lockId, topId, bottomId);
        ItemStack stack = new ItemStack(Items.CHEST);
        CompoundTag tag = new CompoundTag();
        tag.putString("uta_type", "universal_chest");
        tag.putString("lock_texture", lockId.toString());
        tag.putString("top_texture", topId.toString());
        tag.putString("bottom_texture", bottomId.toString());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Universal Chest"));
        player.getInventory().add(stack);
        return 1;
    }

    private static int giveFurniture(net.minecraft.server.level.ServerPlayer player, String type, String leg, String top, String cover, int variant) {
        Identifier legId = normalizeTexture(leg);
        Identifier topId = normalizeTexture(top);
        Identifier coverId = normalizeTexture(cover);
        CommonFurnitureApi.FurnitureDefinition def = switch (type) {
            case "chair" -> CommonFurnitureApi.chair(legId, topId, coverId, variant);
            case "bench" -> CommonFurnitureApi.bench(legId, topId, coverId, variant);
            default -> CommonFurnitureApi.table(legId, topId, coverId, variant);
        };
        ItemStack stack = new ItemStack(type.equals("chair") ? Items.OAK_STAIRS : type.equals("bench") ? Items.OAK_SLAB : Items.CRAFTING_TABLE);
        CompoundTag tag = new CompoundTag();
        tag.putString("uta_type", "common_furniture");
        tag.putString("furniture_kind", type);
        tag.putString("leg_texture", legId.toString());
        tag.putString("top_texture", topId.toString());
        tag.putString("cover_texture", coverId.toString());
        tag.putInt("model_variant", def.modelVariant());
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Universal " + Character.toUpperCase(type.charAt(0)) + type.substring(1)));
        player.getInventory().add(stack);
        return 1;
    }

    private static Identifier normalizeTexture(String text) {
        Identifier id = Identifier.parse(text);
        if (!id.getPath().contains("/")) {
            return Identifier.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath());
        }
        return id;
    }

    private static void writePortalDefinition(Identifier id, Identifier destination, int size, float r, float g, float b) {
        try {
            Path dir = FabricLoader.getInstance().getConfigDir().resolve("universal-tool-apis/generated-portals");
            Files.createDirectories(dir);
            var json = new com.google.gson.JsonObject();
            json.addProperty("id", id.toString());
            json.addProperty("destination_dimension", destination.toString());
            json.addProperty("frame_size", size);
            json.addProperty("source_texture", "minecraft:block/nether_portal");
            json.addProperty("grayscale", true);
            json.addProperty("red", r);
            json.addProperty("green", g);
            json.addProperty("blue", b);
            Files.writeString(dir.resolve(id.getNamespace() + "_" + id.getPath().replace('/', '_') + ".json"), GSON.toJson(json));
        } catch (Exception ignored) {}
    }
}
