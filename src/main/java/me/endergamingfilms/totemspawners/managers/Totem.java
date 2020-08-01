package me.endergamingfilms.totemspawners.managers;

import org.bukkit.Location;

public class Totem {
    private Location coreBlock;
    private Location tierBlock;
    private Tiers tier;
    private String mobType;
    private String totemName;

    public Totem(String totemName, String mobType) {
        this.totemName = totemName;
        this.mobType = mobType;
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
}
