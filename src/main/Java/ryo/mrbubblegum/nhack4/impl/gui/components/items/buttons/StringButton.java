package ryo.mrbubblegum.nhack4.impl.gui.components.items.buttons;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.SoundEvent;
import org.lwjgl.input.Keyboard;
import ryo.mrbubblegum.nhack4.impl.gui.LiteGui;
import ryo.mrbubblegum.nhack4.impl.util.ColorUtil;
import ryo.mrbubblegum.nhack4.impl.util.MathUtil;
import ryo.mrbubblegum.nhack4.impl.util.RenderUtil;
import ryo.mrbubblegum.nhack4.impl.util.Util;
import ryo.mrbubblegum.nhack4.lite.client.ClickGui;
import ryo.mrbubblegum.nhack4.lite.hud.HUD;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class StringButton
        extends Button {
    private final Setting setting;
    public boolean isListening;
    private CurrentString currentString = new CurrentString("");

    public StringButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 40;
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.getInstance().sideSettings.getValue().booleanValue()) {
            int sideColor = ColorUtil.toRGBA(ClickGui.getInstance().sideRed.getValue(), ClickGui.getInstance().sideGreen.getValue(), ClickGui.getInstance().sideBlue.getValue(), ClickGui.getInstance().sideAlpha.getValue());
            RenderUtil.drawRect(this.x, this.y, this.x + 1.0f, this.y + (float) this.height + 1.0f, sideColor);
        }
        if (ClickGui.getInstance().rainbowRolling.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)), Loader.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y + this.height, 0, this.renderer.scaledHeight)), Loader.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, (float) this.width + 7.4f, (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y, 0, this.renderer.scaledHeight)) : color) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int) this.y + this.height, 0, this.renderer.scaledHeight)) : color1) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        } else {
            RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? Loader.colorManager.getColorWithAlpha(Loader.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : Loader.colorManager.getColorWithAlpha(Loader.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        }
        if (this.isListening) {
            Loader.textManager.drawStringWithShadow(this.currentString.getString() + Loader.textManager.getIdleSign(), this.x + 2.3f, this.y - 1.7f - (float) LiteGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        } else {
            Loader.textManager.drawStringWithShadow((this.setting.shouldRenderName() ? this.setting.getName() + " \u00a77" : "") + this.setting.getValue(), this.x + 2.3f, this.y - 1.7f - (float) LiteGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            Util.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (this.isListening) {
            if (keyCode == 1) {
                return;
            }
            if (keyCode == 28) {
                this.enterString();
            } else if (keyCode == 14) {
                this.setString(StringButton.removeLastChar(this.currentString.getString()));
            } else if (keyCode == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
                try {
                    this.setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                this.setString(this.currentString.getString() + typedChar);
            }
        }
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    private void enterString() {
        if (this.currentString.getString().isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        } else {
            this.setting.setValue(this.currentString.getString());
        }
        this.setString("");
        super.onMouseClick();
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.isListening = !this.isListening;
    }

    @Override
    public boolean getState() {
        return !this.isListening;
    }

    public void setString(String newString) {
        this.currentString = new CurrentString(newString);
    }

    public static class CurrentString {
        private final String string;

        public CurrentString(String string) {
            this.string = string;
        }

        public String getString() {
            return this.string;
        }
    }
}
