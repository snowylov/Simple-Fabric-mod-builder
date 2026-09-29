package com.alex.universaltoolsapi.screwdriver.api;

@FunctionalInterface
public interface ScrewdriverHandler {
    ScrewdriverActionResult use(ScrewdriverContext context);
}
