package com.github.starcatcher21.starlib.mixin;

import com.github.starcatcher21.starlib.mechanics.SkinManager;
import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import net.minecraft.client.multiplayer.PlayerInfo;

@Mixin(PlayerInfo.class)
public abstract class SkinMixin {
    private static ArrayList<String> uuids = new ArrayList<>();
    private static int timeout = 0;
    @Shadow
    @Final
    private GameProfile profile;

    @Inject(method = "getSkin", at = @At("HEAD"), cancellable = true)
    private void skins(CallbackInfoReturnable cir) {
        String uuid = this.profile.id().toString();

        if (SkinManager.CACHE.containsKey(uuid)) {
            cir.setReturnValue(SkinManager.CACHE.get(uuid));
        } else {
            if (timeout <= 0) {
                if (!uuids.contains(uuid)) {
                    SkinManager.fetchRemoteSkins();
                    uuids.add(uuid);
                    timeout = 12000;
                }
            } else {
                timeout -= 1;
            }
        }
    }
}
