package de.melanx.simplebackups;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;

public class BackupData extends SavedData {

    private long lastSaved;
    private long lastFullBackup;
    private boolean paused;
    private boolean merging;

    private BackupData() {
        // use BackupData.get
    }

    public static BackupData get(ServerLevel level) {
        return BackupData.get(level.getServer());
    }

    public static BackupData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(nbt -> new BackupData().load(nbt), BackupData::new, "simplebackups");
    }

    public BackupData load(@Nonnull CompoundTag nbt) {
        this.lastSaved = nbt.getLong("lastSaved");
        this.lastFullBackup = nbt.getLong("lastFullBackup");
        this.paused = nbt.getBoolean("paused");
        this.merging = nbt.getBoolean("merging");
        return this;
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        nbt.putLong("lastSaved", this.lastSaved);
        nbt.putLong("lastFullBackup", this.lastFullBackup);
        nbt.putBoolean("paused", this.paused);
        nbt.putBoolean("merging", this.merging);
        return nbt;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        this.setDirty();
    }

    public boolean isPaused() {
        return this.paused;
    }

    public long getLastSaved() {
        return this.lastSaved;
    }

    public void updateSaveTime(long time) {
        this.lastSaved = time;
        this.setDirty();
    }

    public long getLastFullBackup() {
        return this.lastFullBackup;
    }

    public void updateFullBackupTime(long time) {
        this.lastFullBackup = time;
        this.setDirty();
    }

    public boolean isMerging() {
        return merging;
    }

    public void startMerging() {
        this.merging = true;
    }

    public void stopMerging() {
        this.merging = false;
    }
}
