package com.alex.universaltoolsapi.screwdriver.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ScrewdriverApi {
    public static final TagKey<Item> SCREWDRIVERS = TagKey.create(
            Registries.ITEM,
            Identifier.fromNamespaceAndPath("universal-screwdriver-api", "screwdrivers")
    );

    private static final List<ScrewdriverHandler> HANDLERS = new CopyOnWriteArrayList<>();
    private static final Set<Item> REGISTERED_ITEMS = ConcurrentHashMap.newKeySet();

    private ScrewdriverApi() {
    }

    public static void registerHandler(ScrewdriverHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler cannot be null");
        }
        HANDLERS.add(handler);
    }

    public static boolean unregisterHandler(ScrewdriverHandler handler) {
        return HANDLERS.remove(handler);
    }

    public static void registerScrewdriver(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        REGISTERED_ITEMS.add(item);
    }

    public static boolean isScrewdriver(ItemStack stack) {
        return stack.is(SCREWDRIVERS) || REGISTERED_ITEMS.contains(stack.getItem());
    }

    public static ScrewdriverActionResult invoke(ScrewdriverContext context) {
        for (ScrewdriverHandler handler : HANDLERS) {
            ScrewdriverActionResult result = handler.use(context);
            if (result != ScrewdriverActionResult.PASS) {
                return result;
            }
        }
        return ScrewdriverActionResult.PASS;
    }
}
