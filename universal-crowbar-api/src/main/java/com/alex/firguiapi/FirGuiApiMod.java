package com.alex.firguiapi;
import com.alex.firguiapi.api.*;import net.fabricmc.api.ModInitializer;import net.minecraft.resources.Identifier;
public final class FirGuiApiMod implements ModInitializer{
 public static final String MOD_ID="fir-gui-api";
 public void onInitialize(){FirGuiConfig.load();FirGuiApi.register(Identifier.fromNamespaceAndPath(MOD_ID,"chest_3_rows"),FirGuiTemplates.chest(3));FirGuiApi.register(Identifier.fromNamespaceAndPath(MOD_ID,"crafting_3x3"),FirGuiTemplates.crafting3x3());FirGuiApi.register(Identifier.fromNamespaceAndPath(MOD_ID,"furnace"),FirGuiTemplates.furnace());}
}