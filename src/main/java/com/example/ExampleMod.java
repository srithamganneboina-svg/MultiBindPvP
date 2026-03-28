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

    @Override
    public void onInitialize() {
        // This handles the "main" entrypoint
    }

    @Override
    public void onInitializeClient() {
        // This handles the "client" entrypoint (PvP Binds)
        KeyBinding attackG = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.attack2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "PvP"));
        KeyBinding bucketQ = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.bucket", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Q, "PvP"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (attackG.isPressed()) client.options.attackKey.setPressed(true);

            if (bucketQ.isPressed()) {
                ticks++;
                pressed = true;
                if (ticks > 5) client.player.getInventory().selectedSlot = 4; // Slot 5
            } else if (pressed) {
                if (ticks <= 5) client.player.getInventory().selectedSlot = 3; // Slot 4
                ticks = 0;
                pressed = false;
            }
        });
    }
}
