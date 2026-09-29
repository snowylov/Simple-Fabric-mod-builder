package com.alex.datadrivenpotionsapi;
import com.alex.datadrivenpotionsapi.api.*;
import com.alex.datadrivenpotionsapi.effect.WeightMobEffect;
import com.google.gson.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.MobEffect;
import java.io.*;import java.nio.file.*;
public final class DataDrivenPotionsApiMod implements ModInitializer{
 public static final String MOD_ID="data-driven-potions-api";
 public static MobEffect WEIGHT;
 private static final Gson GSON=new Gson();
 public void onInitialize(){
  Identifier id=Identifier.fromNamespaceAndPath(MOD_ID,"weight");
  ResourceKey<MobEffect> key=ResourceKey.create(Registries.MOB_EFFECT,id);
  WEIGHT=Registry.register(BuiltInRegistries.MOB_EFFECT,key,new WeightMobEffect());
  DataDrivenPotionsApi.registerDefinition(new PotionEffectDefinition(MOD_ID+":weight","weight",0x5A4635,true,3.0));
  DataDrivenPotionsApi.registerPotion(new DataDrivenPotionDefinition(MOD_ID+":weight_potion",false,java.util.List.of(new DataDrivenPotionDefinition.EffectEntry(MOD_ID+":weight",3600,0))));
  DataDrivenPotionsApi.registerPotion(new DataDrivenPotionDefinition(MOD_ID+":weight_poison",true,java.util.List.of(new DataDrivenPotionDefinition.EffectEntry(MOD_ID+":weight",1800,1))));
  load();
 }
 private static void load(){Path dir=FabricLoader.getInstance().getConfigDir().resolve("universal-tool-apis/potions");try{Files.createDirectories(dir);try(var s=Files.list(dir)){s.filter(p->p.getFileName().toString().endsWith(".json")).forEach(DataDrivenPotionsApiMod::read);}}catch(IOException ignored){}}
 private static void read(Path p){try(Reader r=Files.newBufferedReader(p)){JsonObject j=GSON.fromJson(r,JsonObject.class);String id=j.get("id").getAsString();String kind=j.has("kind")?j.get("kind").getAsString():"generic";int color=j.has("color")?Integer.decode(j.get("color").getAsString()):0x7F7F7F;boolean harmful=j.has("harmful")&&j.get("harmful").getAsBoolean();double scalar=j.has("scalar")?j.get("scalar").getAsDouble():1.0;DataDrivenPotionsApi.registerDefinition(new PotionEffectDefinition(id,kind,color,harmful,scalar));if(j.has("potion")){JsonObject q=j.getAsJsonObject("potion");String pid=q.has("id")?q.get("id").getAsString():id+"_potion";boolean poison=q.has("poison")&&q.get("poison").getAsBoolean();int dur=q.has("duration")?q.get("duration").getAsInt():3600;int amp=q.has("amplifier")?q.get("amplifier").getAsInt():0;DataDrivenPotionsApi.registerPotion(new DataDrivenPotionDefinition(pid,poison,java.util.List.of(new DataDrivenPotionDefinition.EffectEntry(id,dur,amp))));}}catch(Exception ignored){}}
}