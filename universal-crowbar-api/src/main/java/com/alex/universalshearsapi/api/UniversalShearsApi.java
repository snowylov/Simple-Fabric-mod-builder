package com.alex.universalshearsapi.api;

import com.alex.universalshearsapi.UniversalShearsApiMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class UniversalShearsApi {
    public static final TagKey<Item> SHEARS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath(UniversalShearsApiMod.MOD_ID, "shears")
    );

    private static final List<ShearsHandler> HANDLERS = new CopyOnWriteArrayList<>();
    private static final Set<Item> REGISTERED_SHEARS = ConcurrentHashMap.newKeySet();

    private UniversalShearsApi() {
    }

    public static void registerHandler(ShearsHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler cannot be null");
        }
        HANDLERS.add(handler);
    }

    public static boolean unregisterHandler(ShearsHandler handler) {
        return HANDLERS.remove(handler);
    }

    public static void registerShears(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        REGISTERED_SHEARS.add(item);
    }

    public static boolean unregisterShears(Item item) {
        return REGISTERED_SHEARS.remove(item);
    }

    public static boolean isShears(ItemStack stack) {
        return stack.is(SHEARS) || REGISTERED_SHEARS.contains(stack.getItem());
    }

    public static ShearsActionResult invoke(ShearsContext context) {
        for (ShearsHandler handler : HANDLERS) {
            ShearsActionResult result = handler.use(context);
            if (result != ShearsActionResult.PASS) {
                return result;
            }
        }
        return ShearsActionResult.PASS;
    }
}
