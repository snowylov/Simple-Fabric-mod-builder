package com.alex.universalexpansionapi;

import com.alex.universalchestapi.api.UniversalChestTemplateApi;
import net.fabricmc.api.ModInitializer;

public final class UniversalExpansionApiMod implements ModInitializer {
    @Override
    public void onInitialize() {
        UniversalChestTemplateApi.bootstrapDefaults();
    }
}
