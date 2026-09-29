package com.alex.datadrivenitemattributesapi.api;
public record ItemAttributeDefinition(String id,double defaultValue,double minValue,double maxValue,String description){
 public double clamp(double v){return Math.max(minValue,Math.min(maxValue,v));}
}