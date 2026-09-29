package com.alex.datadrivenpotionsapi.mixin;
import com.alex.datadrivenpotionsapi.DataDrivenPotionsApiMod;import net.minecraft.world.effect.MobEffectInstance;import net.minecraft.world.entity.LivingEntity;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;
@Mixin(LivingEntity.class) public abstract class WeightFallDamageMixin{
 @ModifyVariable(method="causeFallDamage",at=@At("HEAD"),argsOnly=true,ordinal=0)
 private double universalToolApis$weight(double d){LivingEntity self=(LivingEntity)(Object)this;for(MobEffectInstance i:self.getActiveEffects())if(i.is(DataDrivenPotionsApiMod.WEIGHT))return d*Math.pow(3.0,i.getAmplifier()+1);return d;}
}