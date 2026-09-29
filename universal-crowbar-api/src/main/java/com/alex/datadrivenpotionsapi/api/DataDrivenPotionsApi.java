package com.alex.datadrivenpotionsapi.api;
import net.minecraft.core.Holder;import net.minecraft.world.effect.MobEffect;import net.minecraft.world.item.alchemy.Potion;import java.util.*;
public final class DataDrivenPotionsApi{
 private static final Map<String,PotionEffectDefinition> DEFINITIONS=new LinkedHashMap<>();
 private static final Map<String,DataDrivenPotionDefinition> POTIONS=new LinkedHashMap<>();
 private static final Map<String,Holder<MobEffect>> EFFECT_HOLDERS=new LinkedHashMap<>();
 private static final Map<String,Holder<Potion>> POTION_HOLDERS=new LinkedHashMap<>();
 private DataDrivenPotionsApi(){}
 public static void registerDefinition(PotionEffectDefinition d){DEFINITIONS.put(Objects.requireNonNull(d).id(),d);}
 public static PotionEffectDefinition getDefinition(String id){return DEFINITIONS.get(id);}
 public static Map<String,PotionEffectDefinition> definitions(){return Collections.unmodifiableMap(DEFINITIONS);}
 public static void registerPotion(DataDrivenPotionDefinition d){POTIONS.put(Objects.requireNonNull(d).id(),d);}
 public static DataDrivenPotionDefinition getPotion(String id){return POTIONS.get(id);}
 public static Map<String,DataDrivenPotionDefinition> potions(){return Collections.unmodifiableMap(POTIONS);}
 public static void bindEffect(String id,Holder<MobEffect> h){EFFECT_HOLDERS.put(id,h);}public static Holder<MobEffect> effect(String id){return EFFECT_HOLDERS.get(id);}
 public static void bindPotion(String id,Holder<Potion> h){POTION_HOLDERS.put(id,h);}public static Holder<Potion> potion(String id){return POTION_HOLDERS.get(id);}
}