package com.github.starcatcher21.starlib.fabric;

import com.github.starcatcher21.starlib.RegistryKeys;
import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class RegistryKeysImpl {
    public static void init() {
        DynamicRegistries.register(RegistryKeys.FALLING_OBJECTS, FallingObject.CODEC);
        DynamicRegistries.register(RegistryKeys.COBBLE_GEN, CobbleGen.CODEC);
        DynamicRegistries.register(RegistryKeys.FALLING_OBJECTS_LIST, FallingObjectsList.CODEC);
    }
}