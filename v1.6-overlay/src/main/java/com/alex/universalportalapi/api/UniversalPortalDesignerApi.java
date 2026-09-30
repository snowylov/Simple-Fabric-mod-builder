package com.alex.universalportalapi.api;

import net.minecraft.resources.Identifier;

import java.util.Objects;

public final class UniversalPortalDesignerApi {
    public static final Identifier DEFAULT_PORTAL_TEXTURE =
            Identifier.fromNamespaceAndPath("minecraft", "block/nether_portal");

    private UniversalPortalDesignerApi() {}

    public static UniversalPortalApi.PortalDefinition create(
            Identifier destination,
            int outerSize,
            float red,
            float green,
            float blue
    ) {
        int size = Math.max(4, Math.min(6, outerSize));
        return new UniversalPortalApi.PortalDefinition(
                Objects.requireNonNull(destination),
                UniversalPortalApi.PortalKind.NETHER_STYLE,
                new UniversalPortalApi.PortalShape(size - 2, size - 2, size - 2, size - 2, 1, UniversalPortalApi.Orientation.VERTICAL),
                new UniversalPortalApi.PortalTexture(DEFAULT_PORTAL_TEXTURE, true, red, green, blue),
                Identifier.fromNamespaceAndPath("minecraft", "block.portal.ambient"),
                java.util.Set.of(),
                80
        );
    }
}
