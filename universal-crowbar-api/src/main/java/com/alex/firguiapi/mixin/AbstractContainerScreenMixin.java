package com.alex.firguiapi.mixin;
import com.alex.firguiapi.FirGuiConfig;import net.minecraft.client.gui.GuiGraphics;import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;import net.minecraft.world.inventory.*;import org.spongepowered.asm.mixin.*;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu>{
 @Shadow protected int leftPos;@Shadow protected int topPos;@Shadow protected int imageWidth;@Shadow protected int imageHeight;@Shadow protected T menu;
 @Inject(method="render",at=@At("TAIL")) private void universalToolApis$overlay(GuiGraphics g,int mx,int my,float pt,CallbackInfo ci){if(!FirGuiConfig.reskinExistingGuis())return;g.fill(leftPos,topPos,leftPos+imageWidth,topPos+imageHeight,0x4D101010);for(Slot s:menu.slots)g.fill(leftPos+s.x,topPos+s.y,leftPos+s.x+16,topPos+s.y+16,0x4DE0E0E0);}
}