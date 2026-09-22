package com.github.starcatcher21.starlib.worldgen.features.trees;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Tree {
    public final Boolean ROTATO;
    public final String name;
    public final Set<BlockPos> logs = new ObjectArraySet<>();
    public final Set<BlockPos> leaves = new ObjectArraySet<>();
    public final Set<BlockPos> fruits = new ObjectArraySet<>();
    public final Set<Block> replacable = new ObjectArraySet<>();
    public final Set<BlockState> log = new ObjectArraySet<>();
    public final Set<BlockState> leave = new ObjectArraySet<>();
    public final Set<BlockState> fruit = new ObjectArraySet<>();
    public int fruitchange = 0;
    private static final Random random = new Random();

    public Tree(Boolean rotatable, String name) {
        this.ROTATO = rotatable;
        this.name = name;
        this.log.add(Blocks.AIR.defaultBlockState());
        this.leave.add(Blocks.AIR.defaultBlockState());
    }
    public Tree(Boolean rotatable, String name, BlockState log, BlockState leave) {
        this.ROTATO = rotatable;
        this.name = name;
        this.log.add(log);
        this.leave.add(leave);
    }
    public Tree(Boolean rotatable, String name, Set<BlockState> log, Set<BlockState> leave) {
        this.ROTATO = rotatable;
        this.name = name;
        this.log.addAll(log);
        this.leave.addAll(leave);
    }

    public void addLogPos(int X, int Y, int Z) {
        this.logs.add(new BlockPos(X, Y, Z));
    }
    public void addLeavesPos(int X, int Y, int Z) {
        this.leaves.add(new BlockPos(X, Y, Z));
    }
    public void addFruitsPos(int X, int Y, int Z) {
        this.fruits.add(new BlockPos(X, Y, Z));
    }
    public void addLogPos(BlockPos pos) {
        this.logs.add(pos);
    }
    public void addLeavesPos(BlockPos pos) {
        this.leaves.add(pos);
    }
    public void addFruitsPos(BlockPos pos) {
        this.fruits.add(pos);
    }
    public void addLogPos(Set<BlockPos> list) {
        this.logs.addAll(list);
    }
    public void addLeavesPos(Set<BlockPos> list) {
        this.leaves.addAll(list);
    }
    public void addFruitsPos(Set<BlockPos> list) {
        this.fruits.addAll(list);
    }
    public void addReplacableBlock(Block block) {
        this.replacable.add(block);
    }
    public void addReplacableBlock(Set<Block> block) {
        this.replacable.addAll(block);
    }
    public void addLog(BlockState block) {
        this.log.add(block);
    }
    public void clearLog() {
        this.log.clear();
    }
    public void addLeave(BlockState block) {
        this.leave.add(block);
    }
    public void clearLeave() {
        this.leave.clear();
    }
    public void addFruit(BlockState block) {
        this.fruit.add(block);
    }
    public void clearFruit() {
        this.fruit.clear();
    }
    public void addFruits(Set<BlockState> list) {
        this.fruit.addAll(list);
    }
    public void removeFruitPos(Set<BlockPos> list) {
        this.fruits.removeAll(list);
    }
    public void setFruitChange(int change) {
        this.fruitchange = change;
    }

    public Boolean canGrow(WorldGenLevel world, BlockPos base) {
        for (BlockPos pos : logs) {
            if (!(pos.equals(new BlockPos(0, 0, 0 )))) {
                if (!(checkBlocks(world, base.offset(pos), false))) {
                    return false;
                }
            }
        }
        for (BlockPos pos : leaves) {
            if (!(pos.equals(new BlockPos(0, 0, 0 )))) {
                if (!(checkBlocks(world, base.offset(pos), false))) {
                    return false;
                }
            }
        }
        if (!fruit.isEmpty() && !fruits.isEmpty()) {
            for (BlockPos pos : fruits) {
                if (!(pos.equals(new BlockPos(0, 0, 0 )))) {
                    if (!(checkBlocks(world, base.offset(pos), true))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean Grow(WorldGenLevel world, BlockPos base) {
        if (canGrow(world, base)) {
            BlockPos.MutableBlockPos mutable = base.mutable();
            for (int i = 0; i < logs.size(); i ++) {
                BlockPos pos = logs.stream().toList().get(i);
                BlockPos prev;
                if (i - 1 >= 0) {
                    prev = logs.stream().toList().get(i-1);
                } else {
                    if (i + 1 < logs.size()) {
                        prev = logs.stream().toList().get(i + 1);
                    } else {
                        prev = logs.stream().toList().get(i);
                    }
                }
                BlockState newLog = log.stream().toList().get(random.nextInt(log.size()));
                if (newLog.getProperties().contains(BlockStateProperties.AXIS)) {
                    if (Math.abs(pos.getZ()) > Math.abs(pos.getX())) {
                        newLog = newLog.setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
                    }
                    if (Math.abs(pos.getX()) > Math.abs(pos.getZ())) {
                        newLog = newLog.setValue(BlockStateProperties.AXIS, Direction.Axis.X);
                    }
                    if (prev.getX() == pos.getX() && prev.getZ() == pos.getZ()) {
                        newLog = newLog.setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
                    }
                    if (Math.abs(pos.getZ()) == Math.abs(pos.getX()) || logs.contains(pos.above(1)) || logs.contains(pos.below(1))) {
                        newLog = newLog.setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
                    }
                }
                if (world.isOutsideBuildHeight(mutable.offset(pos))) continue;
                if (world.ensureCanWrite(mutable.offset(pos))) {
                    world.setBlock(mutable.offset(pos), newLog, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                }
            }
            for (BlockPos pos : leaves) {
                BlockState leav =  leave.stream().toList().get(random.nextInt(leave.size()));
                if (leav.hasProperty(HugeMushroomBlock.WEST) && leav.hasProperty(HugeMushroomBlock.EAST) && leav.hasProperty(HugeMushroomBlock.NORTH) && leav.hasProperty(HugeMushroomBlock.SOUTH) && leav.hasProperty(HugeMushroomBlock.UP)) {
                    List<Block> logblock = log.stream().map((block) -> block.getBlock()).toList();
                    int search = 3;
                    BlockPos absoluteLeafPos = base.offset(pos);
                    boolean foundLogNorth = false;
                    boolean foundLogSouth = false;
                    boolean foundLogEast = false;
                    boolean foundLogWest = false;

                    for (int xOffset = -search; xOffset <= search; xOffset++) {
                        for (int zOffset = -search; zOffset <= search; zOffset++) {
                            if (xOffset == 0 && zOffset == 0) {
                                continue;
                            }

                            BlockPos checkPos = absoluteLeafPos.offset(xOffset, 0, zOffset);

                            if (logblock.contains(world.getBlockState(checkPos).getBlock())) {
                                if (zOffset < 0) {
                                    foundLogNorth = true;
                                }
                                if (zOffset > 0) {
                                    foundLogSouth = true;
                                }
                                if (xOffset > 0) {
                                    foundLogEast = true;
                                }
                                if (xOffset < 0) {
                                    foundLogWest = true;
                                }
                            }
                        }
                    }
                    boolean north = !foundLogNorth;
                    boolean south = !foundLogSouth;
                    boolean east = !foundLogEast;
                    boolean west = !foundLogWest;
                    leav = leav.setValue(HugeMushroomBlock.DOWN, false)
                            .setValue(HugeMushroomBlock.UP, true)
                            .setValue(HugeMushroomBlock.WEST, west)
                            .setValue(HugeMushroomBlock.EAST, east)
                            .setValue(HugeMushroomBlock.NORTH, north)
                            .setValue(HugeMushroomBlock.SOUTH, south);
                }
                if (leav.hasProperty(BlockStateProperties.DISTANCE)) {
                    leav = leav.setValue(BlockStateProperties.DISTANCE, 1);
                }
                if (world.isOutsideBuildHeight(mutable.offset(pos))) continue;
                if (world.ensureCanWrite(mutable.offset(pos))) {
                    world.setBlock(mutable.offset(pos), leav, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                }
            }
            if (!fruit.isEmpty()) {
                for (BlockPos pos : fruits) {
                    BlockState frut = fruit.stream().toList().get(random.nextInt(fruit.size()));
                    if (random.nextInt(100) <= this.fruitchange) {
                        if (world.isOutsideBuildHeight(mutable.offset(pos))) continue;
                        if (world.ensureCanWrite(mutable.offset(pos))) {
                            world.setBlock(mutable.offset(pos), frut, Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean checkBlocks(WorldGenLevel world, BlockPos pos, boolean isFruit) {
        for (Block block : replacable) {
            if (world.getBlockState(pos).getBlock().equals(block)) {
                if (!(isFruit && block.defaultBlockState().is(BlockTags.LEAVES))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Tree offset(Tree tree, Direction dir, int offset) {
        Tree newTree = new Tree(true, tree.name+"Offset");
        for (BlockPos pos : tree.fruits) {
            newTree.addFruitsPos(pos.relative(dir, offset));
        }
        Set<BlockPos> remFruts = new ObjectArraySet<>();
        for (BlockPos pos : tree.logs) {
            for (BlockPos frutpos : newTree.fruits) {
                if (pos.relative(dir, offset).equals(frutpos)) {
                    remFruts.add(frutpos);
                }
            }
            newTree.addLogPos(pos.relative(dir, offset));
        }
        for (BlockPos pos : tree.leaves) {
            for (BlockPos frutpos : newTree.fruits) {
                if (pos.relative(dir, offset).equals(frutpos)) {
                    remFruts.add(frutpos);
                }
            }
            newTree.addLeavesPos(pos.relative(dir, offset));
        }
        newTree.removeFruitPos(remFruts);
        return newTree;
    }

    public static Tree genBranch(int height) {
        Tree branch = new Tree(false, "branch");
        for (int i = 0; i < height; i++) {
            branch.addLogPos(0,i,0);
        }
        return branch;
    }

    public static void addBranch(Tree treeB, Tree branchB, Direction dir) {
        Tree rotated = DirectionalTree.getFromNorth(branchB, dir);
        treeB.addLogPos(rotated.logs);
        treeB.addLeavesPos(rotated.leaves);
        treeB.addFruitsPos(rotated.fruits);
    }

    public String countLogs() {
        return "" + logs.size();
    }

    public String countLeaves() {
        return "" + leaves.size();
    }

    public String countFruits() {
        return "" + fruits.size();
    }

    @Override
    public String toString() {
        return "Tree{" +
                "name='" + name + '\'' +
                ", logs amount=" + countLogs() +
                ", leaves amount=" + countLeaves() +
                ", fruits amount=" + countFruits() +
                ", logs=" + logs +
                ", leaves=" + leaves +
                ", replacable=" + replacable +
                ", log=" + log +
                ", leave=" + leave +
                ", fruit=" + fruit +
                ", fruits=" + fruits +
                '}';
    }

}