package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.system.command.Command;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class BrowseCommand
        extends Command {
    Desktop desktop = Desktop.getDesktop();

    public BrowseCommand() {
        super("browse", new String[]{"text"});
    }

    @Override
    public void execute(String[] commands) {

        try {
            desktop.browse(new URI("https://www.google.com/search?q=" + URLEncoder.encode(commands[0]) + "&sxsrf=APq-WBs6H06yXya-qlggIMTKxjRwW7jvOA%3A1650656158846&ei=ngNjYo2nM4b_rgStiaO4BA&oq=sxsrf&gs_lcp=Cgdnd3Mtd2l6EAMYATIFCAAQgAQyBQgAEIAEMgUIABCABDIFCAAQgAQyBggAEAoQHjIGCAAQBRAeSgQIQRgASgQIRhgAUABYAGCmDWgAcAF4AIABVogBVpIBATGYAQCgAQKgAQHAAQE&sclient=gws-wiz"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}