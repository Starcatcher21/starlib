package com.github.starcatcher21.starlib.mechanics.star;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum FallingObjectDayState implements StringRepresentable {
    Day("day"),
    Night("night"),
    Both("both");

    private final String ID;

    public static final Codec<FallingObjectDayState> CODEC = StringRepresentable.fromEnum(() -> FallingObjectDayState.values());
    public static final StreamCodec<RegistryFriendlyByteBuf, FallingObjectDayState> PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, FallingObjectDayState::getSerializedName, FallingObjectDayState::newDayState
    );

    FallingObjectDayState(String id) {
        ID = id;
    }

    public static FallingObjectDayState newDayState(String id) {
        if (id.equals("day")) {
            return FallingObjectDayState.Day;
        } else if (id.equals("night")) {
            return FallingObjectDayState.Night;
        } else {
            return FallingObjectDayState.Both;
        }
    }

    @Override
    public String getSerializedName() {
        return this.ID;
    }

    private String getId() {
        return this.ID;
    }
}
