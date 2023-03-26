package ryo.mrbubblegum.nhack4.system.command.commands;

import ryo.mrbubblegum.nhack4.system.command.Command;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class OpenFolderCommand
        extends Command {
    public OpenFolderCommand() {
        super("openfolder", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        try {
            Desktop.getDesktop().open(new File("nhack4/"));
            Command.sendMessage("Opened config folder!", false);
        } catch (IOException e) {
            Command.sendMessage("Could not open config folder!", false);
            e.printStackTrace();
        }
    }
}