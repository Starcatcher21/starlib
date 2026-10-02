package com.github.starcatcher21.starlib.mixin;

import com.github.starcatcher21.starlib.Starlib;
import com.github.starcatcher21.starlib.mechanics.Generators.CobbleGen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowingFluid.class)
public class FlowingFluidMixin {

    //? if < 1.21.2 {
    /*@Inject(
        method = "spreadTo",
        at = @At("HEAD"), 
        cancellable = true
    )
    private void onSpread(LevelAccessor level, BlockPos pos, BlockState state, net.minecraft.core.Direction direction, FluidState fluidState, CallbackInfo ci) {
        if (level instanceof net.minecraft.world.level.Level world) {
            for (CobbleGen gen : CobbleGen.list) {
                if (!gen.gen(world, pos)) {
                    ci.cancel();
                    return;
                }
            }
        }
    }
    *///? }
}
