package ryo.mrbubblegum.nhack4.lite.client;

import ryo.mrbubblegum.nhack4.impl.util.Util;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

public class NickHider
        extends Module {
    private static NickHider instance;
    public final Setting<Boolean> changeOwn = this.register(new Setting<Boolean>("MyName", true));
    public final Setting<String> ownName = this.register(new Setting<Object>("Name", "Name here...", v -> this.changeOwn.getValue()));

    public NickHider() {
        super("NickChanger", "Helps with creating media", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    public static NickHider getInstance() {
        if (instance == null) {
            instance = new NickHider();
        }
        return instance;
    }

    public static String getPlayerName() {
        if (NickHider.fullNullCheck() || !PingBypass.getInstance().isConnected()) {
            return Util.mc.getSession().getUsername();
        }
        String name = PingBypass.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return Util.mc.getSession().getUsername();
        }
        return name;
    }
}

