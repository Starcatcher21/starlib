package com.github.starcatcher21.starlib.energy;

public class SimpleModEnergyStorage implements ModEnergyStorage {
    private long amount;
    private final long capacity;
    private final long maxInsert;
    private final long maxExtract;
    private final Runnable onChange;

    public SimpleModEnergyStorage(long capacity, long maxInsert, long maxExtract, Runnable onChange) {
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
        this.onChange = onChange;
    }

    @Override
    public long receiveEnergy(long maxReceive, boolean simulate) {
        long toInsert = Math.min(maxReceive, Math.min(capacity - amount, maxInsert));
        if (!simulate && toInsert > 0) {
            amount += toInsert;
            if (onChange != null) onChange.run();
        }
        return toInsert;
    }

    @Override
    public long extractEnergy(long maxExtractAmount, boolean simulate) {
        long toExtract = Math.min(maxExtractAmount, Math.min(amount, maxExtract));
        if (!simulate && toExtract > 0) {
            amount -= toExtract;
            if (onChange != null) onChange.run();
        }
        return toExtract;
    }

    @Override public long getAmount() { return amount; }
    @Override public long getCapacity() { return capacity; }
    @Override public void setAmount(long amount) { this.amount = amount; }
}
