package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ExampleMod implements ModInitializer, ClientModInitializer {
    private int ticks = 0;
    private boolean pressed = false;

    // This handles the "Main" entrypoint
    @Override
    public void onInitialize() {
        System.out.println("PvP Mod Initialized!");
    }

    // This handles the "Client" entrypoint (The actual Binds)
    @Override
    public void onInitializeClient() {
        // Registers the G and Q keys in your Options > Controls menu
        KeyBinding attackG = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.attack2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "PvP Binds"));
        
        KeyBinding bucketQ = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.bucket", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Q, "PvP Binds"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // G Key: Auto-Attack Logic
            if (attackG.isPressed()) {
                client.options.attackKey.setPressed(true);
            }

            // Q Key: Multi-Slot Logic
            if (bucketQ.isPressed()) {
                ticks++;
                pressed = true;
                // If held for more than 5 ticks (0.25s), switch to Slot 5
                if (ticks > 5) {
                    client.player.getInventory().selectedSlot = 4; 
                }
            } else if (pressed) {
                // If it was just a quick tap (less than 5 ticks), switch to Slot 4
                if (ticks <= 5) {
                    client.player.getInventory().selectedSlot = 3; 
                }
                ticks = 0;
                pressed = false;
            }
        });
    }
}
