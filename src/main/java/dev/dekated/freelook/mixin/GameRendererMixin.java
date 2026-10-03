package dev.dekated.freelook.mixin;

import dev.dekated.freelook.FreelookMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Builds the world camera from the free-look angles while active, then restores
 * the player's real rotation immediately so the player model and everything
 * drawn afterwards still use it.
 */
@Mixin(net.minecraft.client.render.GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    private Minecraft minecraft;

    @Unique private boolean freelook$swapped;
    @Unique private float freelook$yaw, freelook$pitch, freelook$lastYaw, freelook$lastPitch;

    @Inject(method = "transformCamera(F)V", at = @At("HEAD"))
    private void freelook$preCamera(float tickDelta, CallbackInfo ci) {
        FreelookMod fl = FreelookMod.instance();
        freelook$swapped = false;
        if (!fl.active) return;
        Entity cam = this.minecraft.getCamera();
        if (cam == null) return;
        freelook$yaw = cam.yaw;
        freelook$pitch = cam.pitch;
        freelook$lastYaw = cam.lastYaw;
        freelook$lastPitch = cam.lastPitch;
        cam.yaw = fl.camYaw;
        cam.pitch = fl.camPitch;
        cam.lastYaw = fl.prevCamYaw;
        cam.lastPitch = fl.prevCamPitch;
        freelook$swapped = true;
    }

    @Inject(method = "transformCamera(F)V", at = @At("RETURN"))
    private void freelook$postCamera(float tickDelta, CallbackInfo ci) {
        if (!freelook$swapped) return;
        freelook$swapped = false;
        Entity cam = this.minecraft.getCamera();
        if (cam == null) return;
        cam.yaw = freelook$yaw;
        cam.pitch = freelook$pitch;
        cam.lastYaw = freelook$lastYaw;
        cam.lastPitch = freelook$lastPitch;
    }
}
