package com.github.starcatcher21.starlib.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public abstract class CapeMixin {
    @Shadow
    @Final
    private HumanoidModel<AvatarRenderState> model;

    @Shadow
    protected abstract boolean hasLayer(ItemStack stack, EquipmentClientInfo.LayerType layerType);

    @Inject(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void captureAndModify(
            PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, AvatarRenderState playerEntityRenderState, float f, float g, CallbackInfo ci         // 1. Captured directly from the method arguments
    ) {
        if (!playerEntityRenderState.isInvisible && playerEntityRenderState.showCape) {
            PlayerSkin skinTextures = playerEntityRenderState.skin;
            if (skinTextures.cape() != null) {
                if (!this.hasLayer(playerEntityRenderState.chestEquipment, EquipmentClientInfo.LayerType.WINGS)) {
                    matrixStack.pushPose();
                    if (this.hasLayer(playerEntityRenderState.chestEquipment, EquipmentClientInfo.LayerType.HUMANOID)) {
                        matrixStack.translate(0.0F, -0.053125F, 0.06875F);
                    }
                    orderedRenderCommandQueue.submitModel(
                            this.model,
                            playerEntityRenderState,
                            matrixStack,
                            RenderTypes.entityTranslucent(skinTextures.cape().texturePath()),
                            i,
                            OverlayTexture.NO_OVERLAY,
                            playerEntityRenderState.outlineColor,
                            null
                    );
                    matrixStack.popPose();
                }
            }
            ci.cancel();
        }
    }
}
