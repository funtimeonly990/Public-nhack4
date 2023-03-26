package ryo.mrbubblegum.nhack4.lite.player;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import ryo.mrbubblegum.nhack4.impl.util.Util;
import ryo.mrbubblegum.nhack4.lite.Feature;
import ryo.mrbubblegum.nhack4.lite.Module;

public class NoEntityTrace
        extends Module {
    private boolean focus = false;

    public NoEntityTrace() {
        super("NoEntityTrace", "mines trought entities", Module.Category.PLAYER, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (Feature.nullCheck()) return;
        Util.mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .filter(entity -> Util.mc.player == entity)
                .map(entity -> (EntityLivingBase) entity)
                .filter(entity -> !(entity.isDead))
                .forEach(this::process);
        RayTraceResult normalResult = Util.mc.objectMouseOver;
        if (normalResult != null) {
            focus = normalResult.typeOfHit == RayTraceResult.Type.ENTITY;
        }
    }

    private void process(EntityLivingBase event) {
        RayTraceResult bypassEntityResult = event.rayTrace(6, Util.mc.getRenderPartialTicks());
        if (bypassEntityResult != null && focus) {
            if (bypassEntityResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = bypassEntityResult.getBlockPos();

                if (Util.mc.gameSettings.keyBindAttack.isKeyDown()) {
                    Util.mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
                }
            }
        }
    }
}