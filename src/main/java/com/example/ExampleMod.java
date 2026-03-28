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
        // 1. Register Attack Key (G)
        KeyBinding secondAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.attack2", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_G, 
            "PvP Binds"
        ));

        // 2. Register Bucket Key (Q)
        KeyBinding bucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.bucket_switch", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_Q, 
            "PvP Binds"
        ));

        // 3. Logic Loop
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Attack Logic
            if (secondAttack.isPressed()) {
                client.options.attackKey.setPressed(true);
            }

            // Bucket Logic
            if (bucketKey.isPressed()) {
                pressTicks++;
                wasPressed = true;
                if (pressTicks > 5) {
                    client.player.getInventory().selectedSlot = 4; // Slot 5
                }
            } else if (wasPressed) {
                if (pressTicks <= 5) {
                    client.player.getInventory().selectedSlot = 3; // Slot 4
                }
                pressTicks = 0;
                wasPressed = false;
            }
        });
    }
}
