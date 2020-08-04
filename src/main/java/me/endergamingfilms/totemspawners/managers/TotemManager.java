package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
    public  final TierManager tierManager;
    public BukkitTask mainCheckTask;

    public TotemManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
        this.creationManager = new CreationManager(instance);
        this.spawningManager = new SpawningManager(instance);
        this.tierManager = new TierManager(instance);
        // Scheduled Task - Check for loaded totems
        mainCheckTask = Bukkit.getScheduler().runTaskTimer(plugin, this::totemLoadCheck, 1, 10 * 20L);
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
        if (player.getItemInHand().getType() == Material.AIR) {
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
        totemMap.remove(totemName);
    }

    public Totem getTotem(String totem) {
        return this.totemMap.get(totem);
    }

    public void createTotem(Player player) {
        Totem temp = plugin.totemManager.creationManager.getCreationMap().get(player.getUniqueId());
        // Take selection tool
        creationManager.takeSelectionTool(player);
        // Add newly created totem
        addTotem(temp);
        // Remove player from creation maps
        creationManager.getCreationMap().remove(player.getUniqueId());
        creationManager.getCreationTasks().remove(player.getUniqueId());
        // Send success message
        plugin.messageUtils.send(player, plugin.messageUtils.format("&e" + plugin.messageUtils.capitalize(temp.getMobType()) + " Totem &7has been created!"));
    }

    public void totemLoadCheck() {
        // Iterate through all current totems
        for (Map.Entry<String, Totem> entry : totemMap.entrySet()) {
            Totem totem = entry.getValue();
            if (totem == null) continue;
            // Check if there are players in the totem's world
            if (totem.getWorld().getPlayers().size() == 0) return; // If world has players
            // Check if totem is located in a loaded chunk
            if (!totem.getWorld().isChunkLoaded(totem.getCoreBlock().getChunk())) return;
            for (Player player : totem.getWorld().getPlayers()) {
                // Check if any players are near the totem
                if (!isPlayerNearTotem(player, totem)) return;
                if (totem.getSpawnTask() == null) // Check if totem is already spawning mobs
                    spawningManager.createWaveTask(totem);
//                else if (totem.getSpawnTask() != null) { // Cancel the spawning task if there are no players nearby
//                    totem.getSpawnTask().cancel();
//                }
            }
        }
    }

    public boolean isPlayerNearTotem(Player player, Totem totem) {
        return totem.getCoreBlock().distanceSquared(player.getLocation()) <= 400;
    }
}
