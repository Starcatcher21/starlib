package com.github.starcatcher21.starlib.neoforge;

import com.github.starcatcher21.starlib.RegistryKeys;
import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

public class RegistryKeysImpl {

    public static void init() {
    }

    public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(RegistryKeys.FALLING_OBJECTS, FallingObject.CODEC);
        event.dataPackRegistry(RegistryKeys.COBBLE_GEN, CobbleGen.CODEC);
        event.dataPackRegistry(RegistryKeys.FALLING_OBJECTS_LIST, FallingObjectsList.CODEC);
    }
}