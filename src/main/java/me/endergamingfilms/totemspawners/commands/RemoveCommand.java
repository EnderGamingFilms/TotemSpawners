package me.endergamingfilms.totemspawners.commands;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand {
    private final TotemSpawners plugin;

    public RemoveCommand(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    public void run(Player player, String[] args) {
        if (!player.hasPermission("totemspawnerscommand.remove")) {
            plugin.messageUtils.send(player, plugin.respond.noPerms());
            return;
        }

        if (args.length < 2) {
            plugin.messageUtils.send(player, plugin.respond.getHelp("remove"));
            return;
        }

//        if (plugin.portalManager.getActivePortals().containsKey(args[1])) {
//            plugin.portalManager.removePortal(args[1]);
//            plugin.fileManager.removePortalFromFile(args[1]);
//            // Send success message
//            plugin.messageUtils.send(player, plugin.respond.gatewayDeleted());
//        } else
//            plugin.messageUtils.send(player, plugin.respond.gatewayDoesNotExists());
    }
}
