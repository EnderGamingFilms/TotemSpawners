package me.endergamingfilms.totemspawners.commands;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateCommand {
    private final TotemSpawners plugin;

    public CreateCommand(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    public void run(Player player, String[] args) {
        if (!player.hasPermission("totemspawners.command.create")) {
            plugin.messageUtils.send(player, plugin.respond.noPerms());
            return;
        }

        if (args.length < 2) {
            plugin.messageUtils.send(player, plugin.respond.getHelp(args[0]));
            return;
        }
        plugin.totemManager.startCreation(player, args);
    }
}
