package com.alex.datadrivenpotionsapi.api;
import java.util.List;
public record DataDrivenPotionDefinition(String id,boolean poison,List<EffectEntry> effects){
 public record EffectEntry(String effect,int durationTicks,int amplifier){}
}