package com.alex.firguiapi.api;
public record FirGuiStyle(float backgroundOpacity,float slotOpacity,float progressOpacity){
 public static final FirGuiStyle DEFAULT=new FirGuiStyle(.30F,.30F,.40F);
}