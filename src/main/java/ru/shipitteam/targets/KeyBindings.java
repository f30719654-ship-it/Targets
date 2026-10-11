package ru.shipitteam.targets;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public final class KeyBindings {
    public static final KeyBinding TOGGLE_RECORDING = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                    "key.targets.toggle_recording",
                    InputUtil.Type.KEYSYM,
                    InputUtil.UNKNOWN_KEY.getCode(),
                    "category.targets.neuro_aura"
            )
    );

    public static final KeyBinding OPEN_GUI = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                    "key.targets.open_gui",
                    InputUtil.Type.KEYSYM,
                    InputUtil.UNKNOWN_KEY.getCode(),
                    "category.targets.neuro_aur a"
            )
    );

    public static void register() {
        // no-op, kept for explicit registration compatibility with Fabric
    }
}
