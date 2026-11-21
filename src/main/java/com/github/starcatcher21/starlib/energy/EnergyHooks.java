package com.github.starcatcher21.starlib.energy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
//? if neoforge {
import net.neoforged.neoforge.capabilities.Capabilities;
//? if >= 26.1 {
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
//?} else {
/*import net.neoforged.neoforge.energy.IEnergyStorage;
 *///?}
//? } else {
/*import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;
*///? }

public class EnergyHooks {

    /* ================================================================ */
    /* pushEnergy                                                       */
    /* ================================================================ */
    public static long pushEnergy(Level level, BlockPos pos, Direction direction,
                                  ModEnergyStorage source, long maxAmount) {

        //? if neoforge {
        //? if >= 26.1 {
        EnergyHandler targetHandler = level.getCapability(
                Capabilities.Energy.BLOCK,
                pos.relative(direction),
                direction.getOpposite());
        if (targetHandler == null) return 0L;

        long simulateExtract = source.extractEnergy(maxAmount, true);
        if (simulateExtract <= 0L) return 0L;

        int toInsert = (int) Math.min(simulateExtract, Integer.MAX_VALUE);
        long inserted = 0L;
        try (Transaction tx = Transaction.openRoot()) {
            inserted = targetHandler.insert(toInsert, tx);
            if (inserted > 0L) {
                source.extractEnergy(inserted, false);
                tx.commit();
            }
        }
        return inserted;
        //?} else {
        /*IEnergyStorage target = level.getCapability(
                Capabilities.EnergyStorage.BLOCK,
                pos.relative(direction),
                direction.getOpposite());
        if (target == null) return 0L;

        long toExtract = source.extractEnergy(maxAmount, true);
        if (toExtract <= 0L) return 0L;

        int toInsert = (int) Math.min(toExtract, Integer.MAX_VALUE);
        int accepted = target.receiveEnergy(toInsert, true);
        if (accepted <= 0) return 0L;

        int extracted = (int) source.extractEnergy(accepted, false);
        int inserted = target.receiveEnergy(extracted, false);
        return inserted;
        *///?}
        //? } else {
        /*EnergyStorage target = EnergyStorage.SIDED.find(
                level, pos.relative(direction), direction.getOpposite());
        if (target == null) return 0;

        try (Transaction transaction = Transaction.openOuter()) {
            long extracted = source.extractEnergy(maxAmount, true);
            if (extracted <= 0) return 0;

            long inserted = target.insert(extracted, transaction);
            if (inserted > 0) {
                source.extractEnergy(inserted, false);
                transaction.commit();
                return inserted;
            }
        }
        return 0;
        *///? }
    }

    /* ================================================================ */
    /* hasEnergyCapability                                              */
    /* ================================================================ */
    public static boolean hasEnergyCapability(Level level, BlockPos pos, Direction side) {
        //? if neoforge {
        //? if >= 26.1 {
        return level.getCapability(Capabilities.Energy.BLOCK, pos, side) != null;
        //?} else {
        /*return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, side) != null;
         *///?}
        //? } else {
        /*return EnergyStorage.SIDED.find(level, pos, side) != null;
         *///? }
    }

    /* ================================================================ */
    /* Capability adapters                                              */
    /* ================================================================ */
    //? if neoforge {
    //? if >= 26.1 {
    public static EnergyHandler adapt(ModEnergyStorage storage) {
        return new EnergyHandler() {
            @Override public long getCapacityAsLong() { return storage.getCapacity(); }
            @Override public long getAmountAsLong()   { return storage.getAmount(); }
            @Override public int insert(int i, TransactionContext transactionContext) {
                return Math.toIntExact(storage.receiveEnergy(i, false));
            }
            @Override public int extract(int i, TransactionContext transactionContext) {
                return Math.toIntExact(storage.extractEnergy(i, false));
            }
        };
    }
    //?} else {
    /*public static IEnergyStorage adapt(ModEnergyStorage storage) {
        return new IEnergyStorage() {
            @Override public int receiveEnergy(int maxReceive, boolean simulate) {
                return (int) storage.receiveEnergy(maxReceive, simulate);
            }
            @Override public int extractEnergy(int maxExtract, boolean simulate) {
                return (int) storage.extractEnergy(maxExtract, simulate);
            }
            @Override public int getEnergyStored()    { return (int) storage.getAmount(); }
            @Override public int getMaxEnergyStored() { return (int) storage.getCapacity(); }
            @Override public boolean canExtract()     { return true; }
            @Override public boolean canReceive()     { return true; }
        };
    }
    *///?}
    //? } else {
    /*public static EnergyStorage adapt(ModEnergyStorage storage) {
        return new EnergyStorage() {
            @Override
            public long insert(long maxAmount, TransactionContext transaction) {
                long inserted = storage.receiveEnergy(maxAmount, false);
                transaction.addCloseCallback((tx, result) -> {
                    if (result.wasAborted()) storage.extractEnergy(inserted, false);
                });
                return inserted;
            }

            @Override
            public long extract(long maxAmount, TransactionContext transaction) {
                long extracted = storage.extractEnergy(maxAmount, false);
                transaction.addCloseCallback((tx, result) -> {
                    if (result.wasAborted()) storage.receiveEnergy(extracted, false);
                });
                return extracted;
            }

            @Override public long getAmount()   { return storage.getAmount(); }
            @Override public long getCapacity() { return storage.getCapacity(); }
        };
    }
    *///? }
}
