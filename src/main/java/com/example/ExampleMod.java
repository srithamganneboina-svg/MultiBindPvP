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
        // 1. EXTRA ATTACK (G)
        KeyBinding secondAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.attack2", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_G, 
            "PvP Binds"
        ));

        // 2. BUCKET SWITCHER (Q)
        KeyBinding bucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.bucket_switch", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_Q, 
            "PvP Binds"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Attack Logic
            if (secondAttack.isPressed()) {
                client.options.attackKey.setPressed(true);
            }

            // Bucket Logic (TAP = Slot 4, HOLD = Slot 5)
            if (bucketKey.isPressed()) {
                pressTicks++;
                wasPressed = true;
                // If held for more than 5 ticks, switch to Slot 5 (Water)
                if (pressTicks > 5) {
                    client.player.getInventory().selectedSlot = 4; 
                }
            } else if (wasPressed) {
                // If released quickly, switch to Slot 4 (Lava)
                if (pressTicks <= 5) {
                    client.player.getInventory().selectedSlot = 3; 
                }
                pressTicks = 0;
                wasPressed = false;
            }
        });
    }
}
