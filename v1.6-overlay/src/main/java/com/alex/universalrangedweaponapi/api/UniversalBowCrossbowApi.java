package com.alex.universalrangedweaponapi.api;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class UniversalBowCrossbowApi {
    public static final String API_ID = "universal-bow-crossbow-api";
    private static final Map<Item, WeaponProfile> PROFILES = new IdentityHashMap<>();

    private UniversalBowCrossbowApi() {}

    public static void registerBow(Item item, WeaponProfile profile) {
        PROFILES.put(Objects.requireNonNull(item), Objects.requireNonNull(profile).withType(WeaponType.BOW));
        UniversalRangedWeaponApi.registerWeapon(item);
    }

    public static void registerCrossbow(Item item, WeaponProfile profile) {
        PROFILES.put(Objects.requireNonNull(item), Objects.requireNonNull(profile).withType(WeaponType.CROSSBOW));
        UniversalRangedWeaponApi.registerWeapon(item);
    }

    public static Optional<WeaponProfile> profile(ItemStack stack) {
        return Optional.ofNullable(PROFILES.get(stack.getItem()));
    }

    public static Map<Item, WeaponProfile> profiles() {
        return Collections.unmodifiableMap(PROFILES);
    }

    public static boolean shouldConsumeArrow(boolean infinityActive, int shotIndex) {
        if (infinityActive) return false;
        return true;
    }

    public static int ammoCost(boolean infinityActive, int shotIndex) {
        return shouldConsumeArrow(infinityActive, shotIndex) ? 1 : 0;
    }

    public enum WeaponType { BOW, CROSSBOW }

    public record WeaponProfile(
            WeaponType type,
            int chargeTicks,
            int repeatDelayTicks,
            double damageMultiplier,
            boolean allowInfinity,
            boolean firstShotFreeWithInfinity,
            boolean consumeAmmo
    ) {
        public WeaponProfile {
            type = type == null ? WeaponType.BOW : type;
            chargeTicks = Math.max(1, chargeTicks);
            repeatDelayTicks = Math.max(1, repeatDelayTicks);
            if (damageMultiplier < 0) throw new IllegalArgumentException("damageMultiplier must be >= 0");
        }

        public WeaponProfile withType(WeaponType type) {
            return new WeaponProfile(type, chargeTicks, repeatDelayTicks, damageMultiplier, allowInfinity, firstShotFreeWithInfinity, consumeAmmo);
        }

        public boolean consumeForShot(boolean infinityActive, int shotIndex) {
            if (!consumeAmmo) return false;
            if (allowInfinity && infinityActive) return false;
            return true;
        }
    }
}
