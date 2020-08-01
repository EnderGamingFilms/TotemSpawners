package me.endergamingfilms.totemspawners.managers;

public class Tiers {
    private double damageMod;
    private double healthMod;
    private double armorMod;
    private double speedMod;

    public double getArmorMod() {
        return armorMod;
    }

    public double getDamageMod() {
        return damageMod;
    }

    public double getHealthMod() {
        return healthMod;
    }

    public double getSpeedMod() {
        return speedMod;
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
}
