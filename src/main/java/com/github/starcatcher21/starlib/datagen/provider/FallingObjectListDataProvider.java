package com.github.starcatcher21.starlib.datagen.provider;

import com.github.starcatcher21.starlib.mechanics.star.FallingObjectsList;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FallingObjectListDataProvider implements DataProvider {

    private final FabricPackOutput output;
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public FallingObjectListDataProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        this.output = output;
        this.registriesFuture = registriesFuture;
    }

    protected void addFallingObjectList(BiConsumer<Identifier, FallingObjectsList> exporter, HolderLookup.Provider registries) {

    }


    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        return this.registriesFuture.thenCompose(registries -> {
            List<CompletableFuture<?>> futures = new ArrayList<>();
            var pathResolver = this.output.createPathProvider(
                    PackOutput.Target.DATA_PACK,
                    "starlib/falling/list"
            );

            addFallingObjectList((id, fallingObject) -> {
                Path path = pathResolver.json(id);

                futures.add(DataProvider.saveStable(writer, registries, FallingObjectsList.CODEC, fallingObject, path));
            }, registries);

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    @Override
    public String getName() {
        return "Stargazer Falling Objects List Generator";
    }
}