package com.github.starcatcher21.starlib.worldgen.features.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class TreeConfig implements FeatureConfiguration {
    public final List<String> NAMES;
    public final List<BlockState> growOn;
    public final Boolean BLACKLIST;
    public static final Codec<TreeConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("blacklist").forGetter(config -> config.BLACKLIST),
            ExtraCodecs.NON_EMPTY_STRING.listOf().fieldOf("names").forGetter(config -> config.NAMES),
            ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf(0, Integer.MAX_VALUE)).fieldOf("grow_on").forGetter(config -> config.growOn)
    ).apply(instance, TreeConfig::new));

    public TreeConfig(boolean blacklist, List<String> NAMES, List<BlockState> grow) {
        this.BLACKLIST = blacklist;
        this.NAMES = NAMES;
        this.growOn = grow;
    }
}
