package com.qiqikanna.interactivelist;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InteractiveList implements ModInitializer {
	public static final String MOD_ID = "interactive-list";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's content.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("交互列表模组正在加载");
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}
