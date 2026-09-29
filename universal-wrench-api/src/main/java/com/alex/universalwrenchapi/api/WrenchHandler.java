package com.alex.universalwrenchapi.api;

@FunctionalInterface
public interface WrenchHandler {
    WrenchActionResult use(WrenchContext context);
}
