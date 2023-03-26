package ryo.mrbubblegum.nhack4.injections.mixins.accessors;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = {EntityRenderer.class})
public interface IEntityRenderer {
    @Invoker(value = "setupCameraTransform")
    void setupCameraTransformInvoker(float var1, int var2);
}