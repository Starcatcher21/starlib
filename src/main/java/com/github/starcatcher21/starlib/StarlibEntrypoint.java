package com.github.starcatcher21.starlib;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;

// ---------------------------------------------------------------------------
// Loader-specific imports
// ---------------------------------------------------------------------------
//? if fabric {
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
*///?} else if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
//? if < 26.3 {
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
//? } else {
/*import net.neoforged.neoforge.registries.NewDatapackRegistryEvent;
*///? }
//?} else if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataPackRegistryEvent;
*///?}

// ---------------------------------------------------------------------------
// Fabric
// ---------------------------------------------------------------------------
//? if fabric {
/*public class StarlibEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        // Common setup
        Starlib.init();

        // Register dynamic data-pack registries (Fabric API)
        DynamicRegistries.register(RegistryKeys.FALLING_OBJECTS,     FallingObject.CODEC);
        DynamicRegistries.register(RegistryKeys.COBBLE_GEN,          CobbleGen.CODEC);
        DynamicRegistries.register(RegistryKeys.FALLING_OBJECTS_LIST, FallingObjectsList.CODEC);
    }
}
*///?}

// ---------------------------------------------------------------------------
// NeoForge
// ---------------------------------------------------------------------------
//? if neoforge {
@Mod(Starlib.MOD_ID)
public class StarlibEntrypoint {

    public StarlibEntrypoint(IEventBus modEventBus) {
        // Datapack registries must be declared on the mod event bus
        modEventBus.addListener(StarlibEntrypoint::registerDatapackRegistries);

        // Common setup
        Starlib.init();
    }

    private static void registerDatapackRegistries(
            //? if < 26.3 {
            DataPackRegistryEvent.NewRegistry
            //? } else {
            /*NewDatapackRegistryEvent
            *///? }
                    event) {
        //? if < 26.3 {
        event.dataPackRegistry(RegistryKeys.FALLING_OBJECTS,     FallingObject.CODEC);
        event.dataPackRegistry(RegistryKeys.COBBLE_GEN,          CobbleGen.CODEC);
        event.dataPackRegistry(RegistryKeys.FALLING_OBJECTS_LIST, FallingObjectsList.CODEC);
        //? } else {
        /*event.worldRegistry(RegistryKeys.FALLING_OBJECTS,     FallingObject.CODEC);
        event.worldRegistry(RegistryKeys.COBBLE_GEN,          CobbleGen.CODEC);
        event.worldRegistry(RegistryKeys.FALLING_OBJECTS_LIST, FallingObjectsList.CODEC);
        *///? }
    }
}
//?}
