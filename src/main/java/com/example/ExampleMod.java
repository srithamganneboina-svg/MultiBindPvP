package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer {
    private int pressTicks = 0;
    private boolean wasPressed = false;

    @Override
    public void onInitialize() {
        // G Key - Extra Attack
        KeyBinding secondAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.attack2", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_G, 
            "PvP Binds"
        ));

        // Q Key - Bucket Switcher (Tap for Slot 4, Hold for Slot 5)
        KeyBinding bucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.bucket_switch", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_Q, 
            "PvP Binds"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Attack Logic (G)
            if (secondAttack.isPressed()) {
                client.options.attackKey.setPressed(true);
            }

            // Bucket Logic (Q)
            if (bucketKey.isPressed()) {
                pressTicks++;
                wasPressed = true;
                // Hold longer than 5 ticks (approx 0.25s) for Slot 5
                if (pressTicks > 5) {
                    client.player.getInventory().selectedSlot = 4;
                }
            } else if (wasPressed) {
                // Quick Tap for Slot 4
                if (pressTicks <= 5) {
                    client.player.getInventory().selectedSlot = 3;
                }
                pressTicks = 0;
                wasPressed = false;
            }
        });
    }
}
