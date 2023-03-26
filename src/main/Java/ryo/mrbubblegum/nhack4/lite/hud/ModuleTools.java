package ryo.mrbubblegum.nhack4.lite.hud;

import ryo.mrbubblegum.nhack4.impl.util.TextUtil;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

public class ModuleTools
        extends Module {
    private static ModuleTools INSTANCE;
    public Setting<Notifier> notifier = register(new Setting<Notifier>("ModuleNotifier", Notifier.NHACK4));
    public Setting<PopNotifier> popNotifier = register(new Setting<PopNotifier>("PopNotifier", PopNotifier.NONE));
    public Setting<TextUtil.Color> abyssColor = this.register(new Setting<TextUtil.Color>("AbyssTextColor", TextUtil.Color.AQUA, color -> this.notifier.getValue() == Notifier.ABYSS));

    public ModuleTools() {
        super("PopNotify", "Change settings", Category.HUD, true, false, false);
        INSTANCE = this;
    }


    public static ModuleTools getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleTools();
        }
        return INSTANCE;
    }

    public enum Notifier {
        NHACK4,
        FUTURE,
        DOTGOD,
        MUFFIN,
        WEATHER,
        SNOW,
        PYRO,
        CATALYST,
        KONAS,
        RUSHERHACK,
        LEGACY,
        EUROPA,
        ABYSS,
        LUIGIHACK
    }

    public enum PopNotifier {
        PHOBOS,
        FUTURE,
        DOTGOD,
        NONE
    }
}
