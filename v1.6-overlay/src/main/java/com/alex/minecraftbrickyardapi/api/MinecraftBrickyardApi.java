package com.alex.minecraftbrickyardapi.api;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class MinecraftBrickyardApi {
    public static final String API_ID = "minecraft-brickyard-api";
    private static final Map<Identifier, BrickFamily> FAMILIES = new LinkedHashMap<>();

    private MinecraftBrickyardApi() {}

    public static BrickFamily registerFamily(Identifier id, BrickFamily family) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(family, "family");
        if (FAMILIES.putIfAbsent(id, family) != null) {
            throw new IllegalStateException("Brick family already registered: " + id);
        }
        return family;
    }

    public static BrickFamily registerMinecraftFamily(Identifier id, BrickFamily family) {
        registerItem(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_brick"), family.brickItem());
        registerBlock(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_bricks"), family.bricks());
        registerBlock(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_brick_stairs"), family.stairs());
        registerBlock(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_brick_slab"), family.slab());
        registerBlock(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_brick_wall"), family.wall());
        registerBlock(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_brick_pressure_plate"), family.pressurePlate());
        registerBlock(Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath() + "_brick_button"), family.button());
        return registerFamily(id, family);
    }

    public static Item registerItem(Identifier id, Item item) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static <T extends Block> T registerBlock(Identifier id, T block) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    public static Optional<BrickFamily> get(Identifier id) {
        return Optional.ofNullable(FAMILIES.get(id));
    }

    public static Map<Identifier, BrickFamily> families() {
        return Collections.unmodifiableMap(FAMILIES);
    }

    public record BrickFamily(
            Item brickItem,
            Block bricks,
            StairBlock stairs,
            SlabBlock slab,
            WallBlock wall,
            PressurePlateBlock pressurePlate,
            ButtonBlock button
    ) {
        public BrickFamily {
            Objects.requireNonNull(brickItem, "brickItem");
            Objects.requireNonNull(bricks, "bricks");
            Objects.requireNonNull(stairs, "stairs");
            Objects.requireNonNull(slab, "slab");
            Objects.requireNonNull(wall, "wall");
            Objects.requireNonNull(pressurePlate, "pressurePlate");
            Objects.requireNonNull(button, "button");
        }
    }
}
