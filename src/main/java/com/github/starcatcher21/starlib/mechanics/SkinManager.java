package com.github.starcatcher21.starlib.mechanics;

import com.github.starcatcher21.starlib.Starlib;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import java.io.IOException;
import java.net.Proxy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SkinManager {
    public static final Map<String, PlayerSkin> CACHE = new HashMap<>();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();

    // Explicitly instantiation allowed in 1.21.11 via TextureManager & single thread execution
    private static final SkinTextureDownloader pstd = new SkinTextureDownloader(
            Proxy.NO_PROXY,
            Minecraft.getInstance().getTextureManager(),
            Minecraft.getInstance()
    );

    public static void fetchRemoteSkins() {
        String url = "https://starcatcher21.github.io/minecraft/skins.json";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        Map<String, PlayerEntry> data = GSON.fromJson(json, new TypeToken<Map<String, PlayerEntry>>(){}.getType());
                        if (data != null) {
                            data.forEach(SkinManager::registerPlayer);
                        }
                    } catch (Exception e) {
                        Starlib.LOGGER.error("Failed to parse remote skins JSON", e);
                    }
                });
    }

    private static void registerPlayer(String uuid, PlayerEntry entry) {
        Path cacheDir = FabricLoader.getInstance().getGameDir().resolve("cache/custom_skins");
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            Starlib.LOGGER.error("Failed to create skin cache directory", e);
            return;
        }

        Identifier skinIdentifier = Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "skins/" + uuid.toLowerCase());
        Identifier capeIdentifier = Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "capes/" + uuid.toLowerCase());

        CompletableFuture<ClientAsset.Texture> skinFuture = pstd.downloadAndRegisterSkin(
                skinIdentifier,
                cacheDir.resolve(uuid + "_skin.png"),
                entry.skin,
                false // remap legacy skins
        );

        CompletableFuture<ClientAsset.Texture> capeFuture = (entry.cape != null && !entry.cape.isEmpty()) ?
                pstd.downloadAndRegisterSkin(capeIdentifier, cacheDir.resolve(uuid + "_cape.png"), entry.cape, false) :
                CompletableFuture.completedFuture(null);

        PlayerModelType model = (entry.model == 1) ? PlayerModelType.SLIM : PlayerModelType.WIDE;

        CompletableFuture.runAsync(() -> {
            PlayerSkin textures = new PlayerSkin(
                    skinFuture.join(),    // Skin Asset
                    capeFuture.join(),    // Cape Aasset
                    capeFuture.join(),    // Elytra Asset
                    model,                // PlayerSkinType enum
                    true                  // Secure flag
            );

            CACHE.put(uuid, textures);
        });
    }

    public static class PlayerEntry {
        public String skin, cape;
        public int model;
    }
}