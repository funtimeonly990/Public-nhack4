package ryo.mrbubblegum.nhack4.lite.misc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryo.mrbubblegum.nhack4.impl.manager.FileManager;
import ryo.mrbubblegum.nhack4.impl.util.MathUtil;
import ryo.mrbubblegum.nhack4.impl.util.Timer;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.lite.combat.AutoCrystal;
import ryo.mrbubblegum.nhack4.loader.Loader;
import ryo.mrbubblegum.nhack4.system.command.Command;
import ryo.mrbubblegum.nhack4.system.setting.Setting;
import ryo.mrbubblegum.nhack4.world.events.DeathEvent;
import ryo.mrbubblegum.nhack4.world.events.PacketEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AutoGG
        extends Module {
    private static final String path = "nhack4/autogg.txt";
    private final Setting<Boolean> onOwnDeath = this.register(new Setting<Boolean>("OwnDeath", false));
    private final Setting<Boolean> greentext = this.register(new Setting<Boolean>("Greentext", false));
    private final Setting<Boolean> loadFiles = this.register(new Setting<Boolean>("LoadFiles", false));
    private final Setting<Integer> targetResetTimer = this.register(new Setting<Integer>("Reset", 30, 0, 90));
    private final Setting<Integer> delay = this.register(new Setting<Integer>("Delay", 5, 0, 30));
    private final Setting<Boolean> test = this.register(new Setting<Boolean>("Test", false));
    private final Timer timer = new Timer();
    private final Timer cooldownTimer = new Timer();
    public Map<EntityPlayer, Integer> targets = new ConcurrentHashMap<EntityPlayer, Integer>();
    public List<String> messages = new ArrayList<String>();
    public EntityPlayer cauraTarget;
    private boolean cooldown;

    public AutoGG() {
        super("AutoGG", "Automatically GGs", Module.Category.MISC, true, false, true);
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEnable() {
        this.loadMessages();
        this.timer.reset();
        this.cooldownTimer.reset();
    }

    @Override
    public void onTick() {
        if (this.loadFiles.getValue().booleanValue()) {
            this.loadMessages();
            Command.sendMessage("<AutoGG> Loaded messages.");
            this.loadFiles.setValue(false);
        }
        if (AutoCrystal.target != null && this.cauraTarget != AutoCrystal.target) {
            this.cauraTarget = AutoCrystal.target;
        }
        if (this.test.getValue().booleanValue()) {
            this.announceDeath(AutoGG.mc.player);
            this.test.setValue(false);
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.passedS(this.delay.getValue().intValue()) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (AutoCrystal.target != null) {
            this.targets.put(AutoCrystal.target, (int) (this.timer.getPassedTimeMs() / 1000L));
        }
        this.targets.replaceAll((p, v) -> (int) (this.timer.getPassedTimeMs() / 1000L));
        for (EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.targetResetTimer.getValue()) continue;
            this.targets.remove(player);
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onEntityDeath(DeathEvent event) {
        if (this.targets.containsKey(event.player) && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
            this.targets.remove(event.player);
        }
        if (event.player == this.cauraTarget && !this.cooldown) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
        if (event.player == AutoGG.mc.player && this.onOwnDeath.getValue().booleanValue()) {
            this.announceDeath(event.player);
            this.cooldown = true;
        }
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !Loader.friendManager.isFriend(event.getEntityPlayer())) {
            this.targets.put((EntityPlayer) event.getTarget(), 0);
        }
    }

    @SubscribeEvent
    public void onSendAttackPacket(PacketEvent.Send event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(AutoGG.mc.world) instanceof EntityPlayer && !Loader.friendManager.isFriend((EntityPlayer) packet.getEntityFromWorld(AutoGG.mc.world))) {
            this.targets.put((EntityPlayer) packet.getEntityFromWorld(AutoGG.mc.world), 0);
        }
    }

    public void loadMessages() {
        this.messages = FileManager.readTextFileAllLines(path);
    }

    public String getRandomMessage() {
        this.loadMessages();
        Random rand = new Random();
        if (this.messages.size() == 0) {
            return "<player> GET NHACK4 DAWG";
        }
        if (this.messages.size() == 1) {
            return this.messages.get(0);
        }
        return this.messages.get(MathUtil.clamp(rand.nextInt(this.messages.size()), 0, this.messages.size() - 1));
    }

    public void announceDeath(EntityPlayer target) {
        AutoGG.mc.player.connection.sendPacket(new CPacketChatMessage((this.greentext.getValue() ? ">" : "") + this.getRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
    }
}

