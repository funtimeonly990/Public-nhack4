package ryo.mrbubblegum.nhack4.lite.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryo.mrbubblegum.nhack4.impl.manager.FileManager;
import ryo.mrbubblegum.nhack4.impl.util.TextUtil;
import ryo.mrbubblegum.nhack4.impl.util.Timer;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.lite.hud.ModuleTools;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.command.Command;
import ryo.mrbubblegum.nhack4.system.setting.Setting;
import ryo.mrbubblegum.nhack4.world.events.ClientEvent;
import ryo.mrbubblegum.nhack4.world.events.PacketEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notifications
        extends Module {
    private static final String fileName = "nhack4/util/ModuleMessage_List.txt";
    private static final List<String> modules = new ArrayList<>();
    private static Notifications INSTANCE = new Notifications();
    private final Timer timer = new Timer();
    public Setting<Boolean> totemPops = this.register(new Setting<>("TotemPops", true));
    public Setting<Boolean> totemNoti = this.register(new Setting<Object>("TotemNoti", Boolean.FALSE, v -> this.totemPops.getValue()));
    public Setting<Integer> delay = this.register(new Setting<Object>("Delay", 200, 0, 5000, v -> this.totemPops.getValue(), "Delays messages."));
    public Setting<Boolean> clearOnLogout = this.register(new Setting<>("LogoutClear", false));
    public Setting<Boolean> moduleMessage = this.register(new Setting<>("ModuleMessage", true));
    private final Setting<Boolean> readfile = this.register(new Setting<Object>("LoadFile", Boolean.FALSE, v -> this.moduleMessage.getValue()));
    public Setting<Boolean> list = this.register(new Setting<Object>("List", Boolean.FALSE, v -> this.moduleMessage.getValue()));
    public Setting<Boolean> visualRange = this.register(new Setting<>("VisualRange", false));
    public Setting<Boolean> VisualRangeSound = this.register(new Setting<>("VisualRangeSound", false));
    public Setting<Boolean> coords = this.register(new Setting<Object>("Coords", Boolean.TRUE, v -> this.visualRange.getValue()));
    public Setting<Boolean> leaving = this.register(new Setting<Object>("Leaving", Boolean.FALSE, v -> this.visualRange.getValue()));
    public Setting<Boolean> pearls = this.register(new Setting<>("PearlNotifs", false));
    public Setting<Boolean> crash = this.register(new Setting<>("Crash", false));
    public Setting<Boolean> popUp = this.register(new Setting<>("PopUpVisualRange", false));
    public Timer totemAnnounce = new Timer();
    private List<EntityPlayer> knownPlayers = new ArrayList<>();
    private boolean check;

    public Notifications() {
        super("Notifications", "notifications dawg", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    public static Notifications getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Notifications();
        }
        return INSTANCE;
    }

    public static void displayCrash(Exception e) {
        Command.sendMessage("\u00a7cException caught: " + e.getMessage());
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        this.check = true;
        this.loadFile();
        this.check = false;
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<>();
        if (!this.check) {
            this.loadFile();
        }
    }

    @Override
    public void onUpdate() {
        if (this.readfile.getValue()) {
            if (!this.check) {
                Command.sendMessage("Loading File...");
                this.timer.reset();
                this.loadFile();
            }
            this.check = true;
        }
        if (this.check && this.timer.passedMs(750L)) {
            this.readfile.setValue(false);
            this.check = false;
        }
        if (this.visualRange.getValue()) {
            ArrayList<EntityPlayer> tickPlayerList = new ArrayList<>(Notifications.mc.world.playerEntities);
            if (tickPlayerList.size() > 0) {
                for (EntityPlayer player : tickPlayerList) {
                    if (player.getName().equals(Notifications.mc.player.getName()) || this.knownPlayers.contains(player))
                        continue;
                    this.knownPlayers.add(player);
                    if (Loader.friendManager.isFriend(player)) {
                        Command.sendMessage("Player \u00a7a" + player.getName() + "\u00a7r" + " entered your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                    } else {
                        Command.sendMessage("Player \u00a7c" + player.getName() + "\u00a7r" + " entered your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                    }
                    if (this.VisualRangeSound.getValue()) {
                        Notifications.mc.player.playSound(SoundEvents.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                    }
                    return;
                }
            }
            if (this.knownPlayers.size() > 0) {
                for (EntityPlayer player : this.knownPlayers) {
                    if (tickPlayerList.contains(player)) continue;
                    this.knownPlayers.remove(player);
                    if (this.leaving.getValue()) {
                        if (Loader.friendManager.isFriend(player)) {
                            Command.sendMessage("Player \u00a7a" + player.getName() + "\u00a7r" + " left your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                        } else {
                            Command.sendMessage("Player \u00a7c" + player.getName() + "\u00a7r" + " left your visual range" + (this.coords.getValue() ? " at (" + (int) player.posX + ", " + (int) player.posY + ", " + (int) player.posZ + ")!" : "!"), this.popUp.getValue());
                        }
                    }
                    return;
                }
            }
        }
    }

    public void loadFile() {
        List<String> fileInput = FileManager.readTextFileAllLines(fileName);
        Iterator<String> i = fileInput.iterator();
        modules.clear();
        while (i.hasNext()) {
            String s = i.next();
            if (s.replaceAll("\\s", "").isEmpty()) continue;
            modules.add(s);
        }
    }

    @SubscribeEvent
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject && this.pearls.getValue()) {
            SPacketSpawnObject packet = event.getPacket();
            EntityPlayer player = Notifications.mc.world.getClosestPlayer(packet.getX(), packet.getY(), packet.getZ(), 1.0, false);
            if (player == null) {
                return;
            }
            if (packet.getEntityID() == 85) {
                Command.sendMessage("\u00a7cPearl thrown by " + player.getName() + " at X:" + (int) packet.getX() + " Y:" + (int) packet.getY() + " Z:" + (int) packet.getZ());
            }
        }
    }


    public TextComponentString getNotifierOn(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case NHACK4: {
                    TextComponentString text = new TextComponentString((Loader.commandManager.getClientMessage()) + " " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RESET + ChatFormatting.GREEN + " enabled.");
                    return text;
                }
                case FUTURE: {
                    TextComponentString text = new TextComponentString(ChatFormatting.RED + "[Future] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.GREEN + "on" + ChatFormatting.GRAY + ".");
                    return text;
                }
                case DOTGOD: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.GREEN + "enabled.");
                    return text;
                }
                case SNOW: {
                    TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + ChatFormatting.AQUA + "Snow" + ChatFormatting.BLUE + "] [" + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.BLUE + "] " + ChatFormatting.GREEN + "enabled");
                    return text;
                }
                case WEATHER: {
                    TextComponentString text = new TextComponentString(ChatFormatting.AQUA + "[" + ChatFormatting.AQUA + "Weather" + ChatFormatting.AQUA + "] " + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.WHITE + " was toggled " + ChatFormatting.GREEN + "on.");
                    return text;
                }
                case CATALYST: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_GRAY + "[" + ChatFormatting.AQUA + "Catalyst" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.GRAY + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.GREEN + " ON");
                    return text;
                }
                case RUSHERHACK: {
                    TextComponentString text = new TextComponentString(ChatFormatting.WHITE + "[" + ChatFormatting.GREEN + "rusherhack" + ChatFormatting.WHITE + "] " + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.WHITE + " has been enabled");
                    return text;
                }
                case KONAS: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_GRAY + "[" + ChatFormatting.LIGHT_PURPLE + "Konas" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.WHITE + " has been enabled");
                    return text;
                }
                case LEGACY: {
                    TextComponentString text = new TextComponentString(ChatFormatting.WHITE + "[" + ChatFormatting.LIGHT_PURPLE + "Legacy" + ChatFormatting.WHITE + "] " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.GREEN + " enabled.");
                    return text;
                }
                case EUROPA: {
                    TextComponentString text = new TextComponentString(ChatFormatting.GRAY + "[" + ChatFormatting.RED + "Europa" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.GREEN + ChatFormatting.BOLD + " Enabled!");
                    return text;
                }
                case PYRO: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_RED + "[" + ChatFormatting.DARK_RED + "Pyro" + ChatFormatting.DARK_RED + "] " + ChatFormatting.GREEN + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.GREEN + " has been enabled.");
                    return text;
                }
                case MUFFIN: {
                    TextComponentString text = new TextComponentString(ChatFormatting.LIGHT_PURPLE + "[" + ChatFormatting.DARK_PURPLE + "Muffin" + ChatFormatting.LIGHT_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + module.getDisplayName() + ChatFormatting.DARK_PURPLE + " was " + ChatFormatting.GREEN + "enabled.");
                    return text;
                }
                case ABYSS: {
                    TextComponentString text = new TextComponentString(TextUtil.coloredString("[Abyss] ", ModuleTools.getInstance().abyssColor.getPlannedValue()) + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.GREEN + " ON");
                    return text;
                }
                case LUIGIHACK: {
                    TextComponentString text = new TextComponentString(ChatFormatting.GREEN + "[LuigiHack] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.GREEN + "on" + ChatFormatting.GRAY + ".");
                    return text;
                }
            }
        }
        TextComponentString text = new TextComponentString(Loader.commandManager.getClientMessage() + " " + ChatFormatting.GREEN + module.getDisplayName() + " toggled on.");
        return text;
    }

    public TextComponentString getNotifierOff(Module module) {
        if (ModuleTools.getInstance().isEnabled()) {
            switch (ModuleTools.getInstance().notifier.getValue()) {
                case NHACK4: {
                    TextComponentString text = new TextComponentString((Loader.commandManager.getClientMessage()) + " " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.RESET + ChatFormatting.RED + " disabled.");
                    return text;
                }
                case FUTURE: {
                    TextComponentString text = new TextComponentString(ChatFormatting.RED + "[Future] " + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.RED + "off" + ChatFormatting.GRAY + ".");
                    return text;
                }
                case DOTGOD: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + "DotGod.CC" + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.RED + "disabled.");
                    return text;
                }
                case SNOW: {
                    TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + ChatFormatting.AQUA + "Snow" + ChatFormatting.BLUE + "] [" + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.BLUE + "] " + ChatFormatting.RED + "disabled");
                    return text;
                }
                case WEATHER: {
                    TextComponentString text = new TextComponentString(ChatFormatting.AQUA + "[" + ChatFormatting.AQUA + "Weather" + ChatFormatting.AQUA + "] " + ChatFormatting.DARK_AQUA + module.getDisplayName() + ChatFormatting.WHITE + " was toggled " + ChatFormatting.RED + "off.");
                    return text;
                }
                case CATALYST: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_GRAY + "[" + ChatFormatting.AQUA + "Catalyst" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.GRAY + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.RED + " OFF");
                    return text;
                }
                case RUSHERHACK: {
                    TextComponentString text = new TextComponentString(ChatFormatting.WHITE + "[" + ChatFormatting.GREEN + "rusherhack" + ChatFormatting.WHITE + "] " + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.WHITE + " has been disabled");
                    return text;
                }
                case LEGACY: {
                    TextComponentString text = new TextComponentString(ChatFormatting.WHITE + "[" + ChatFormatting.LIGHT_PURPLE + "Legacy" + ChatFormatting.WHITE + "] " + ChatFormatting.BOLD + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.RED + " disabled.");
                    return text;
                }
                case KONAS: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_GRAY + "[" + ChatFormatting.LIGHT_PURPLE + "Konas" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.WHITE + " has been disabled");
                    return text;
                }
                case EUROPA: {
                    TextComponentString text = new TextComponentString(ChatFormatting.GRAY + "[" + ChatFormatting.RED + "Europa" + ChatFormatting.GRAY + "] " + ChatFormatting.RESET + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.RED + ChatFormatting.BOLD + " Disabled!");
                    return text;
                }
                case PYRO: {
                    TextComponentString text = new TextComponentString(ChatFormatting.DARK_RED + "[" + ChatFormatting.DARK_RED + "Pyro" + ChatFormatting.DARK_RED + "] " + ChatFormatting.RED + module.getDisplayName() + ChatFormatting.LIGHT_PURPLE + "" + ChatFormatting.RED + " has been disabled.");
                    return text;
                }
                case MUFFIN: {
                    TextComponentString text = new TextComponentString(ChatFormatting.LIGHT_PURPLE + "[" + ChatFormatting.DARK_PURPLE + "Muffin" + ChatFormatting.LIGHT_PURPLE + "] " + ChatFormatting.LIGHT_PURPLE + module.getDisplayName() + ChatFormatting.DARK_PURPLE + " was " + ChatFormatting.RED + "disabled.");
                    return text;
                }
                case ABYSS: {
                    TextComponentString text = new TextComponentString(TextUtil.coloredString("[Abyss] ", ModuleTools.getInstance().abyssColor.getPlannedValue()) + ChatFormatting.WHITE + module.getDisplayName() + ChatFormatting.RED + " OFF");
                    return text;
                }
                case LUIGIHACK: {
                    TextComponentString text = new TextComponentString(ChatFormatting.GREEN + ("[LuigiHack] ") + ChatFormatting.GRAY + module.getDisplayName() + " toggled " + ChatFormatting.RED + "off" + ChatFormatting.GRAY + ".");
                    return text;
                }
            }
        }
        TextComponentString text = new TextComponentString(Loader.commandManager.getClientMessage() + " " + ChatFormatting.RED + module.getDisplayName() + " toggled off.");
        return text;
    }

    @SubscribeEvent
    public void onToggleModule(ClientEvent event) {
        int moduleNumber;
        Module module;
        if (!this.moduleMessage.getValue()) {
            return;
        }
        if (!(event.getStage() != 0 || (module = (Module) event.getFeature()).equals(this) || !modules.contains(module.getDisplayName()) && this.list.getValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(getNotifierOff(module), moduleNumber);
        }

        if (event.getStage() == 1 && (modules.contains((module = (Module) event.getFeature()).getDisplayName()) || !this.list.getValue())) {
            moduleNumber = 0;
            for (char character : module.getDisplayName().toCharArray()) {
                moduleNumber += character;
                moduleNumber *= 10;
            }
            Notifications.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(getNotifierOn(module), moduleNumber);
        }
    }
}
