package com.alex.universalcrowbarapi.api;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
public final class CrowbarApi {
    private static final List<CrowbarHandler> HANDLERS = new CopyOnWriteArrayList<>();
    private CrowbarApi() {}
    public static void register(CrowbarHandler handler) {
        if (handler == null) throw new IllegalArgumentException("handler cannot be null");
        HANDLERS.add(handler);
    }
    public static boolean unregister(CrowbarHandler handler) { return HANDLERS.remove(handler); }
    public static CrowbarActionResult invoke(CrowbarContext context) {
        for (CrowbarHandler handler : HANDLERS) {
            CrowbarActionResult result = handler.use(context);
            if (result != CrowbarActionResult.PASS) return result;
        }
        return CrowbarActionResult.PASS;
    }
}
