package com.github.starcatcher21.starlib;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.minecraft.core.Registry;
//? if >= 26.2 {
import net.minecraft.resources.Identifier;
//? } else {
/*import net.minecraft.resources.ResourceLocation;
*///? }
import net.minecraft.resources.ResourceKey;

public class RegistryKeys {
    //? if >= 26.2 {
    public static final ResourceKey<Registry<CobbleGen>> COBBLE_GEN =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "cobblegen"));

    public static final ResourceKey<Registry<FallingObject>> FALLING_OBJECTS =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "falling/objects"));

    public static final ResourceKey<Registry<FallingObjectsList>> FALLING_OBJECTS_LIST =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "falling/list"));
    //? } else {
    /*public static final ResourceKey<Registry<CobbleGen>> COBBLE_GEN =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Starlib.MOD_ID, "cobblegen"));

    public static final ResourceKey<Registry<FallingObject>> FALLING_OBJECTS =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Starlib.MOD_ID, "falling/objects"));

    public static final ResourceKey<Registry<FallingObjectsList>> FALLING_OBJECTS_LIST =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Starlib.MOD_ID, "falling/list"));
    *///? }

    public static void init() {
    }
}
