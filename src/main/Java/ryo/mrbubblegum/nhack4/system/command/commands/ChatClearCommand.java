package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.system.command.Command;

public class ChatClearCommand
        extends Command {
    public ChatClearCommand() {
        super("chatclear", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        mc.ingameGUI.getChatGUI().clearChatMessages(true);
    }
}