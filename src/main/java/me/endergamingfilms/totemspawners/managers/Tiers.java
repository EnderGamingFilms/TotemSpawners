package me.endergamingfilms.totemspawners.managers;

import org.bukkit.Material;

import java.util.List;

public class Tiers {
    private double damageMod;
    private double healthMod;
    private double armorMod;
    private double speedMod;
    private double knockbackMod;
    private Material blockMaterial;
    private String customName;
    private final String identifier;
    private List<String> flags;

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

    public List<String> getFlags() {
        return flags;
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

    public void setFlags(List<String> flags) {
        this.flags = flags;
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

//    public String convertFlagsToString() {
//        if (this.flags == null) return "";
//        StringBuilder flagsLine = new StringBuilder("[");
//        for (String str : this.flags) {
//            flagsLine.append(str).append(",");
//        }
//        System.out.println("--> shit: " + flagsLine);
//
//        int lastComma = flagsLine.lastIndexOf(",");
//        String fix = flagsLine.substring(0,lastComma);
//        StringBuilder tempThing = new StringBuilder(fix).append("]");
//
//        System.out.println("--> shit2: " + tempThing.toString());
//
//        return tempThing.toString();
//    }
}
