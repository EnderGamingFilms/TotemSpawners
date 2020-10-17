package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TotemManager {
    // TODO: Handles all totem actions (creation / totem-tiers / totem location / wave system)
    private final TotemSpawners plugin;
    private final Map<String, Totem> totemMap = new HashMap<>();
    public final CreationManager creationManager;
    public final SpawningManager spawningManager;
    public final TierManager tierManager;
    public BukkitTask mainCheckTask;

    public TotemManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
        this.creationManager = new CreationManager(instance);
        this.spawningManager = new SpawningManager(instance);
        this.tierManager = new TierManager(instance);
        // Scheduled Task - Check for loaded totems
        startCheckTask();
    }

    public void startCheckTask() {
        mainCheckTask = Bukkit.getScheduler().runTaskTimer(plugin, this::totemLoadCheck, 1, 30 * 20L);
    }

    public void addTotem(Totem totem) {
        this.totemMap.put(totem.getTotemName(), totem);
    }

    public Map<String, Totem> getTotemMap() {
        return this.totemMap;
    }

    public void startCreation(Player player, final String[] args) {
        // Check if player is already creating a portal
        if (creationManager.getCreationTasks().containsKey(player.getUniqueId())) {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&cYou are already creating a totem!"));
            return;
        }
        // Check if the player is holding an item (to be used as a mob type)
        if (!player.getItemInHand().getType().getKey().getKey().matches(".+?(_spawn_egg)")) {
            plugin.messageUtils.send(player, plugin.messageUtils.format("&cYou must hold a spawn egg to create a totem!"));
            return;
        }
        // Check if totem with the same name already exists
        if (!totemMap.isEmpty()) {
            for (Map.Entry<String, Totem> entry : totemMap.entrySet()) {
                String name = entry.getKey();
                if (name.equalsIgnoreCase(args[1])) {
                    plugin.messageUtils.send(player, plugin.respond.totemExists());
                    return;
                }
            }
        }
        // Start totem creation process if all checks are passed
        creationManager.startSelection(player, args, player.getItemInHand());
    }

    public void removeTotem(String totemName) {
        if (totemMap.get(totemName).getSpawnTask() != null)
            totemMap.get(totemName).getSpawnTask().cancel();
        totemMap.remove(totemName);
    }

    public Totem getTotem(String totem) {
        return this.totemMap.get(totem);
    }

    public void createTotem(Player player) {
        Totem temp = plugin.totemManager.creationManager.getCreationMap().get(player.getUniqueId());
        // Take selection tool
        creationManager.takeSelectionTool(player);
        // Setup totem tier
        setTierToTotem(temp, temp.getTierBlock());
        // Add newly created totem
        addTotem(temp);
        // Remove player from creation maps
        creationManager.getCreationMap().remove(player.getUniqueId());
        creationManager.getCreationTasks().remove(player.getUniqueId());
        // Send success message
        plugin.messageUtils.send(player, plugin.messageUtils.format("&e" + plugin.messageUtils.capitalize(temp.getMobType()) + " Totem &7has been created!"));
    }

    public void setTierToTotem(@NotNull Totem totem, @NotNull Location loc) {
        totem.setTier(tierManager.getTierFromMaterial(loc.getBlock().getType()));
    }

    public boolean isTracking(String totem) {
        return totemMap.containsKey(totem);
    }

    public void totemLoadCheck() {
        // This is only to create a spawning task for new totems that are created or for some reason were not loaded when the plugin stated
        // This will also recreate the spawning tasks when the plugin is reloaded
        // Iterate through all current totems (and loads a spawning task if needed) [run every 10 seconds]
        for (Map.Entry<String, Totem> entry : totemMap.entrySet()) {
            Totem totem = entry.getValue();
//            if (totem == null) continue;
            // Check if totem is already spawning mobs, if not then create task
            if (totem.getSpawnTask() == null || totem.getSpawnTask().isCancelled())//totem.getSpawnTask() == null)
                spawningManager.createWaveTask(totem);
        }
    }

    public boolean totemGeneralCheck(Totem totem) {
        // Check if there are players in the totem's world
        if (plugin.fileManager.debug) System.out.println("--> playersInWorld? " + !(totem.getWorld().getPlayers().size() == 0));
        if (totem.getWorld().getPlayers().size() == 0) return false;
        // Check if totem is located in a loaded chunk
        if (plugin.fileManager.debug) System.out.println("--> chunkLoaded? " + (totem.getWorld().isChunkLoaded(totem.getCoreBlock().getChunk())));
        if (!totem.getWorld().isChunkLoaded(totem.getCoreBlock().getChunk())) return false;
        // Check if players are near the totem
        for (Player player : totem.getWorld().getPlayers()) {
            // Check if any players are near the totem
            if (plugin.fileManager.debug) System.out.println("--> playersNearTotem? " + (isPlayerNearTotem(player, totem)));
            if (!isPlayerNearTotem(player, totem)) return false;
        }
        return true;
    }

    public boolean isPlayerNearTotem(Player player, Totem totem) {
        return totem.getCoreBlock().distanceSquared(player.getLocation()) <= 400;
    }
}
