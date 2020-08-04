package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

public class SpawningManager {
    // TODO: System to summon a certain amount of mobs around the "Totem" structure.
    private final TotemSpawners plugin;

    public SpawningManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }


    public void createWaveTask(Totem totem) {
        // Spawn wave every 10 seconds if there are less than 4 mobs near by
        System.out.println("--> Created wave task");
        totem.setSpawnTask(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            System.out.println("--> Wave Called");

            int monsterCount = 0;
            for (Entity entity : totem.getWorld().getNearbyEntities(totem.getCoreBlock(), 12, 12, 12)) {
                if (!(entity instanceof Monster)) continue;
                ++monsterCount;
            }

            System.out.println("--> Monsters in Chunk " + monsterCount);
            if (monsterCount < 3) {
                spawnWave(totem);
            }
//            spawnWave(totem);
        }, 1, 8 * 20L));
    }

    public void spawnWave(Totem totem) {
        // Check if chunk is loaded
        if (!totem.getWorld().isChunkLoaded(totem.getCoreBlock().getChunk())) return;
        int spawnAmount = (int) (Math.random() * 5) + 3;

        if (!checkForNearPlayers(totem)) return;

        for (int x = 1; x <= spawnAmount; ++x) {
            // Generate random spawn location for this mob
            Location summonLocation = getRandomLocationInBounds(totem.getCoreBlock());
            // Summon the mob to the world
            summonMob(totem.getWorld(), summonLocation, totem.getMobType(), totem.getTier());
        }
    }

    public boolean checkForNearPlayers(Totem totem) {
        for (Player player : totem.getWorld().getPlayers()) {
            if (plugin.totemManager.isPlayerNearTotem(player, totem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param loc (Bukkit.Location) Pass in location to generate random location
     * @return Bukkit.Location - This will return a block location on the circumference of a circle around the given location
     */
    public Location getRandomLocationInBounds(Location loc) {
        // TODO make it so you pass in the bounds (stored in totem object)
        double angle = Math.random() * Math.PI * 2;
        int distance = (int) (Math.random() * 4) + 2;
        double x = Math.floor(Math.cos(angle) * distance) + loc.getX();
        double y = loc.getBlockY() + 1.5;
        double z = Math.floor(Math.sin(angle) * distance) + loc.getZ();

        Location newLoc = new Location(loc.getWorld(), x, y, z);
        return newLoc;
    }

    public void summonMob(World world, Location location, String mobType, Tiers tier) {
        // Parse mobType to Bukkit.EntityType
        EntityType entityType = EntityType.fromName(mobType) == EntityType.UNKNOWN ? EntityType.PIG : EntityType.fromName(mobType);
        // Summon Mob
        LivingEntity mob = (LivingEntity) world.spawnEntity(location, entityType);
        if (mob.getType() == EntityType.PIG) return;
        // Set Mob Health Stat
//        AttributeInstance maxHealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
//        maxHealth.setBaseValue(maxHealth.getValue() * tier.getHealthMod());
//        mob.setHealth(maxHealth.getValue());
//        // Set Knockback Resistance Stat
//        AttributeInstance knockbackRes = mob.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
//        knockbackRes.setBaseValue(knockbackRes.getValue() * tier.getKnockbackMod());
//        // Set Mob Speed Stat
//        AttributeInstance movementSpeed = mob.getAttrsibute(Attribute.GENERIC_MOVEMENT_SPEED);
//        movementSpeed.setBaseValue(movementSpeed.getValue() * tier.getSpeedMod());
//        // Set Mob Damage Stat
//        AttributeInstance mobDamage = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
//        mobDamage.setBaseValue(mobDamage.getValue() * tier.getDamageMod());
//        // Set Mob Armor Stat
//        AttributeInstance mobArmor = mob.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS);
//        mobArmor.setBaseValue(mobArmor.getValue() * tier.getArmorMod());
    }
}
