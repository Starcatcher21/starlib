package com.github.starcatcher21.starlib.mechanics;

import com.github.starcatcher21.starlib.Starlib;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
//? if >= 26.1 {
import net.minecraft.client.renderer.texture.SkinTextureDownloader;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
//? } else {
/*import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
*///? }

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

    //? if >= 26.1 {
    private static final SkinTextureDownloader pstd = new SkinTextureDownloader(
            Proxy.NO_PROXY,
            Minecraft.getInstance().getTextureManager(),
            Minecraft.getInstance()
    );
    //? }

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
        Path cacheDir = Minecraft.getInstance().gameDirectory.toPath().resolve("cache/custom_skins");
        try {
            Files.createDirectories(cacheDir);
        } catch (IOException e) {
            Starlib.LOGGER.error("Failed to create skin cache directory", e);
            return;
        }

        //? if >= 26.1 {
        Identifier skinIdentifier = Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "skins/" + uuid.toLowerCase());
        Identifier capeIdentifier = Identifier.fromNamespaceAndPath(Starlib.MOD_ID, "capes/" + uuid.toLowerCase());
        //? } else {
        /*ResourceLocation skinIdentifier = ResourceLocation.fromNamespaceAndPath(Starlib.MOD_ID, "skins/" + uuid.toLowerCase());
        ResourceLocation capeIdentifier = ResourceLocation.fromNamespaceAndPath(Starlib.MOD_ID, "capes/" + uuid.toLowerCase());
        *///? }

        //? if >= 26.1 {
        CompletableFuture<ClientAsset.Texture> skinFuture = pstd.downloadAndRegisterSkin(
                skinIdentifier,
                cacheDir.resolve(uuid + "_skin.png"),
                entry.skin,
                false
        );
        CompletableFuture<ClientAsset.Texture> capeFuture = (entry.cape != null && !entry.cape.isEmpty()) ?
                pstd.downloadAndRegisterSkin(capeIdentifier, cacheDir.resolve(uuid + "_cape.png"), entry.cape, false) :
                CompletableFuture.completedFuture(null);
        PlayerModelType model = (entry.model == 1) ? PlayerModelType.SLIM : PlayerModelType.WIDE;
        //? } else {
        /*ResourceLocation skinTexture = skinIdentifier;
        ResourceLocation capeTexture = capeIdentifier;

        try {
            Path skinFile = cacheDir.resolve(uuid + "_skin.png");
            if (!Files.exists(skinFile) && entry.skin != null && !entry.skin.isEmpty()) {
                downloadFile(entry.skin, skinFile);
            }

            if (Files.exists(skinFile)) {
                NativeImage skinImage = NativeImage.read(Files.newInputStream(skinFile));
                Minecraft.getInstance().getTextureManager().register(
                        skinTexture,
                        new DynamicTexture(skinImage)
                );
            }

            Path capeFile = cacheDir.resolve(uuid + "_cape.png");
            if (entry.cape != null && !entry.cape.isEmpty() && !Files.exists(capeFile)) {
                downloadFile(entry.cape, capeFile);
            }

            if (entry.cape != null && !entry.cape.isEmpty() && Files.exists(capeFile)) {
                NativeImage capeImage = NativeImage.read(Files.newInputStream(capeFile));
                Minecraft.getInstance().getTextureManager().register(
                        capeTexture,
                        new DynamicTexture(capeImage)
                );
            }
        } catch (IOException e) {
            Starlib.LOGGER.error("Failed to register skin textures", e);
            return;
        }

        PlayerSkin.Model model = (entry.model == 1) ? PlayerSkin.Model.SLIM : PlayerSkin.Model.WIDE;
        *///? }

        CompletableFuture.runAsync(() -> {
            //? if >= 26.1 {
            PlayerSkin textures = new PlayerSkin(
                    skinFuture.join(),
                    capeFuture.join(),
                    capeFuture.join(),
                    model,
                    true
            );
            //? } else {
            /*PlayerSkin textures = new PlayerSkin(
                    skinTexture,
                    null,
                    capeTexture,
                    capeTexture,
                    model,
                    true
            );
            *///? }

            CACHE.put(uuid, textures);
        });
    }

    //? if < 26.1 {
    /*private static void downloadFile(String url, Path destination) throws IOException {
        if (url == null || url.isEmpty()) return;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<Path> response;
        try {
            response = CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofFile(destination)
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Files.deleteIfExists(destination);
            throw new IOException("Interrupted while downloading skin from " + url, e);
        }

        if (response.statusCode() != 200) {
            Files.deleteIfExists(destination);
            throw new IOException("Failed to download skin from " + url
                    + ": HTTP " + response.statusCode());
        }
    }
    *///? }

    public static class PlayerEntry {
        public String skin, cape;
        public int model;
    }
}
