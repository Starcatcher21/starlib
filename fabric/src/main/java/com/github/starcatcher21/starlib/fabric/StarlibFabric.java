package com.github.starcatcher21.starlib.fabric;

import com.github.starcatcher21.starlib.Starlib;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarlibFabric implements ModInitializer {
	public static final String MOD_ID = "starlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public void onInitialize() {
		Starlib.init();
	}
}
