package ryo.mrbubblegum.nhack4.lite.player;

import ryo.mrbubblegum.nhack4.lite.Module;

public class MultiTask
        extends Module {
    private static MultiTask INSTANCE = new MultiTask();

    public MultiTask() {
        super("MultiTask", "multirtarks", Module.Category.PLAYER, false, false, false);
        this.setInstance();
    }

    public static MultiTask getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MultiTask();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

