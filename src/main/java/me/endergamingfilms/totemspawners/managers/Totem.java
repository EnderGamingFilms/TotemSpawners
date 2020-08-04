package me.endergamingfilms.totemspawners.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

public class Totem {
    private World world;
    private Location coreBlock;
    private Location tierBlock;
    private Tiers tier;
    private String mobType;
    private String totemName;
    private BukkitTask spawnTask;

    public Totem(String totemName, World world, String mobType) {
        this.totemName = totemName;
        this.world = world;
        this.mobType = mobType;
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

    public void setWorld(World world) {
        this.world = world;
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

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public void setTotemName(String totemName) {
        this.totemName = totemName;
    }

    public void setSpawnTask(BukkitTask spawnTask) {
        this.spawnTask = spawnTask;
    }
}
