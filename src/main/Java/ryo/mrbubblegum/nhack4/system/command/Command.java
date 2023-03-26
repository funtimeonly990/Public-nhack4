package ryo.mrbubblegum.nhack4.system.command;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import ryo.mrbubblegum.nhack4.lite.Feature;
import ryo.mrbubblegum.nhack4.loader.Loader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command
        extends Feature {
    protected String name;
    protected String[] commands;

    public Command(String name) {
        super(name);
        this.name = name;
        this.commands = new String[]{""};
    }

    public Command(String name, String[] commands) {
        super(name);
        this.name = name;
        this.commands = commands;
    }

    public static void sendMessage(String message, boolean notification) {
        Command.sendSilentMessage(Loader.commandManager.getClientMessage() + " " + "\u00a7r" + message);
        if (notification) {
            Loader.notificationManager.addNotification(message, 3000L);
        }
    }

    public static void sendMessage(String message) {
        Command.sendSilentMessage(Loader.commandManager.getClientMessage() + " " + "\u00a7r" + message);
    }

    public static void sendSilentMessage(String message) {
        if (Command.nullCheck()) {
            return;
        }
        Command.mc.player.sendMessage(new ChatMessage(message));
    }

    public static void sendOverwriteMessage(String message, int id, boolean notification) {
        TextComponentString component = new TextComponentString(message);
        Command.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
        if (notification) {
            Loader.notificationManager.addNotification(message, 3000L);
        }
    }

    public static void sendRainbowMessage(String message) {
        StringBuilder stringBuilder = new StringBuilder(message);
        stringBuilder.insert(0, "\u00a7+");
        Command.mc.player.sendMessage(new ChatMessage(stringBuilder.toString()));
    }

    public static String getCommandPrefix() {
        return Loader.commandManager.getPrefix();
    }

    public abstract void execute(String[] var1);

    @Override
    public String getName() {
        return this.name;
    }

    public String[] getCommands() {
        return this.commands;
    }

    public static class ChatMessage
            extends TextComponentBase {
        private final String text;

        public ChatMessage(String text) {
            Pattern pattern = Pattern.compile("&[0123456789abcdefrlosmk]");
            Matcher matcher = pattern.matcher(text);
            StringBuffer stringBuffer = new StringBuffer();
            while (matcher.find()) {
                String replacement = "\u00a7" + matcher.group().substring(1);
                matcher.appendReplacement(stringBuffer, replacement);
            }
            matcher.appendTail(stringBuffer);
            this.text = stringBuffer.toString();
        }

        public String getUnformattedComponentText() {
            return this.text;
        }

        public ITextComponent createCopy() {
            return new ChatMessage(this.text);
        }
    }
}

