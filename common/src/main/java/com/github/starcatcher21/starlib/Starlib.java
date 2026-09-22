package com.github.starcatcher21.starlib;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.core.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Starlib {
	public static final String MOD_ID = "starlib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void init() {
		// 1. Initialize your custom registries
		RegistryKeys.init();

		// 2. Register server startup event using Architectury's cross-platform event API
		LifecycleEvent.SERVER_STARTED.register(server -> {
			var registryManager = server.registryAccess();

			Registry<CobbleGen> cobbleGens = registryManager.lookupOrThrow(RegistryKeys.COBBLE_GEN);
			Registry<FallingObject> fallingObjects = registryManager.lookupOrThrow(RegistryKeys.FALLING_OBJECTS);
			Registry<FallingObjectsList> fallingObjectsList = registryManager.lookupOrThrow(RegistryKeys.FALLING_OBJECTS_LIST);

			LOGGER.info("Loaded: " + cobbleGens.keySet().size() + " Cobble Gens");
			LOGGER.info("Loaded: " + fallingObjects.keySet().size() + " Falling Objects");
			LOGGER.info("Loaded: " + fallingObjectsList.keySet().size() + " Falling Objects Lists");
		});

		// Note: For data pack reloads, NeoForge/Forge handles reloading via ResourceReloaders
		// rather than a direct server reload event. If you need reload-time logic across both
		// platforms, consider using Architectury's ReloadListenerRegistry instead.
	}
}
