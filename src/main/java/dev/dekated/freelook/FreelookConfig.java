package dev.dekated.freelook;

import java.util.function.Consumer;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Info;
import org.polyfrost.oneconfig.api.config.v1.annotations.Keybind;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.OneConfigKeybind;
import org.polyfrost.oneconfig.internal.legacy.InputConstants;

/**
 * The OneConfig settings page for Freelook. Shows in OneClient's mod list with
 * the usual Enabled / Reset Mod controls.
 */
public class FreelookConfig extends Config {

    @Info(title = "Made by @wuwster", description = "Freelook for OneClient")
    public boolean credit = true;

    @Keybind(title = "Freelook Key", description = "Hold or toggle this to look around your player.")
    public OneConfigKeybind key = KeybindHelper.builder()
        .key(InputConstants.KEY_LALT)
        .action((Consumer<Boolean>) down -> FreelookMod.instance().onKey(down))
        .build();

    @Dropdown(title = "Mode", description = "Hold: active only while the key is held. Toggle: press to start, press again to stop.", options = {"hold", "toggle"})
    public String mode = "hold";

    @Dropdown(title = "Perspective", description = "Where the camera sits while looking around.", options = {"behind", "in front"})
    public String perspective = "behind";

    @Slider(title = "Sensitivity", description = "How fast the camera turns, on top of your mouse sensitivity.", min = 0.25f, max = 3.0f, step = 0.05f)
    public float sensitivity = 1.0f;

    @Switch(title = "Invert Yaw", description = "Swap left and right while looking around.")
    public boolean invertYaw = false;

    @Switch(title = "Invert Pitch", description = "Swap up and down while looking around.")
    public boolean invertPitch = false;

    public FreelookConfig() {
        super("freelook.json", "Freelook", Category.VISUALS);
    }
}
