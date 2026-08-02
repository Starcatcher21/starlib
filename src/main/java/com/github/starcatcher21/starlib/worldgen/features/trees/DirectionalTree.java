package com.github.starcatcher21.starlib.worldgen.features.trees;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

// North negative Z
// South positive Z
// West negative X
// east positive X
public class DirectionalTree {
    public static Set<BlockPos> logs;
    public static Set<BlockPos> leaves;
    public static Set<BlockPos> fruits;
    public static Tree getFromNorth(Tree tree, Direction want) {
        logs = new ObjectArraySet<>();
        leaves = new ObjectArraySet<>();
        fruits = new ObjectArraySet<>();
        if (want.equals(Direction.SOUTH)) {
            for (BlockPos pos : tree.logs) {
                if (!(pos.getZ() == 0 && pos.getX() == 0)) {
                    BlockPos newPos = new BlockPos(pos.getX()*-1, pos.getY(), pos.getZ() * -1);
                    logs.add(newPos);
                } else {
                    logs.add(pos);
                }
            }
            for (BlockPos pos : tree.leaves) {
                BlockPos newPos = new BlockPos(pos.getX()*-1, pos.getY(), pos.getZ() * -1);
                leaves.add(newPos);
            }
            for (BlockPos pos : tree.fruits) {
                BlockPos newPos = new BlockPos(pos.getX()*-1, pos.getY(), pos.getZ() * -1);
                fruits.add(newPos);
            }
            Tree tre = new Tree(false, tree.name+"SOUTH", tree.log, tree.leave);
            tre.addLogPos(logs);
            tre.addLeavesPos(leaves);
            tre.addFruitsPos(fruits);
            tre.addFruits(tree.fruit);
            tre.addReplacableBlock(tree.replacable);
            tre.setFruitChange(tree.fruitchange);
            return tre;
        }
        if (want.equals(Direction.WEST)) {
            for (BlockPos pos : tree.logs) {
                if (!(pos.getZ() == 0 && pos.getX() == 0)) {
                    BlockPos newPos = new BlockPos(pos.getZ(), pos.getY(), pos.getX()*-1);
                    logs.add(newPos);
                } else {
                    logs.add(pos);
                }
            }
            for (BlockPos pos : tree.leaves) {
                BlockPos newPos = new BlockPos(pos.getZ(), pos.getY(), pos.getX()*-1);
                leaves.add(newPos);
            }
            for (BlockPos pos : tree.fruits) {
                BlockPos newPos = new BlockPos(pos.getZ(), pos.getY(), pos.getX()*-1);
                fruits.add(newPos);
            }
            Tree tre = new Tree(false, tree.name+"WEST", tree.log, tree.leave);
            tre.addLogPos(logs);
            tre.addLeavesPos(leaves);
            tre.addFruitsPos(fruits);
            tre.addFruits(tree.fruit);
            tre.addReplacableBlock(tree.replacable);
            tre.setFruitChange(tree.fruitchange);
            return tre;
        }
        if (want.equals(Direction.EAST)) {
            for (BlockPos pos : tree.logs) {
                if (!(pos.getZ() == 0 && pos.getX() == 0)) {
                    BlockPos newPos = new BlockPos(pos.getZ()*-1, pos.getY(), pos.getX());
                    logs.add(newPos);
                } else {
                    logs.add(pos);
                }
            }
            for (BlockPos pos : tree.leaves) {
                BlockPos newPos = new BlockPos(pos.getZ()*-1, pos.getY(), pos.getX());
                leaves.add(newPos);
            }
            for (BlockPos pos : tree.fruits) {
                BlockPos newPos = new BlockPos(pos.getZ()*-1, pos.getY(), pos.getX());
                fruits.add(newPos);
            }
            Tree tre = new Tree( false, tree.name+"WEST", tree.log, tree.leave);
            tre.addLogPos(logs);
            tre.addLeavesPos(leaves);
            tre.addFruits(tree.fruit);
            tre.addFruitsPos(fruits);
            tre.addReplacableBlock(tree.replacable);
            tre.setFruitChange(tree.fruitchange);
            return tre;
        }
        if (want.equals(Direction.DOWN)) {
            for (BlockPos pos : tree.logs) {
                if (!(pos.getZ() == 0 && pos.getX() == 0)) {
                    BlockPos newPos = new BlockPos(pos.getX()*-1, pos.getY()*-1, pos.getZ()*-1);
                    logs.add(newPos);
                } else {
                    logs.add(pos);
                }
            }
            for (BlockPos pos : tree.leaves) {
                BlockPos newPos = new BlockPos(pos.getX()*-1, pos.getY()*-1, pos.getZ()*-1);
                leaves.add(newPos);
            }
            for (BlockPos pos : tree.fruits) {
                BlockPos newPos = new BlockPos(pos.getX()*-1, pos.getY()*-1, pos.getZ()*-1);
                fruits.add(newPos);
            }
            Tree tre = new Tree( false, tree.name+"WEST", tree.log, tree.leave);
            tre.addLogPos(logs);
            tre.addLeavesPos(leaves);
            tre.addFruitsPos(fruits);
            tre.addFruits(tree.fruit);
            tre.addReplacableBlock(tree.replacable);
            tre.setFruitChange(tree.fruitchange);
            return tre;
        }
        return tree;
    }
}
