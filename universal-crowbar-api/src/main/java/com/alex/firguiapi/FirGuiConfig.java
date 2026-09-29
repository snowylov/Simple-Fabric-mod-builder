package com.alex.firguiapi;
import com.google.gson.*;import net.fabricmc.loader.api.FabricLoader;import java.io.*;import java.nio.file.*;
public final class FirGuiConfig{
 private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();private static boolean enabled=true;private static Path path;
 private FirGuiConfig(){}
 public static void load(){path=FabricLoader.getInstance().getConfigDir().resolve("universal-tool-apis/fir_gui.json");try{Files.createDirectories(path.getParent());if(Files.isRegularFile(path)){try(Reader r=Files.newBufferedReader(path)){Data d=GSON.fromJson(r,Data.class);if(d!=null)enabled=d.reskinExistingGuis;}}else save();}catch(Exception ignored){}}
 public static boolean reskinExistingGuis(){return enabled;}public static void setReskinExistingGuis(boolean v){enabled=v;save();}
 private static void save(){if(path==null)return;try(Writer w=Files.newBufferedWriter(path)){GSON.toJson(new Data(enabled),w);}catch(Exception ignored){}}
 private record Data(boolean reskinExistingGuis){}
}