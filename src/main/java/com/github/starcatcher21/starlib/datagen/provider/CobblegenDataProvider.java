package com.github.starcatcher21.starlib.datagen.provider;

import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
//? if >=26.2 {
import net.minecraft.resources.Identifier;
//? } else {
/*import net.minecraft.resources.ResourceLocation;
*///? }

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class CobblegenDataProvider implements DataProvider {

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public CobblegenDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.output = output;
        this.registriesFuture = registriesFuture;
    }

    //? if >= 26.2 {
    protected abstract void addCobblegen(BiConsumer<Identifier, CobbleGen> exporter, HolderLookup.Provider registries);
    //? } else {
    /*protected abstract void addCobblegen(BiConsumer<ResourceLocation, CobbleGen> exporter, HolderLookup.Provider registries);
    *///? }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.registriesFuture.thenCompose(registries -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            var pathResolver = this.output.createPathProvider(
                    PackOutput.Target.DATA_PACK,
                    "starlib/cobblegen"
            );

            addCobblegen((id, cobbleGen) -> {
                Path path = pathResolver.json(id);
                futures.add(DataProvider.saveStable(writer, registries, CobbleGen.CODEC, cobbleGen, path));
            }, registries);

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    @Override
    public String getName() {
        return "Stargazer Cobblegen Generator";
    }
}
