package com.github.starcatcher21.starlib.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mixin(SplashManager.class)
public class SplashTextMixin {
    @Unique
    private static final Style PORTAL_STYLE = Style.EMPTY.withColor(0x0000FF);
    @Unique
    private static final Style STAR_STYLE = Style.EMPTY.withColor(0xF0F0F0);
    @Unique
    private static final Style BLOOD_STYLE = Style.EMPTY.withColor(0x880808);
    @Shadow
    @Final
    private static Identifier SPLASHES_LOCATION;

    @Shadow
    @Final
    private static Style DEFAULT_STYLE;

    @Inject(method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;", at = @At("HEAD"), cancellable = true)
    private static void CustomSplashes(final ResourceManager manager, final ProfilerFiller profiler, CallbackInfoReturnable cir) {
        List<Component> og = new ArrayList<>();
        try (BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(SPLASHES_LOCATION)) {
            for (String s : reader.lines().map(String::trim).filter(line -> line.hashCode() != 125780783).toList()) {
                og.add(Component.literal(s).withStyle(DEFAULT_STYLE));
            }
        } catch (IOException ignored) {
        }
        og.add(Component.literal("Fatty Fatty no Parrents").withStyle(PORTAL_STYLE));
        og.add(Component.literal("I'm Still Alive").withStyle(PORTAL_STYLE));
        og.add(Component.literal("The cake is a lie").withStyle(PORTAL_STYLE));
        og.add(Component.literal("Stars are beautiful").withStyle(STAR_STYLE));
        og.add(Component.literal("Light the stars they sound like you").withStyle(STAR_STYLE));
        og.add(Component.literal("Shine so bright they always do").withStyle(STAR_STYLE));
        og.add(Component.literal("Carnival of Carnage").withStyle(BLOOD_STYLE));
        og.add(Component.literal("Bloody Eyes Looking For You").withStyle(BLOOD_STYLE));
        cir.setReturnValue(og);
    }
}
