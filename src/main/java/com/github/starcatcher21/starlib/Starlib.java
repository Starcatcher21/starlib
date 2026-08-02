package com.github.starcatcher21.starlib;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starlib implements ModInitializer {
	public static final String MOD_ID = "starlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void main(String[] string) {}

	public void onInitialize() {
		RegistryKeys.init();
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			var registryManager = server.registryAccess();

			Registry<CobbleGen> cobbleGens = registryManager.lookupOrThrow(RegistryKeys.COBBLE_GEN);
			Registry<FallingObject> fallingObjects = registryManager.lookupOrThrow(RegistryKeys.FALLING_OBJECTS);
			Registry<FallingObjectsList> fallingObjectsList = registryManager.lookupOrThrow(RegistryKeys.FALLING_OBJECTS_LIST);

			LOGGER.info("Loaded: " + cobbleGens.keySet().size() + " Cobble Gens");
			LOGGER.info("Loaded: " + fallingObjects.keySet().size() + " Falling Objects");
			LOGGER.info("Loaded: " + fallingObjectsList.keySet().size() + " Falling Objects Lists");
		});
		ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) -> {
			var registryManager = server.registryAccess();

			Registry<CobbleGen> cobbleGens = registryManager.lookupOrThrow(RegistryKeys.COBBLE_GEN);
			Registry<FallingObject> fallingObjects = registryManager.lookupOrThrow(RegistryKeys.FALLING_OBJECTS);
			Registry<FallingObjectsList> fallingObjectsList = registryManager.lookupOrThrow(RegistryKeys.FALLING_OBJECTS_LIST);

			LOGGER.info("Loaded: " + cobbleGens.keySet().size() + " Cobble Gens");
			LOGGER.info("Loaded: " + fallingObjects.keySet().size() + " Falling Objects");
			LOGGER.info("Loaded: " + fallingObjectsList.keySet().size() + " Falling Objects Lists");
		});
	}
}
