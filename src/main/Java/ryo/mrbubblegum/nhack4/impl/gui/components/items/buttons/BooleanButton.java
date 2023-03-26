package ryo.mrbubblegum.nhack4.impl.gui.components.items.buttons;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import ryo.mrbubblegum.nhack4.impl.gui.LiteGui;
import ryo.mrbubblegum.nhack4.impl.util.ColorUtil;
import ryo.mrbubblegum.nhack4.impl.util.MathUtil;
import ryo.mrbubblegum.nhack4.impl.util.RenderUtil;
import ryo.mrbubblegum.nhack4.impl.util.Util;
import ryo.mrbubblegum.nhack4.lite.client.ClickGui;
import ryo.mrbubblegum.nhack4.lite.hud.HUD;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

public class BooleanButton
        extends Button {
    private final Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 40;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)), Loader.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y + this.height, 0, this.renderer.scaledHeight)), Loader.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, (float) this.width + 7.4f, (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)) : color) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y + this.height, 0, this.renderer.scaledHeight)) : color1) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        } else {
            RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? Loader.colorManager.getColorWithAlpha(Loader.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : Loader.colorManager.getColorWithAlpha(Loader.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        }
        Loader.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) LiteGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        if (ClickGui.getInstance().sideSettings.getValue().booleanValue()) {
            int sideColor = ColorUtil.toRGBA(ClickGui.getInstance().sideRed.getValue(), ClickGui.getInstance().sideGreen.getValue(), ClickGui.getInstance().sideBlue.getValue(), ClickGui.getInstance().sideAlpha.getValue());
            RenderUtil.drawRect(this.x, this.y, this.x + 1.0f, this.y + (float) this.height + 1.0f, sideColor);
        }
        if (ClickGui.getInstance().enableSwitch.getValue().booleanValue()) {
            Loader.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) LiteGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
            RenderUtil.drawRect(this.x + 85.0f, this.y + 5.0f, this.x + (float) this.width + 3.5f, this.y + (float) this.height - 2.0f, ColorUtil.toRGBA(ClickGui.getInstance().sbRed.getValue(), ClickGui.getInstance().sbGreen.getValue(), ClickGui.getInstance().sbBlue.getValue(), ClickGui.getInstance().sbAlpha.getValue()));
            if (this.getState()) {
                RenderUtil.drawRect(this.x + 93.0f, this.y + 6.0f, this.x + (float) this.width + 2.5f, this.y + (float) this.height - 3.0f, ColorUtil.toRGBA(ClickGui.getInstance().seRed.getValue(), ClickGui.getInstance().seGreen.getValue(), ClickGui.getInstance().seBlue.getValue(), ClickGui.getInstance().seAlpha.getValue()));
            } else {
                RenderUtil.drawRect(this.x + 86.0f, this.y + 6.0f, this.x + (float) this.width - 4.7f, this.y + (float) this.height - 3.0f, ColorUtil.toRGBA(ClickGui.getInstance().sdRed.getValue(), ClickGui.getInstance().sdGreen.getValue(), ClickGui.getInstance().sdBlue.getValue(), ClickGui.getInstance().sdAlpha.getValue()));
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.setting.setValue(!((Boolean) this.setting.getValue()));
    }

    @Override
    public boolean getState() {
        return (Boolean) this.setting.getValue();
    }
}
