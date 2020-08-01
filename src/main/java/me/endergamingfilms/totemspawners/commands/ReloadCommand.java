package me.endergamingfilms.totemspawners.commands;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand {
    private final TotemSpawners plugin;

    public ReloadCommand(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    public void run(Player player) {
        if (!player.hasPermission("totemspawnersreload")) {
            plugin.messageUtils.send(player, plugin.respond.noPerms());
            return;
        }

        // Reload Actions
        long start = System.currentTimeMillis();
        plugin.fileManager.reloadAll();
        long end = System.currentTimeMillis();
        // Send Response
        plugin.messageUtils.send(player, plugin.respond.pluginReload(end - start));
    }
}
