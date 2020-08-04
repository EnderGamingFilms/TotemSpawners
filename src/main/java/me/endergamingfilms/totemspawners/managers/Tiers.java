package me.endergamingfilms.totemspawners.managers;

import org.bukkit.Material;

public class Tiers {
    private double damageMod;
    private double healthMod;
    private double armorMod;
    private double speedMod;
    private double knockbackMod;
    private Material blockMaterial;
    private String customName;
    private final String identifier;

    public Tiers(String identifier) {
        this.identifier = identifier;
    }

    public double getArmorMod() {
        return armorMod;
    }

    public double getDamageMod() {
        return damageMod;
    }

    public double getHealthMod() {
        return healthMod;
    }

    public double getKnockbackMod() {
        return knockbackMod;
    }

    public double getSpeedMod() {
        return speedMod;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public String getCustomName() {
        return customName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setArmorMod(double armorMod) {
        this.armorMod = armorMod;
    }

    public void setDamageMod(double damageMod) {
        this.damageMod = damageMod;
    }

    public void setHealthMod(double healthMod) {
        this.healthMod = healthMod;
    }

    public void setSpeedMod(double speedMod) {
        this.speedMod = speedMod;
    }

    public void setKnockbackMod(double knockbackMod) {
        this.knockbackMod = knockbackMod;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public void setBlockMaterial(Material blockMaterial) {
        this.blockMaterial = blockMaterial;
    }
}
