package ryo.mrbubblegum.nhack4.lite.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ryo.mrbubblegum.nhack4.impl.util.Timer;
import ryo.mrbubblegum.nhack4.lite.Module;
import ryo.mrbubblegum.nhack4.system.command.Command;
import ryo.mrbubblegum.nhack4.system.setting.Setting;
import ryo.mrbubblegum.nhack4.world.events.PacketEvent;

public class AntiUnicode
        extends Module {
    private final Timer delay = new Timer();
    public Setting<Integer> maxSymbolCount = this.register(new Setting<Integer>("MaxSymbolCount", 100, 1, 250));
    public Setting<Boolean> notify = this.register(new Setting<Boolean>("Notify", true));

    public AntiUnicode() {
        super("AntiPopLag", "prevents u from getting popLagged", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketChat) {
            String text = ((SPacketChat) event.getPacket()).chatComponent.getFormattedText();
            int symbolCount = 0;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (this.isSymbol(c)) symbolCount++;
                if (symbolCount > this.maxSymbolCount.getValue().intValue()) {
                    if (this.notify.getValue().booleanValue() && this.delay.passed(10)) {
                        Command.sendMessage("[AntiUnicode] " + ChatFormatting.GREEN + "Message blocked!");
                        this.delay.reset();
                    }
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    private boolean isSymbol(char charIn) {
        return !((charIn >= 65 && charIn <= 90) || (charIn >= 97 && charIn <= 122)) && !(charIn >= 48 && charIn <= 57);
    }
}