package com.alex.firguiapi.client;
import com.alex.firguiapi.api.*;import net.minecraft.client.gui.GuiGraphics;
public final class FirGuiRenderer{
 private FirGuiRenderer(){}
 public static void render(GuiGraphics g,int left,int top,FirGuiDefinition d,FirProgressProvider p){int bg=a(d.style().backgroundOpacity(),0x101010),slot=a(d.style().slotOpacity(),0xE0E0E0),bar=a(d.style().progressOpacity(),0xFFFFFF);g.fill(left,top,left+d.width(),top+d.height(),bg);for(FirSlot s:d.slots())g.fill(left+s.x(),top+s.y(),left+s.x()+s.width(),top+s.y()+s.height(),slot);if(p!=null)for(FirProgressBar b:d.progressBars()){float v=Math.max(0,Math.min(1,p.getProgress(b.id())));int w=b.vertical()?b.width():Math.round(b.width()*v),h=b.vertical()?Math.round(b.height()*v):b.height();g.fill(left+b.x(),top+b.y(),left+b.x()+w,top+b.y()+h,bar);}}
 private static int a(float o,int rgb){return(Math.round(Math.max(0,Math.min(1,o))*255)<<24)|(rgb&0xFFFFFF);}
}