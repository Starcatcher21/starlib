package com.github.starcatcher21.starlib.mechanics.star;

import com.github.starcatcher21.starlib.RegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;

public class FallingObjectsList {
    public static final Codec<FallingObjectsList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Level.RESOURCE_KEY_CODEC.fieldOf("world").forGetter(FallingObjectsList::getWorld),
            RegistryFileCodec.create(RegistryKeys.FALLING_OBJECTS, FallingObject.CODEC).listOf()
                    .fieldOf("objects")
                    .forGetter(FallingObjectsList::getIdList),
            ExtraCodecs.POSITIVE_INT.listOf().fieldOf("chances").forGetter(FallingObjectsList::getChanceList),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("light").forGetter(FallingObjectsList::getLightLevel),
            FallingObjectDayState.CODEC.optionalFieldOf("daystate").forGetter(FallingObjectsList::getDayState)
    ).apply(instance, FallingObjectsList::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FallingObjectsList> PACKET_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static List<FallingObjectsList> list2 = new ArrayList<>();

    public List<Holder<FallingObject>> idList;
    public List<FallingObject> list;
    public ResourceKey<Level> world;
    public List<Integer> chanceList;
    public List<FallingObject> weightedList;
    public int lightLevel = 15;
    public FallingObjectDayState dayState = FallingObjectDayState.Both;

    public FallingObjectsList(ResourceKey<Level> world, List<Holder<FallingObject>> idList, List<Integer> chanceList, Optional<Integer> light, Optional<FallingObjectDayState> dstate) {
        this.idList = idList;
        this.list = idList.stream().map(sas -> sas.value()).toList();

        this.world = world;
        this.chanceList = chanceList;
        this.weightedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            FallingObject cur = list.get(i);
            if (i > chanceList.size()) {
                this.weightedList.add(cur);
            } else {
                Integer a = chanceList.get(i);
                for (int w = 0; w < a; w++) {
                    this.weightedList.add(cur);
                }
            }
        }
        light.ifPresent(integer -> this.lightLevel = integer);
        dstate.ifPresent(s -> this.dayState = s);
        list2.add(this);
    }

    private List<Holder<FallingObject>> getIdList() {
        return this.idList;
    }
    private List<FallingObject> getList() {
        return this.list;
    }
    private ResourceKey<Level> getWorld() {
        return this.world;
    }
    private List<Integer> getChanceList() {
        return this.chanceList;
    }
    private Optional<Integer> getLightLevel() {
        return Optional.of(this.lightLevel);
    }
    private Optional<FallingObjectDayState> getDayState() {
        return Optional.of(this.dayState);
    }

}
