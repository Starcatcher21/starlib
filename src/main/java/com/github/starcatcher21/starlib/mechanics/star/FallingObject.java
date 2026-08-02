package com.github.starcatcher21.starlib.mechanics.star;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class FallingObject {
    private final static Random random = new Random();
    private static final int SPAWN_RANGE = 12;
    public static final Codec<FallingObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Item.CODEC.fieldOf("item").forGetter(FallingObject::getItem),
            BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("fall_particle").forGetter(FallingObject::getFallParticle),
            BuiltInRegistries.PARTICLE_TYPE.byNameCodec().fieldOf("hit_particle").forGetter(FallingObject::getHitParticle),
            ExtraCodecs.POSITIVE_INT.fieldOf("particle_amount").forGetter(FallingObject::getAmount),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("velocity").forGetter(FallingObject::getVelocity)
    ).apply(instance, FallingObject::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, FallingObject> PACKET_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

    public static List<FallingObject> list = new ArrayList<>();

    private ParticleType<?> getHitParticle() {
        return this.hitParticle;
    }

    private ParticleType<?> getFallParticle() {
        return this.fallParticle;
    }

    private Float getVelocity() {
        return this.velocity;
    }

    private Integer getAmount() {
        return this.amount;
    }

    private Holder<Item> getItem() {
        return this.item;
    }

    public final Holder<Item> item;
    public final int amount;
    public final float velocity;
    public final ParticleType<?> fallParticle;
    public final ParticleType<?> hitParticle;

    public FallingObject(Holder<Item> itemConstruct, ParticleType<?> fallParticleType, ParticleType<?> hitParticleType, int particleAmount, float particleVelocity) {
        item = itemConstruct;
        amount = particleAmount;
        velocity = particleVelocity;
        fallParticle = fallParticleType;
        hitParticle = hitParticleType;
        list.add(this);
    }

    public void hitParticles(Level world, int X, int Y, int Z) {
        if (world instanceof ServerLevel sw) {
            sw.sendParticles((SimpleParticleType) hitParticle, X + 0.5D, Y + 0.5D, Z + 0.5D, 30, -velocity + random.nextDouble(velocity * 2d), -velocity + random.nextDouble(velocity * 2d), -velocity + random.nextDouble(velocity * 2d), 0.06);
        }
        for (int i = 0; i < 30; i++) {
            world.addParticle((SimpleParticleType) hitParticle, true, true, X + 0.5F, Y + 0.5F, Z + 0.5F, -velocity + random.nextFloat(velocity * 2), -velocity + random.nextFloat(velocity * 2), -velocity + random.nextFloat(velocity * 2));
        }
    }

    public void fallParticles(Level world, int X, int Y, int Z) {
        if (world instanceof ServerLevel sw) {
            sw.sendParticles((SimpleParticleType) fallParticle, X + 0.5F, Y + 200 + 0.5F, Z + 0.5F, amount, 0.0, -amount / 4.0F, 0.0, 0.06);
        }
        for (int i = 1; i <= amount; i++) {
            world.addParticle((SimpleParticleType) fallParticle, true, true, X + 0.5F, Y + 200 + 0.5F, Z + 0.5F, 0.0, -amount / 4.0F, 0.0);
        }
    }

    public void spawn(Level world, int X, int Y, int Z) {
        fallParticles(world, X, Y, Z);
        hitParticles(world, X, Y, Z);
        if (world instanceof ServerLevel serverWorld) {
            Entity entity = new ItemEntity(serverWorld, X + 0.5, Y + 0.5, Z + 0.5, new ItemStack(item));
            serverWorld.addFreshEntity(entity);
            serverWorld.playSound(null, X, Y, Z, SoundEvents.BAMBOO_FALL, SoundSource.AMBIENT, 1.0f, 1.0f);
        }
    }

    public int airEnd(Level world, BlockPos pos) {
        if (world.isEmptyBlock(pos)) {
            if (!(world.isEmptyBlock(pos.below(1)))) {
                return pos.getY();
            } else {
                return airEnd(world, pos.below(1));
            }
        } else {
            return airEnd(world, pos.above(1));
        }
    }

    public void spawn(Level world, Player player) {
        int X = (int) player.getX() - SPAWN_RANGE + random.nextInt(SPAWN_RANGE * 2);
        int Y = (int) player.getY();
        int Z = (int) player.getZ() - SPAWN_RANGE + random.nextInt(SPAWN_RANGE * 2);
        Y = airEnd(world, new BlockPos(X, Y, Z));
        spawn(world, X, Y, Z);
    }

    public void spawn(Level world, BlockPos player) {
        int X = (int) player.getX() - SPAWN_RANGE + random.nextInt(SPAWN_RANGE * 2);
        int Y = (int) player.getY();
        int Z = (int) player.getZ() - SPAWN_RANGE + random.nextInt(SPAWN_RANGE * 2);
        Y = airEnd(world, new BlockPos(X, Y, Z));
        spawn(world, X, Y, Z);
    }
}
