package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.impl.util.PlayerUtil;
import ryo.mrbubblegum.nhack4.system.command.Command;

import java.util.List;
import java.util.UUID;

public class HistoryCommand
        extends Command {
    public HistoryCommand() {
        super("history", new String[]{"<player>"});
    }

    @Override
    public void execute(String[] commands) {
        List<String> names;
        UUID uuid;
        if (commands.length == 1 || commands.length == 0) {
            HistoryCommand.sendMessage("Please specify a player.");
        }
        try {
            uuid = PlayerUtil.getUUIDFromName(commands[0]);
        } catch (Exception e) {
            HistoryCommand.sendMessage("An error occured.");
            return;
        }
        try {
            names = PlayerUtil.getHistoryOfNames(uuid);
        } catch (Exception e) {
            HistoryCommand.sendMessage("An error occured.");
            return;
        }
        if (names != null) {
            HistoryCommand.sendMessage(commands[0] + " name history:");
            for (String name : names) {
                HistoryCommand.sendMessage(name);
            }
        } else {
            HistoryCommand.sendMessage("No names found.");
        }
    }
}

