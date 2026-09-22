package com.github.starcatcher21.starlib.neoforge;

import com.github.starcatcher21.starlib.Starlib;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Starlib.MOD_ID)
public final class StarlibNeoForge {
    public StarlibNeoForge(IEventBus modEventBus) {
        modEventBus.addListener(RegistryKeysImpl::onNewRegistry);
        Starlib.init();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // General setup...
    }
}
