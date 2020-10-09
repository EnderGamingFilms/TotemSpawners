package me.endergamingfilms.totemspawners.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

public class Totem {
    private final World world;
    private Location coreBlock;
    private Location tierBlock;
    private Tiers tier;
    private final String mobType;
    private final String totemName;
    private BukkitTask spawnTask;
    private int minSpawnLimit;
    private int maxSpawnLimit;
    private int minSpawnRadius;
    private int maxSpawnRadius;
    private int spawningPeriod;


    public Totem(final String totemName, final World world, final String mobType) {
        this.totemName = totemName;
        this.world = world;
        this.mobType = mobType;
        // Set Default Values
        this.minSpawnLimit = 3;
        this.maxSpawnLimit = 5;
        this.minSpawnRadius = 3;
        this.maxSpawnRadius = 4;
        this.spawningPeriod = 8; // Default 8 Seconds
    }

    public World getWorld() {
        return world;
    }

    public Location getCoreBlock() {
        return coreBlock;
    }

    public Location getTierBlock() {
        return tierBlock;
    }

    public Tiers getTier() {
        return tier;
    }

    public String getMobType() {
        return mobType;
    }

    public String getTotemName() {
        return totemName;
    }

    public BukkitTask getSpawnTask() {
        return spawnTask;
    }

    public int getMinSpawnRadius() {
        return minSpawnRadius;
    }

    public int getMaxSpawnRadius() {
        return maxSpawnRadius;
    }

    public int getMinSpawnLimit() {
        return minSpawnLimit;
    }

    public int getMaxSpawnLimit() {
        return maxSpawnLimit;
    }

    public int getSpawningPeriod() {
        return spawningPeriod;
    }

    public void setCoreBlock(Location coreBlock) {
        this.coreBlock = coreBlock;
    }

    public void setTierBlock(Location tierBlock) {
        this.tierBlock = tierBlock;
    }

    public void setTier(Tiers tier) {
        this.tier = tier;
    }

    public void setSpawnTask(BukkitTask spawnTask) {
        this.spawnTask = spawnTask;
    }

    public void setMinSpawnRadius(int minSpawnRadius) {
        this.minSpawnRadius = minSpawnRadius;
    }

    public void setMaxSpawnRadius(int maxSpawnRadius) {
        this.maxSpawnRadius = maxSpawnRadius;
    }

    public void setMinSpawnLimit(int minSpawnLimit) {
        this.minSpawnLimit = minSpawnLimit;
    }

    public void setMaxSpawnLimit(int maxSpawnLimit) {
        this.maxSpawnLimit = maxSpawnLimit;
    }

    public void setSpawningPeriod(int spawningPeriod) {
        this.spawningPeriod = spawningPeriod;
    }
}
