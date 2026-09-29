package com.alex.universalshearsapi.api;

@FunctionalInterface
public interface ShearsHandler {
    ShearsActionResult use(ShearsContext context);
}
