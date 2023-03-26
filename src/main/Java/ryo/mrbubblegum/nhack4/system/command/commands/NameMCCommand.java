package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.impl.util.PlayerUtil;
import ryo.mrbubblegum.nhack4.system.command.Command;

import java.util.List;
import java.util.UUID;

public class NameMCCommand
        extends Command {
    public NameMCCommand() {
        super("namemc", new String[]{"<player>"});
    }

    @Override
    public void execute(String[] commands) {
        List<String> names;
        UUID uuid;
        if (commands.length == 1 || commands.length == 0) {
            NameMCCommand.sendMessage("Please specify a player.");
        }
        try {
            uuid = PlayerUtil.getUUIDFromName(commands[0]);
        } catch (Exception e) {
            NameMCCommand.sendMessage("An error occured.");
            return;
        }
        try {
            names = PlayerUtil.getHistoryOfNames(uuid);
        } catch (Exception e) {
            NameMCCommand.sendMessage("An error occured.");
            return;
        }
        if (names != null) {
            NameMCCommand.sendMessage(commands[0] + "s name history:");
            for (String name : names) {
                NameMCCommand.sendMessage(name);
            }
        } else {
            NameMCCommand.sendMessage("No names found.");
        }
    }
}