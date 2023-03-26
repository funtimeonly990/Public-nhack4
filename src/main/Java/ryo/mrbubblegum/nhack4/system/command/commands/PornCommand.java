package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.system.command.Command;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class PornCommand
        extends Command {
    Desktop desktop = Desktop.getDesktop();

    public PornCommand() {
        super("porn", new String[]{"type"});
    }

    @Override
    public void execute(String[] commands) {
        try {
            desktop.browse(new URI("https://www.pornhub.com/video/search?search=" + URLEncoder.encode(commands[0])));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}