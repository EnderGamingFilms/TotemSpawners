package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class SpawningManager {
    // TODO: System to summon a certain amount of mobs around the "Totem" structure.
    private final TotemSpawners plugin;

    public SpawningManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }


    public void createWaveTask(Totem totem) {
        // Spawn wave every 8 seconds if there are less than 4 mobs near by
        if (totem.getSpawnTask() != null) totem.setSpawnTask(null);
        totem.setSpawnTask(Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (plugin.fileManager.debug) System.out.println("--> Wave Called (" + totem.getTotemName() + ")");
            // Use this to preform as little checks as possible
            if (!plugin.totemManager.totemGeneralCheck(totem)) return;
            // Check if there are more than 3 mobs near the totem
            if (getMonsterCountNearLocation(totem.getCoreBlock()) < 3) {
                spawnWave(totem);
            }
        }, 1, 8 * 20L));
    }

    public int getMonsterCountNearLocation(Location loc) {
        if (loc.getWorld() == null) return 0;

        int count = 0;
        for (Entity entity : loc.getWorld().getNearbyEntities(loc, 12, 12, 12)) {
            if (!(entity instanceof Monster)) continue;
            ++count;
        }

        if (plugin.fileManager.debug) System.out.println("--> Monsters in Chunk " + count);

        return count;
    }

    public void spawnWave(Totem totem) {
        // Check if chunk is loaded
        if (!totem.getWorld().isChunkLoaded(totem.getCoreBlock().getChunk())) return;
        int spawnAmount = (int) (Math.random() * Math.max(1, totem.getMaxSpawnLimit()) + Math.max(1, totem.getMinSpawnLimit()));

        if (!checkForNearPlayers(totem)) return;

        for (int x = 1; x <= spawnAmount; ++x) {
            // Generate random spawn location for this mob
            Location summonLocation = getRandomLocationInBounds(totem.getCoreBlock(), totem.getMinSpawnRadius(), totem.getMaxSpawnRadius());
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
     * @param min (int) Min radius to spawn mob
     * @param max (int) Max radius to spawn mob
     * @return Bukkit.Location - This will return a block location on the circumference of a circle around the given location
     */
    public Location getRandomLocationInBounds(Location loc, int min, int max) {
        // TODO make it so you pass in the bounds (stored in totem object)
        double angle = Math.random() * Math.PI * 2;
        int distance = (int) (Math.random() * Math.max(1, max)) + Math.max(1, min);
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
        // Apply Flags
        applyFlags(mob, tier);
        // Get Attribute Instances
        AttributeInstance ATTRIBUTE_MAX_HEALTH = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        AttributeInstance ATTRIBUTE_MOVEMENT_SPEED = mob.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeInstance ATTRIBUTE_ATTACK_DAMAGE = mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        // Modify Attributes
        if (ATTRIBUTE_MAX_HEALTH != null) {
            final double baseMaxHealth = ATTRIBUTE_MAX_HEALTH.getBaseValue();
            final double newMaxHealth = baseMaxHealth * tier.getHealthMod();
            ATTRIBUTE_MAX_HEALTH.setBaseValue(newMaxHealth);
            mob.setHealth(newMaxHealth);
        }

        if (ATTRIBUTE_MOVEMENT_SPEED != null) {
            final double baseMovementSpeed = Math.max(0.1, ATTRIBUTE_MOVEMENT_SPEED.getBaseValue());
            final double newMovementSpeed = baseMovementSpeed * tier.getSpeedMod();
            ATTRIBUTE_MOVEMENT_SPEED.setBaseValue(newMovementSpeed);
        }

        if (ATTRIBUTE_ATTACK_DAMAGE != null) {
            final double baseAttackDamage = Math.max(0.1, ATTRIBUTE_ATTACK_DAMAGE.getBaseValue());
            final double newAttackDamage = baseAttackDamage * tier.getDamageMod();
            ATTRIBUTE_ATTACK_DAMAGE.setBaseValue(newAttackDamage);
        }
    }

    public void applyFlags(LivingEntity mob, Tiers tier) {
        if (tier.getFlags() == null) return;
        for (String value : tier.getFlags()) {
            if (value.startsWith("canBurn")) {
                String tmp = value.replace("canBurn=", "");
                if (tmp.equalsIgnoreCase("false")) {
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1000000, 1, true));
                }
            }
        }
    }
}
