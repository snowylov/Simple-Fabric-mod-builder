package com.alex.firguiapi.api;
import net.minecraft.resources.Identifier;import java.util.*;
public final class FirGuiApi{
 private static final Map<Identifier,FirGuiDefinition> DEFINITIONS=new LinkedHashMap<>();
 private FirGuiApi(){}
 public static void register(Identifier id,FirGuiDefinition d){DEFINITIONS.put(id,d);}
 public static FirGuiDefinition get(Identifier id){return DEFINITIONS.get(id);}
 public static Map<Identifier,FirGuiDefinition> definitions(){return Collections.unmodifiableMap(DEFINITIONS);}
}