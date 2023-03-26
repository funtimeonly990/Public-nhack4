package ryo.mrbubblegum.nhack4.lite.client;

import ryo.mrbubblegum.nhack4.impl.util.ColorUtil;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Colors
        extends Module {
    public static Colors INSTANCE;
    public Setting<Boolean> rainbow = this.register(new Setting<Boolean>("Rainbow", Boolean.valueOf(false), "Rainbow colors."));
    public Setting<Integer> rainbowSpeed = this.register(new Setting<Object>("Speed", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(100), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowSaturation = this.register(new Setting<Object>("Saturation", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Integer> rainbowBrightness = this.register(new Setting<Object>("Brightness", Integer.valueOf(150), Integer.valueOf(0), Integer.valueOf(255), v -> this.rainbow.getValue()));
    public Setting<Integer> red = this.register(new Setting<Object>("Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !this.rainbow.getValue()));
    public Setting<Integer> green = this.register(new Setting<Object>("Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !this.rainbow.getValue()));
    public Setting<Integer> blue = this.register(new Setting<Object>("Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !this.rainbow.getValue()));
    public Setting<Integer> alpha = this.register(new Setting<Object>("Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> !this.rainbow.getValue()));
    public float hue;
    public Map<Integer, Integer> colorHeightMap = new HashMap<Integer, Integer>();

    public Colors() {
        super("Colors", "sync colors across the client", Module.Category.CLIENT, true, false, true);
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        int colorSpeed = 101 - this.rainbowSpeed.getValue();
        float tempHue = this.hue = (float) (System.currentTimeMillis() % (long) (360L * colorSpeed)) / (360.0f * (float) colorSpeed);
        for (int i = 0; i <= 510; ++i) {
            this.colorHeightMap.put(i, Color.HSBtoRGB(tempHue, (float) this.rainbowSaturation.getValue().intValue() / 255.0f, (float) this.rainbowBrightness.getValue().intValue() / 255.0f));
            tempHue += 0.0013071896f;
        }
        if (ClickGui.getInstance().colorSync.getValue().booleanValue()) {
            Loader.colorManager.setColor(INSTANCE.getCurrentColor().getRed(), INSTANCE.getCurrentColor().getGreen(), INSTANCE.getCurrentColor().getBlue(), ClickGui.getInstance().hoverAlpha.getValue());
        }
    }

    public int getCurrentColorHex() {
        if (this.rainbow.getValue().booleanValue()) {
            return Color.HSBtoRGB(this.hue, (float) this.rainbowSaturation.getValue().intValue() / 255.0f, (float) this.rainbowBrightness.getValue().intValue() / 255.0f);
        }
        return ColorUtil.toARGB(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }

    public Color getCurrentColor() {
        if (this.rainbow.getValue().booleanValue()) {
            return Color.getHSBColor(this.hue, (float) this.rainbowSaturation.getValue().intValue() / 255.0f, (float) this.rainbowBrightness.getValue().intValue() / 255.0f);
        }
        return new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
    }
}