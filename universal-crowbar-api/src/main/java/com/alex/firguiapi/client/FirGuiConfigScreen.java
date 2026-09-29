package com.alex.firguiapi.client;

import com.alex.firguiapi.FirGuiConfig;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class FirGuiConfigScreen extends Screen {
    private final Screen parent;
    public FirGuiConfigScreen(Screen parent){super(Component.literal("Fir GUI API Settings"));this.parent=parent;}
    @Override protected void init(){
        addRenderableWidget(Button.builder(label(),button->{FirGuiConfig.setReskinExistingGuis(!FirGuiConfig.reskinExistingGuis());button.setMessage(label());}).bounds(width/2-100,height/2-10,200,20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"),button->onClose()).bounds(width/2-100,height/2+18,200,20).build());
    }
    private static Component label(){return Component.literal("Reskin existing GUIs: "+(FirGuiConfig.reskinExistingGuis()?"ON":"OFF"));}
    @Override public void onClose(){if(minecraft!=null)minecraft.setScreen(parent);}
}