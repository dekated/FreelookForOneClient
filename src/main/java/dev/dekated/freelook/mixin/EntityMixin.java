package dev.dekated.freelook.mixin;

import dev.dekated.freelook.FreelookMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * While freelook is active, the local player's mouse turn is diverted into the
 * free camera instead of rotating the player, so movement, aim and the rotation
 * sent to the server stay put.
 */
@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "updateLocalPlayerCamera(FF)V", at = @At("HEAD"), cancellable = true)
    private void freelook$divert(float yaw, float pitch, CallbackInfo ci) {
        FreelookMod fl = FreelookMod.instance();
        if (!fl.active) return;
        if ((Entity) (Object) this != Minecraft.getInstance().getCamera()) return;
        fl.applyLook(yaw, pitch);
        ci.cancel();
    }
}
