package com.github.starcatcher21.starlib.mechanics.Generators;

import com.github.starcatcher21.starlib.Helpers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import static net.minecraft.world.level.block.LiquidBlock.POSSIBLE_FLOW_DIRECTIONS;

public class CobbleGen {
    public static final Codec<CobbleGen> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagKey.hashedCodec(Registries.FLUID).fieldOf("fluid").forGetter(CobbleGen::getFluid),
            BlockState.CODEC.fieldOf("lava").forGetter(CobbleGen::getLava),
            BlockState.CODEC.fieldOf("obsidian").forGetter(CobbleGen::getObs),
            BlockState.CODEC.fieldOf("cobble").forGetter(CobbleGen::getCobble)
    ).apply(instance, CobbleGen::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CobbleGen> PACKET_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    public static List<CobbleGen> list = new ArrayList<>();

    public final BlockState LAVA;
    public final BlockState BLOCKOBS;
    public final BlockState BLOCKCOBBLE;
    public final TagKey<Fluid> FLUID;

    private BlockState getCobble() {
        return this.BLOCKCOBBLE;
    }

    private BlockState getObs() {
        return this.BLOCKOBS;
    }

    private BlockState getLava() {
        return this.LAVA;
    }

    private TagKey<Fluid> getFluid() {
        return this.FLUID;
    }

    public CobbleGen(TagKey<Fluid> fluid, BlockState lava, BlockState Obs, BlockState Cobble) {
        LAVA = lava;
        BLOCKCOBBLE = Cobble;
        BLOCKOBS = Obs;
        FLUID = fluid;
        list.add(this);
    }

    public boolean gen(Level world, BlockPos pos) {
        for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
            if (Helpers.isColliding(LAVA.getBlock(), world, pos, direction)) {
                BlockPos blockPos = pos.relative(direction.getOpposite());
                if (world.getFluidState(blockPos).is(FLUID)) {
                    BlockState block = world.getFluidState(pos).isSource() ? BLOCKOBS : BLOCKCOBBLE;
                    if (direction == Direction.DOWN) {
                        BlockPos newPos = Helpers.getCollidingPosition(LAVA.getBlock(), world, pos, POSSIBLE_FLOW_DIRECTIONS);
                        world.setBlockAndUpdate(newPos, BLOCKOBS);
                    } else {
                        world.setBlockAndUpdate(pos, block);
                    }
                    return false;
                }
            }
        }
        if (world.getFluidState(pos).isSource() && world.getFluidState(pos).is(FLUID)) {
            if (Helpers.isCollidingAny(LAVA.getBlock(), world, pos, POSSIBLE_FLOW_DIRECTIONS)) {
                BlockState block = world.getFluidState(pos).isSource() ? BLOCKOBS : BLOCKCOBBLE;
                BlockPos newPos = Helpers.getCollidingPosition(LAVA.getBlock(), world, pos, POSSIBLE_FLOW_DIRECTIONS);
                world.setBlockAndUpdate(newPos, block);
                return false;
            }
        }
        return true;
    }
}
