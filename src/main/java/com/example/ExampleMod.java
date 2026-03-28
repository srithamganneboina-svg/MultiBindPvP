package com.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer {
    private int pressTicks = 0;
    private boolean wasPressed = false;

    @Override
    public void onInitialize() {
        // Attack Bind (G)
        KeyBinding secondAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.attack2", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_G, 
            "PvP Binds"
        ));

        // Bucket Bind (Q)
        KeyBinding bucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.bucket_switch", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_Q, 
            "PvP Binds"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Handle G - Attack
            if (secondAttack.isPressed()) {
                client.options.attackKey.setPressed(true);
            }

            // Handle Q - Tap for Slot 4, Hold for Slot 5
            if (bucketKey.isPressed()) {
                pressTicks++;
                wasPressed = true;
                if (pressTicks > 5) { 
                    client.player.getInventory().selectedSlot = 4; 
                }
            } else if (wasPressed) {
                if (pressTicks <= 5) { 
                    client.player.getInventory().selectedSlot = 3; 
                }
                pressTicks = 0;
                wasPressed = false;
            }
        });
    }
}
