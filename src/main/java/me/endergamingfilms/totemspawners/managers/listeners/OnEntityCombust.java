package me.endergamingfilms.totemspawners.managers.listeners;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnEntityCombust implements Listener {

    private final TotemSpawners plugin;

    public OnEntityCombust(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    // TODO: if the mob combusting has canBurn=false. ==> Cancel the event.
    @EventHandler
    void onEntityCombust (EntityCombustEvent event) {
        PersistentDataContainer container = event.getEntity().getPersistentDataContainer();
        if (!container.has(plugin.totemManager.spawningManager.canBurn, PersistentDataType.STRING)) return;
        if (Objects.requireNonNull(container.get(plugin.totemManager.spawningManager.canBurn,
                PersistentDataType.STRING)).equalsIgnoreCase("false")) {
            event.setCancelled(true);
        }
    }
}
