package dev.dekated.freelook;

import java.util.Objects;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

/**
 * Freelook entry point and shared state.
 *
 * <p>The keybind (declared in {@link FreelookConfig}) calls {@link #onKey(boolean)}
 * on press and release, setting {@link #requested}. Each client tick
 * {@link #onClientTick} turns that into {@link #active} (gated on being in a
 * world with no screen open) and forces third person. While active, the Entity
 * mixin diverts the mouse into {@link #camYaw}/{@link #camPitch} instead of the
 * player, and the GameRenderer mixin builds the world camera from those angles,
 * so the camera looks around while the player keeps facing, moving and aiming
 * the way it was.</p>
 */
public class FreelookMod implements ClientModInitializer {
    private static FreelookMod instance;

    public static FreelookMod instance() {
        return Objects.requireNonNull(instance, "Freelook not initialized");
    }

    public FreelookConfig config;

    /** The user is asking to look around (key held, or toggled on). */
    public volatile boolean requested;
    /** The camera is actually detached and looking around. */
    public boolean active;
    /** Where the free camera looks, and its previous-tick values for interpolation. */
    public float camYaw, camPitch, prevCamYaw, prevCamPitch;
    /** The perspective to restore when freelook ends. */
    private int savedPerspective;

    @Override
    public void onInitializeClient() {
        if (instance != null) throw new IllegalStateException("Freelook has already been initialized!");
        instance = this;
        config = new FreelookConfig();
    }

    /** Called by the keybind on press (down=true) and release (down=false). */
    public void onKey(boolean down) {
        if (toggling()) {
            if (down) requested = !requested;
        } else {
            requested = down;
        }
    }

    /** Called at the end of every client tick (via the Minecraft mixin). */
    public void onClientTick(Minecraft mc) {
        boolean inWorld = mc.player != null && mc.world != null && mc.screen == null;
        if (requested && inWorld && !active) {
            camYaw = prevCamYaw = mc.player.yaw;
            camPitch = prevCamPitch = mc.player.pitch;
            savedPerspective = mc.options.perspective;
            mc.options.perspective = wantFront() ? 2 : 1;
            active = true;
        } else if (active && (!requested || !inWorld)) {
            mc.options.perspective = savedPerspective;
            active = false;
            if (!inWorld) requested = false;
        } else if (active) {
            int want = wantFront() ? 2 : 1;
            if (mc.options.perspective != want) mc.options.perspective = want;
        }
    }

    /** Called by the Entity mixin with the raw mouse deltas while active. */
    public void applyLook(float deltaYaw, float deltaPitch) {
        float sens = config.sensitivity;
        prevCamYaw = camYaw;
        prevCamPitch = camPitch;
        camYaw += (float) (deltaYaw * 0.15) * sens * (config.invertYaw ? -1.0F : 1.0F);
        camPitch -= (float) (deltaPitch * 0.15) * sens * (config.invertPitch ? -1.0F : 1.0F);
        if (camPitch < -90.0F) camPitch = -90.0F;
        if (camPitch > 90.0F) camPitch = 90.0F;
    }

    public boolean toggling() {
        return "toggle".equals(config.mode);
    }

    public boolean wantFront() {
        return "in front".equals(config.perspective);
    }
}
