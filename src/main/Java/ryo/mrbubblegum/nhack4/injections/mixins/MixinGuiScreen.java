package ryo.mrbubblegum.nhack4.injections.mixins;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ryo.mrbubblegum.nhack4.lite.misc.ToolTips;
import ryo.mrbubblegum.nhack4.lite.render.NoRender;

@Mixin(value = {GuiScreen.class})
public class MixinGuiScreen
        extends Gui {
    @Inject(method = {"renderToolTip"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderToolTipHook(ItemStack stack, int x, int y, CallbackInfo info) {
        if (ToolTips.getInstance().isOn() && ToolTips.getInstance().shulkers.getValue().booleanValue() && stack.getItem() instanceof ItemShulkerBox) {
            ToolTips.getInstance().renderShulkerToolTip(stack, x, y, null);
            info.cancel();
        }
    }

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    public void drawDefaultBackground(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().containerBackground.getValue().booleanValue()) {
            info.cancel();
        }
    }
}
