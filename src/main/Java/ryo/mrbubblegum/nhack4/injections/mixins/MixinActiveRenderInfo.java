package ryo.mrbubblegum.nhack4.injections.mixins;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ryo.mrbubblegum.nhack4.impl.util.RenderUtil;

@Mixin(value = {ActiveRenderInfo.class})
public class MixinActiveRenderInfo {
    @Inject(method = {"updateRenderInfo(Lnet/minecraft/entity/Entity;Z)V"}, at = {@At(value = "HEAD")}, remap = false)
    private static void updateRenderInfo(Entity entity, boolean wtf, CallbackInfo ci) {
        RenderUtil.updateModelViewProjectionMatrix();
    }
}

