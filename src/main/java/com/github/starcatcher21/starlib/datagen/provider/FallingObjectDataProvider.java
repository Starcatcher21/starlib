package com.github.starcatcher21.starlib.datagen.provider;

import com.github.starcatcher21.starlib.mechanics.star.FallingObject;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
//? if >=26.1 {
import net.minecraft.resources.Identifier;
//? } else {
/*import net.minecraft.resources.ResourceLocation;
*///? }

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FallingObjectDataProvider implements DataProvider {

    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public FallingObjectDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.output = output;
        this.registriesFuture = registriesFuture;
    }

    //? if >= 26.1 {
    protected void addFallingObject(BiConsumer<Identifier, FallingObject> exporter, HolderLookup.Provider registries) {}
    //? } else {
    /*protected void addFallingObject(BiConsumer<ResourceLocation, FallingObject> exporter, HolderLookup.Provider registries) {}
    *///? }

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.registriesFuture.thenCompose(registries -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            var pathResolver = this.output.createPathProvider(
                    PackOutput.Target.DATA_PACK,
                    "starlib/falling/objects"
            );

            addFallingObject((id, fallingObject) -> {
                Path path = pathResolver.json(id);

                JsonElement json = FallingObject.CODEC.encodeStart(registries.createSerializationContext(JsonOps.INSTANCE), fallingObject)
                        .getOrThrow(IllegalStateException::new);

                futures.add(DataProvider.saveStable(writer, json, path));
                }, registries);

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    @Override
    public String getName() {
        return "Stargazer Falling Objects Generator";
    }
}
