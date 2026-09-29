package com.alex.universalwrenchapi.api;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class WrenchApi {
    private static final List<WrenchHandler> HANDLERS = new CopyOnWriteArrayList<>();

    private WrenchApi() {
    }

    public static void register(WrenchHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler cannot be null");
        }
        HANDLERS.add(handler);
    }

    public static boolean unregister(WrenchHandler handler) {
        return HANDLERS.remove(handler);
    }

    public static WrenchActionResult invoke(WrenchContext context) {
        for (WrenchHandler handler : HANDLERS) {
            WrenchActionResult result = handler.use(context);
            if (result != WrenchActionResult.PASS) {
                return result;
            }
        }
        return WrenchActionResult.PASS;
    }
}
