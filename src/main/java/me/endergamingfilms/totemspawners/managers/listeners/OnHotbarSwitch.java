package me.endergamingfilms.totemspawners.managers.listeners;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class OnHotbarSwitch implements Listener {
    private final TotemSpawners plugin;
    private final Map<UUID, Integer> tasks = new HashMap<>();

    public OnHotbarSwitch(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    @EventHandler
    void onHotBarSwitch(PlayerItemHeldEvent event) {
        if (!event.getPlayer().getInventory().contains(plugin.totemManager.creationManager.getSelectionTool())) return;
        if (event.getPlayer().getInventory().getItem(event.getPreviousSlot()) == null) return;
        if (event.getPlayer().getInventory().getItem(event.getNewSlot()) == null) return;

        if (!tasks.isEmpty()) {
            if (event.getPlayer().getInventory().getItem(event.getNewSlot()).getItemMeta().getPersistentDataContainer().get(plugin.totemManager.creationManager.key, PersistentDataType.STRING) == plugin.totemManager.creationManager.getSelectionTool().getItemMeta().getPersistentDataContainer().get(plugin.totemManager.creationManager.key, PersistentDataType.STRING)) {
                if (tasks.containsKey(event.getPlayer().getUniqueId())) {
                    // Cancel the cancel
                    Bukkit.getScheduler().cancelTask(tasks.get(event.getPlayer().getUniqueId()));
                    return;
                }
            }
        }

        PersistentDataContainer container = Objects.requireNonNull(event.getPlayer().getInventory()
                .getItem(event.getPreviousSlot())).getItemMeta().getPersistentDataContainer();
        // Check if held item is the selectionTool
        if (Objects.equals(container.get(plugin.totemManager.creationManager.key, PersistentDataType.STRING),
                Objects.requireNonNull(plugin.totemManager.creationManager.getSelectionTool().getItemMeta())
                        .getPersistentDataContainer().get(plugin.totemManager.creationManager.key, PersistentDataType.STRING))) {
            // Begin creation cancellation
            int taskID2 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    int taskID = plugin.totemManager.creationManager.getCreationTasks().get(event.getPlayer().getUniqueId());
                    plugin.totemManager.creationManager.cancelCreation(event.getPlayer());
                    // Cancel runnable task
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    // Send message
                    plugin.messageUtils.send(event.getPlayer(), plugin.messageUtils.format("&cGateway creation has been cancelled."));
                }
            }, 2 * 20L);
            tasks.put(event.getPlayer().getUniqueId(), taskID2);
        }
    }
}
