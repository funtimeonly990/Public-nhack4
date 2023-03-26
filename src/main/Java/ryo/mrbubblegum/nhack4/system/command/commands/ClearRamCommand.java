package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.system.command.Command;

public class ClearRamCommand
        extends Command {
    public ClearRamCommand() {
        super("clearram");
    }

    @Override
    public void execute(String[] commands) {
        System.gc();
        Command.sendMessage("Finished clearing the ram.", false);
    }
}