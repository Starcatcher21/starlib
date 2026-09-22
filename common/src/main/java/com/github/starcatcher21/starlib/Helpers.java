package com.github.starcatcher21.starlib;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Helpers {
    public static ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureOf(String namepsace, String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(namepsace, id));
    }
    public static void spawnParticle(Level world, Vec3 pos, RandomSource random, ParticleOptions effect) {
        double d = pos.x() + random.nextDouble();
        double e = pos.y() - 0.05;
        double f = pos.z() + random.nextDouble();
        world.addParticle(effect, d, e, f, 0.0, 0.0, 0.0);
    }
    public static VoxelShape getVox(LivingEntity entity, BlockPos pos) {
        AABB entBox = entity.getBoundingBox();
        double enminX = entBox.minX - pos.getX();
        double enminY = entBox.minY - pos.getY();
        double enminZ = entBox.minZ - pos.getZ();
        double enmaxX = entBox.maxX - pos.getX();
        double enmaxY = entBox.maxY - pos.getY();
        double enmaxZ = entBox.maxZ - pos.getZ();
        return Shapes.box(enminX, enminY, enminZ, enmaxX, enmaxY, enmaxZ);
    }
    public static boolean isIntersect(Level world, LivingEntity entity, BlockState state, BlockPos pos) {
        BlockGetter bw = (BlockGetter) world;
        VoxelShape entVox = Helpers.getVox(entity, pos);
        VoxelShape blockVox = state.getShape(bw, pos);

        AABB bBox = blockVox.bounds();
        AABB eBox = entVox.bounds();
        return bBox.intersects(eBox);
    }
    public static boolean isIntersectSolid(Level world, LivingEntity entity, BlockState state, BlockPos pos) {
        BlockGetter bw = (BlockGetter) world;
        VoxelShape entVox = Helpers.getVox(entity, pos);
        VoxelShape blockVox = state.getShape(bw, pos);

        AABB bhBox = blockVox.bounds();
        AABB bBox = Shapes.box(bhBox.minX - 0.1, bhBox.minY - 0.1, bhBox.minZ - 0.1, bhBox.maxX + 0.1, bhBox.maxY + 0.1, bhBox.maxZ + 0.1).bounds();
        AABB eBox = entVox.bounds();
        return bBox.intersects(eBox);
    }
    public static boolean isIntersectCollision(Level world, LivingEntity entity, BlockState state, BlockPos pos) {
        BlockGetter bv = (BlockGetter) world;
        VoxelShape entVox = Helpers.getVox(entity, pos);
        VoxelShape blockVox = state.getCollisionShape(bv, pos);

        AABB bBox = blockVox.bounds();
        AABB eBox = entVox.bounds();
        return bBox.intersects(eBox);
    }
    public static boolean isIntersectSolidCollision(Level world, LivingEntity entity, BlockState state, BlockPos pos) {
        BlockGetter bw = (BlockGetter) world;
        VoxelShape entVox = Helpers.getVox(entity, pos);
        VoxelShape blockVox = state.getCollisionShape(bw, pos);

        AABB bhBox = blockVox.bounds();
        AABB bBox = Shapes.box(bhBox.minX - 0.1, bhBox.minY - 0.1, bhBox.minZ - 0.1, bhBox.maxX + 0.1, bhBox.maxY + 0.1, bhBox.maxZ + 0.1).bounds();
        AABB eBox = entVox.bounds();
        return bBox.intersects(eBox);
    }
    public static boolean isIntersectCustom(LivingEntity entity, BlockPos pos, AABB bBox) {
        VoxelShape entVox = Helpers.getVox(entity, pos);

        AABB eBox = entVox.bounds();
        return bBox.intersects(eBox);
    }
    public static VoxelShape FacingAll(Direction dir, float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return switch (dir) {
            case DOWN -> Shapes.box(1-maxX, 1-maxY, 1-maxZ, 1-minX, 1-minY, 1-minZ);
            case EAST -> Shapes.box(minY, minX, minZ, maxY, maxX, maxZ);
            case WEST -> Shapes.box(1-maxY, 1-maxX, 1-maxZ, 1-minY, 1-minX, 1-minZ);
            case NORTH -> Shapes.box(1-maxZ, 1-maxX, 1-maxY, 1-minZ, 1-minX, 1-minY);
            case SOUTH -> Shapes.box(minZ, minX, minY, maxZ, maxX, maxY);
            default ->  Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
        };
    }
    public static VoxelShape FacingHorizontal(Direction dir, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, boolean lowZ) {
        if (!lowZ) {
            return switch (dir) {
                case EAST -> Shapes.box(1 - maxZ, 1 - maxY, 1 - maxX, 1 - minZ, 1 - minY, 1 - minX);
                case WEST -> Shapes.box(minZ, minY, minX, maxZ, maxY, maxX);
                case SOUTH -> Shapes.box(1 - maxX, 1 - maxY, 1 - maxZ, 1 - minX, 1 - minY, 1 - minZ);
                default -> Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
            };
        } else {
            return switch (dir) {
                case EAST -> Shapes.box(1 - maxZ, minY, 1 - maxX, 1 - minZ,  maxY, 1 - minX);
                case WEST -> Shapes.box(minZ, minY, minX, maxZ, maxY, maxX);
                case SOUTH -> Shapes.box(1 - maxX, minY, 1 - maxZ, 1 - minX, maxY, 1 - minZ);
                default -> Shapes.box(minX, minY, minZ, maxX, maxY, maxZ);
            };
        }
    }
    public static boolean isColliding(Block targetBlock, BlockGetter world, BlockPos pos, Direction dir) {
        return switch (dir) {
            case NORTH -> world.getBlockState(pos.north(1)).getBlock() == targetBlock;
            case SOUTH -> world.getBlockState(pos.south(1)).getBlock() == targetBlock;
            case WEST -> world.getBlockState(pos.west(1)).getBlock() == targetBlock;
            case EAST -> world.getBlockState(pos.east(1)).getBlock() == targetBlock;
            case UP -> world.getBlockState(pos.above(1)).getBlock() == targetBlock;
            case DOWN -> world.getBlockState(pos.below(1)).getBlock() == targetBlock;
        };
    }
    public static boolean isCollidingAny(Block targetBlock, BlockGetter world, BlockPos pos, ImmutableList<Direction> list) {
        for (Direction direction : list) {
            if (isColliding(targetBlock, world, pos, direction)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Direction getCollidingDirection(Block targetBlock, BlockGetter world, BlockPos pos, ImmutableList<Direction> list) {
        for (Direction direction : list) {
            if (isColliding(targetBlock, world, pos, direction)) {
                return direction;
            }
        }
        return null;
    }

    @Nullable
    public static BlockPos getCollidingPosition(Block targetBlock, BlockGetter world, BlockPos pos, ImmutableList<Direction> list) {
        for (Direction direction : list) {
            if (isColliding(targetBlock, world, pos, direction)) {
                return switch (direction) {
                    case NORTH -> pos.north(1);
                    case SOUTH -> pos.south(1);
                    case WEST -> pos.west(1);
                    case EAST -> pos.east(1);
                    case UP -> pos.above(1);
                    case DOWN -> pos.below(1);
                };
            }
        }
        return null;
    }
}
