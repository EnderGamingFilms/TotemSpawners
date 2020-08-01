package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TotemManager {
    // TODO: Handles all totem actions (creation / totem-tiers / totem location / wave system)
    private final TotemSpawners plugin;
    private final Map<String, Totem> totemMap = new HashMap<>();
    public final CreationManager creationManager;
    public final NamespacedKey keyFor;

    public TotemManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
        this.creationManager = new CreationManager(instance);
        this.keyFor = new NamespacedKey(instance, "keyFor");
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
}
