package com.github.starcatcher21.starlib;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class RegistryKeys {
    public static final ResourceKey<Registry<CobbleGen>> COBBLE_GEN = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "cobblegen"));

    public static final ResourceKey<Registry<FallingObject>> FALLING_OBJECTS = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "falling/objects"));

    public static final ResourceKey<Registry<FallingObjectsList>> FALLING_OBJECTS_LIST = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "falling/list"));


    public static void init() {
        DynamicRegistries.<FallingObject>register(
                FALLING_OBJECTS,
                FallingObject.CODEC
        );
        DynamicRegistries.<CobbleGen>register(
                COBBLE_GEN,
                CobbleGen.CODEC
        );
        DynamicRegistries.<FallingObjectsList>register(
                FALLING_OBJECTS_LIST,
                FallingObjectsList.CODEC
        );
    }
}