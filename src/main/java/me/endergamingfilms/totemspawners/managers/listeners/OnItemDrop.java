package me.endergamingfilms.totemspawners.managers.listeners;

import me.endergamingfilms.totemspawners.TotemSpawners;
import me.endergamingfilms.totemspawners.managers.Totem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class OnItemDrop implements Listener {
    // TODO: When creation tool is dropped, cancel the event.
    final private TotemSpawners plugin;

    public OnItemDrop(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    @EventHandler
    void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() != plugin.totemManager.creationManager.getSelectionTool().getType()) return;
        if (event.getItemDrop().getItemStack().getItemMeta() == null) return;
        if (!event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(plugin.totemManager.creationManager.key, PersistentDataType.STRING)) return;
        event.setCancelled(true);
    }
}
