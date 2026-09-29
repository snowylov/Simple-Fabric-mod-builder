package com.alex.datadrivenpotionsapi.api;
import java.util.*;
public final class DataDrivenPotionsApi{
 private static final Map<String,PotionEffectDefinition> EFFECTS=new LinkedHashMap<>();
 private static final Map<String,DataDrivenPotionDefinition> POTIONS=new LinkedHashMap<>();
 private DataDrivenPotionsApi(){}
 public static void registerDefinition(PotionEffectDefinition d){EFFECTS.put(Objects.requireNonNull(d).id(),d);}
 public static PotionEffectDefinition getDefinition(String id){return EFFECTS.get(id);}
 public static Map<String,PotionEffectDefinition> definitions(){return Collections.unmodifiableMap(EFFECTS);}
 public static void registerPotion(DataDrivenPotionDefinition d){POTIONS.put(Objects.requireNonNull(d).id(),d);}
 public static DataDrivenPotionDefinition getPotion(String id){return POTIONS.get(id);}
 public static Map<String,DataDrivenPotionDefinition> potions(){return Collections.unmodifiableMap(POTIONS);}
}