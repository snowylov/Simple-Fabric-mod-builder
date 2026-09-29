package com.alex.firguiapi.api;
public final class FirGuiTemplates{
 private FirGuiTemplates(){}
 public static FirGuiDefinition chest(int rows){int r=Math.max(1,Math.min(6,rows));var b=FirGuiDefinition.builder(176,114+r*18);for(int y=0;y<r;y++)for(int x=0;x<9;x++)b.slot(8+x*18,18+y*18);inv(b,8,32+r*18);return b.build();}
 public static FirGuiDefinition crafting3x3(){var b=FirGuiDefinition.builder(176,166);for(int y=0;y<3;y++)for(int x=0;x<3;x++)b.slot(30+x*18,17+y*18);b.slot(124,35);inv(b,8,84);return b.build();}
 public static FirGuiDefinition furnace(){var b=FirGuiDefinition.builder(176,166);b.slot(56,17).slot(56,53).slot(116,35).progress("cook",79,34,24,17,false).progress("fuel",57,37,14,14,true);inv(b,8,84);return b.build();}
 private static void inv(FirGuiDefinition.Builder b,int x,int y){for(int r=0;r<3;r++)for(int c=0;c<9;c++)b.slot(x+c*18,y+r*18);for(int c=0;c<9;c++)b.slot(x+c*18,y+58);}
}