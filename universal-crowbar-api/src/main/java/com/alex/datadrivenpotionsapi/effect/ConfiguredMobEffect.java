package com.alex.datadrivenpotionsapi.effect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
public final class ConfiguredMobEffect extends MobEffect{
 public ConfiguredMobEffect(boolean harmful,int color){super(harmful?MobEffectCategory.HARMFUL:MobEffectCategory.BENEFICIAL,color);}
}