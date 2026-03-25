package com.moigferdsrte.windchimes;

import com.moigferdsrte.windchimes.register.BlocksRegistry;
import com.moigferdsrte.windchimes.register.ItemRegistry;
import com.moigferdsrte.windchimes.register.SoundRegistry;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WindChimes implements ModInitializer {
	public static final String MOD_ID = "windchimes";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SoundRegistry.register();
		BlocksRegistry.Blocks.register();
		BlocksRegistry.BlockEntityTypes.register();
		ItemRegistry.register();
		LOGGER.info("[Wind chimes]");
	}
}