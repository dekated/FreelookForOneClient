package dev.dekated.freelook.mixin;

import dev.dekated.freelook.FreelookMod;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "tick()V", at = @At("RETURN"))
    private void freelook$tick(CallbackInfo ci) {
        FreelookMod.instance().onClientTick((Minecraft) (Object) this);
    }
}
