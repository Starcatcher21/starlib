package com.github.starcatcher21.starlib;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class RegistryKeys {
    public static final ResourceKey<Registry<CobbleGen>> COBBLE_GEN =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "cobblegen"));

    public static final ResourceKey<Registry<FallingObject>> FALLING_OBJECTS =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "falling/objects"));

    public static final ResourceKey<Registry<FallingObjectsList>> FALLING_OBJECTS_LIST =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "falling/list"));

    @ExpectPlatform
    public static void init() {
        throw new AssertionError();
    }
}