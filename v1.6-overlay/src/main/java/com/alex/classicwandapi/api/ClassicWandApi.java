package com.alex.classicwandapi.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ClassicWandApi {
    public static final String API_ID = "classic-wand-api";
    private static final Map<Item, WandDefinition> WANDS = new IdentityHashMap<>();
    private static final Map<Item, WandFocus> FOCI = new IdentityHashMap<>();
    private static FocusGuiOpener guiOpener = context -> false;

    private ClassicWandApi() {}

    public static void registerWand(Item wand, WandDefinition definition) {
        Objects.requireNonNull(wand, "wand");
        Objects.requireNonNull(definition, "definition");
        if (WANDS.putIfAbsent(wand, definition) != null) {
            throw new IllegalStateException("Wand already registered: " + wand);
        }
    }

    public static void registerFocus(Item focusItem, WandFocus focus) {
        Objects.requireNonNull(focusItem, "focusItem");
        Objects.requireNonNull(focus, "focus");
        if (FOCI.putIfAbsent(focusItem, focus) != null) {
            throw new IllegalStateException("Wand focus already registered: " + focusItem);
        }
    }

    public static Optional<WandDefinition> wand(Item item) {
        return Optional.ofNullable(WANDS.get(item));
    }

    public static Optional<WandFocus> focus(ItemStack stack) {
        return Optional.ofNullable(FOCI.get(stack.getItem()));
    }

    public static void setFocusGuiOpener(FocusGuiOpener opener) {
        guiOpener = Objects.requireNonNull(opener, "opener");
    }

    public static boolean onShiftRightClick(Level level, Player player, ItemStack wandStack) {
        WandDefinition definition = WANDS.get(wandStack.getItem());
        if (definition == null || !player.isShiftKeyDown()) return false;
        return guiOpener.open(new FocusGuiContext(level, player, wandStack, definition));
    }

    public static void cast(WandCastContext context) {
        focus(context.focusStack()).ifPresent(focus -> focus.cast(context));
    }

    public static Map<Item, WandDefinition> wands() {
        return Collections.unmodifiableMap(WANDS);
    }

    public static Map<Item, WandFocus> focuses() {
        return Collections.unmodifiableMap(FOCI);
    }

    public record WandDefinition(int focusSlots, boolean focusRequired) {
        public WandDefinition {
            if (focusSlots < 1) throw new IllegalArgumentException("focusSlots must be >= 1");
        }
    }

    public record FocusGuiContext(Level level, Player player, ItemStack wandStack, WandDefinition definition) {}

    public record WandCastContext(
            Level level,
            Player player,
            ItemStack wandStack,
            ItemStack focusStack,
            float partialTick
    ) {}

    @FunctionalInterface
    public interface FocusGuiOpener {
        boolean open(FocusGuiContext context);
    }

    @FunctionalInterface
    public interface WandFocus {
        void cast(WandCastContext context);
    }
}
