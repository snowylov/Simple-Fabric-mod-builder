package com.alex.firguiapi.client;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
public final class FirGuiModMenuIntegration implements ModMenuApi {
 @Override public ConfigScreenFactory<?> getModConfigScreenFactory(){return FirGuiConfigScreen::new;}
}