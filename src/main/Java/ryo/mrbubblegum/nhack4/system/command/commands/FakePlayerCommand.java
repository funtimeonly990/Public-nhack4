package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.lite.player.FakePlayer;
import ryo.mrbubblegum.nhack4.system.command.Command;

public class FakePlayerCommand extends Command {

    public FakePlayerCommand() {
        super("fakeplayer");
    }

    @Override
    public void execute(String[] commands) {
        FakePlayer.getInstance().enable();
    }
}