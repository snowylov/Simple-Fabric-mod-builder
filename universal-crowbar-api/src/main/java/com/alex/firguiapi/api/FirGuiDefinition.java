package com.alex.firguiapi.api;
import java.util.*;
public final class FirGuiDefinition{
 private final int width,height;private final FirGuiStyle style;private final List<FirSlot> slots;private final List<FirProgressBar> bars;
 private FirGuiDefinition(Builder b){width=b.width;height=b.height;style=b.style;slots=List.copyOf(b.slots);bars=List.copyOf(b.bars);}
 public int width(){return width;}public int height(){return height;}public FirGuiStyle style(){return style;}public List<FirSlot> slots(){return Collections.unmodifiableList(slots);}public List<FirProgressBar> progressBars(){return Collections.unmodifiableList(bars);}
 public static Builder builder(int w,int h){return new Builder(w,h);}
 public static final class Builder{private final int width,height;private FirGuiStyle style=FirGuiStyle.DEFAULT;private final List<FirSlot> slots=new ArrayList<>();private final List<FirProgressBar> bars=new ArrayList<>();
  private Builder(int w,int h){width=w;height=h;}public Builder style(FirGuiStyle s){style=s;return this;}public Builder slot(int x,int y){slots.add(new FirSlot(x,y));return this;}public Builder slot(int x,int y,int w,int h){slots.add(new FirSlot(x,y,w,h));return this;}public Builder progress(String id,int x,int y,int w,int h,boolean v){bars.add(new FirProgressBar(id,x,y,w,h,v));return this;}public FirGuiDefinition build(){return new FirGuiDefinition(this);}
 }
}