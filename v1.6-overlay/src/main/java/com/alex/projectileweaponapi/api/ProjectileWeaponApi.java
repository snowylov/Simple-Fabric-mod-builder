package com.alex.projectileweaponapi.api;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class ProjectileWeaponApi {
    public static final String API_ID = "projectile-weapon-api";
    private static final Map<Item, Entry> ENTRIES = new IdentityHashMap<>();

    private ProjectileWeaponApi() {}

    public static void register(Item item, ProjectileWeaponProfile profile, Shooter shooter) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(profile, "profile");
        Objects.requireNonNull(shooter, "shooter");
        if (ENTRIES.putIfAbsent(item, new Entry(profile, shooter)) != null) {
            throw new IllegalStateException("Projectile weapon already registered: " + item);
        }
    }

    public static Optional<ProjectileWeaponProfile> profile(Item item) {
        Entry entry = ENTRIES.get(item);
        return entry == null ? Optional.empty() : Optional.of(entry.profile());
    }

    public static boolean tickHeldUse(Level level, LivingEntity user, ItemStack stack, int useTicks) {
        Entry entry = ENTRIES.get(stack.getItem());
        if (entry == null || useTicks < entry.profile().chargeTicks()) {
            return false;
        }
        int sinceFirstShot = useTicks - entry.profile().chargeTicks();
        if (sinceFirstShot % entry.profile().repeatDelayTicks() != 0) {
            return false;
        }
        entry.shooter().shoot(new ShootContext(level, user, stack, entry.profile(), useTicks));
        return true;
    }

    public static Map<Item, Entry> entries() {
        return Collections.unmodifiableMap(ENTRIES);
    }

    public record ProjectileWeaponProfile(
            int chargeTicks,
            int repeatDelayTicks,
            double baseDamage,
            Identifier arrowOverlayTexture,
            OverlayRotation arrowOverlayRotation,
            boolean requireFullCharge
    ) {
        public ProjectileWeaponProfile {
            if (chargeTicks < 1) throw new IllegalArgumentException("chargeTicks must be >= 1");
            if (repeatDelayTicks < 1) throw new IllegalArgumentException("repeatDelayTicks must be >= 1");
            if (baseDamage < 0) throw new IllegalArgumentException("baseDamage must be >= 0");
            Objects.requireNonNull(arrowOverlayRotation, "arrowOverlayRotation");
        }
    }

    public enum OverlayRotation {
        NONE,
        LEFT_90,
        RIGHT_90,
        UPSIDE_DOWN
    }

    public record ShootContext(
            Level level,
            LivingEntity user,
            ItemStack weapon,
            ProjectileWeaponProfile profile,
            int useTicks
    ) {}

    @FunctionalInterface
    public interface Shooter {
        void shoot(ShootContext context);
    }

    public record Entry(ProjectileWeaponProfile profile, Shooter shooter) {}
}
