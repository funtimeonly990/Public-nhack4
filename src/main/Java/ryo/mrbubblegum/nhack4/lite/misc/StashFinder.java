package ryo.mrbubblegum.nhack4.lite.misc;

import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ryo.mrbubblegum.nhack4.impl.util.PlayerUtil;
import ryo.mrbubblegum.nhack4.impl.util.Timer;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.command.Command;
import ryo.mrbubblegum.nhack4.system.setting.Setting;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class StashFinder
        extends Module {
    private static final String pathSave = "nhack4/StashLogger.txt";
    public Setting<Integer> amount = this.register(new Setting<Integer>("Amount", 15, 1, 100));
    public Setting<Boolean> hoppers = this.register(new Setting<Boolean>("Hoppers", true));
    public Setting<Boolean> shulkers = this.register(new Setting<Boolean>("Shulkers", true));
    public Setting<Boolean> dispensers = this.register(new Setting<Boolean>("Dispensers", true));
    public Setting<Boolean> droppers = this.register(new Setting<Boolean>("Droppers", true));
    public Setting<Boolean> chests = this.register(new Setting<Boolean>("Chests", true));
    public Setting<Boolean> windowsAlert = this.register(new Setting<Boolean>("WindowsAlert", true));
    public Setting<Boolean> sound = this.register(new Setting<Boolean>("Sound", true));
    public Setting<Boolean> chatMessage = this.register(new Setting<Boolean>("ChatMessage", true));
    private final Timer timer = new Timer();
    private final HashMap<Chunk, ArrayList<TileEntity>> map = new HashMap();
    private final ArrayList<Chunk> loggedChunks = new ArrayList();

    public StashFinder() {
        super("StashFinder", "dpsociety module", Module.Category.MISC, true, false, false);
    }

    public static void sendWindowsAlert(String message) {
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("System tray icon demo");
            tray.add(trayIcon);
            trayIcon.displayMessage("Loader", message, TrayIcon.MessageType.INFO);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public void onDisable() {
        this.loggedChunks.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (this.timer.passedMs(500L) && StashFinder.mc.player != null && StashFinder.mc.world != null && StashFinder.mc.world.loadedEntityList != null) {
            this.timer.reset();
            this.map.clear();
            for (TileEntity tileEntity : StashFinder.mc.world.loadedTileEntityList) {
                if (!this.isValid(tileEntity)) continue;
                Chunk chunk = StashFinder.mc.world.getChunk(tileEntity.getPos());
                ArrayList<TileEntity> list = new ArrayList<TileEntity>();
                if (this.map.containsKey(chunk)) {
                    list = this.map.get(chunk);
                }
                list.add(tileEntity);
                this.map.put(chunk, list);
            }
            for (Chunk chunk : this.map.keySet()) {
                if (this.map.get(chunk).size() < this.amount.getValue() || this.loggedChunks.contains(chunk))
                    continue;
                this.loggedChunks.add(chunk);
                this.log(chunk, this.map.get(chunk));
            }
        }
    }

    public void log(Chunk chunk, ArrayList<TileEntity> list) {
        if (list.size() <= 0) {
            return;
        }
        int x = list.get(0).getPos().getX();
        int z = list.get(0).getPos().getZ();
        if (this.chatMessage.getValue().booleanValue()) {
            Command.sendMessage("Stash located with " + list.size() + " on X: " + x + " Z: " + z);
        }
        if (this.sound.getValue().booleanValue()) {
            StashFinder.mc.world.playSound(PlayerUtil.getPlayerPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 100.0f, 18.0f, true);
        }
        if (this.windowsAlert.getValue().booleanValue()) {
            StashFinder.sendWindowsAlert("Found the stem!");
        }
        new Thread(() -> {
            try {
                File file = new File(pathSave);
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime now = LocalDateTime.now();
                bw.write("X: " + x + " Z: " + z + " Found " + list.size() + " container blocks - " + dtf.format(now));
                bw.newLine();
                bw.close();
            } catch (Exception e) {
                System.out.println(" - Error logging chunk. StashLogger");
                e.printStackTrace();
            }
        }).start();
    }

    public boolean isValid(TileEntity tileEntity) {
        return this.chests.getValue() && tileEntity instanceof TileEntityChest || this.droppers.getValue() && tileEntity instanceof TileEntityDropper || this.dispensers.getValue() && tileEntity instanceof TileEntityDispenser || this.shulkers.getValue() && tileEntity instanceof TileEntityShulkerBox || this.hoppers.getValue() && tileEntity instanceof TileEntityDropper;
    }
}

