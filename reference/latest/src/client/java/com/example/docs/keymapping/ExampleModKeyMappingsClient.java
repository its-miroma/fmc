package com.example.docs.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import com.example.docs.ExampleMod;

public class ExampleModKeyMappingsClient implements ClientModInitializer {
	// #region category
	KeyMapping.Category CATEGORY = new KeyMapping.Category(
			Identifier.fromNamespaceAndPath(ExampleMod.MOD_ID, "custom_category")
	);
	// #endregion category

	// #region key-mapping
	KeyMapping sendToChatKey = KeyBindingHelper.registerKeyBinding(
		new KeyMapping(
			"key.example-mod.send_to_chat", // The translation key for the key mapping.
			InputConstants.Type.KEYSYM, // // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_J, // The GLFW keycode of the key.
			CATEGORY // The category of the mapping.
		));
	// #endregion key-mapping

	@Override
	public void onInitializeClient() {
		// #region client-tick-event
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (sendToChatKey.consumeClick()) {
				if (client.player != null) {
					client.player.displayClientMessage(Component.literal("Key Pressed!"), false);
				}
			}
		});
		// #endregion client-tick-event
	}
}
