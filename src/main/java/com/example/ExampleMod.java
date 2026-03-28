package com.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "multibindpvp";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("MultiBind PvP Mod Initialized!");

        // Register a second attack key (set to 'G' by default)
        KeyBinding secondAttack = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.multi_bind.attack2", 
            InputUtil.Type.KEYSYM, 
            GLFW.GLFW_KEY_G, 
            "category.multi_bind.pvp"
        ));

        // Every tick, if G is held, force the game to "Attack"
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && secondAttack.isPressed()) {
                client.options.attackKey.setPressed(true);
            }
        });
    }
}
