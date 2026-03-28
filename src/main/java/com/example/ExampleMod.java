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
        // --- FUNCTION 1: EXTRA ATTACK BUTTON (Default: G) ---
        KeyBinding secondAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.attack2", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_G, 
            "PvP Binds"
        ));

        // --- FUNCTION 2: UHC BUCKET SWITCHER (Default: Q) ---
        KeyBinding bucketKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.bucket_switch", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_Q, 
            "PvP Binds"
        ));

        // This part handles the "Logic" every tick (20 times a second)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // 1. If you hold G, it attacks
            if (secondAttack.isPressed()) {
                client.options.attackKey.setPressed(true);
            }

            // 2. If you interact with Q (Tap = Lava, Hold = Water)
            if (bucketKey.isPressed()) {
                pressTicks++;
                wasPressed = true;
                if (pressTicks > 5) { 
                    // Switches to Slot 5 (Water)
                    client.player.getInventory().selectedSlot = 4; 
                }
            } else if (wasPressed) {
                if (pressTicks <= 5) { 
                    // Switches to Slot 4 (Lava)
                    client.player.getInventory().selectedSlot = 3; 
                }
                pressTicks = 0;
                wasPressed = false;
            }
        });
    }
}
