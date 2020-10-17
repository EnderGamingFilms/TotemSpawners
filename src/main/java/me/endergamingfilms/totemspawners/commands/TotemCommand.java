package me.endergamingfilms.totemspawners.commands;

import me.endergamingfilms.totemspawners.TotemSpawners;
import me.endergamingfilms.totemspawners.managers.Totem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import scala.$eq$colon$eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TotemCommand extends BaseCommand {
    private final TotemSpawners plugin;

    public TotemCommand(String command, @NotNull final TotemSpawners instance, String... aliases) {
        super(command, null, null, null, Arrays.asList(aliases));
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.messageUtils.send(sender, plugin.respond.nonPlayer());
            return false;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            plugin.messageUtils.send(player, plugin.respond.getHelp(player));
            return false;
        }

        if (args[0].matches("rl|reload")) {
            plugin.cmdManager.reloadCmd.run(player);
        } else if (args[0].equalsIgnoreCase("create")) {
            plugin.cmdManager.createCmd.run(player, args);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (!player.hasPermission("totemspawners.command.list")) {
                plugin.messageUtils.send(player, plugin.respond.noPerms());
                return false;
            }
            plugin.messageUtils.send(player, plugin.respond.listTotems());
        } else if (args[0].equalsIgnoreCase("test")) { // --------- Test Command --------- \\
            // TODO: Test Code Here
            if (!player.hasPermission("totemspawners.*")) {
                plugin.messageUtils.send(player, plugin.respond.noPerms());
                return false;
            }
            if (args.length > 2) {
                if (args[1].equalsIgnoreCase("id")) {
                    plugin.messageUtils.send(player, "ID: " + plugin.totemManager.getTotem(args[2]).getSpawnTask().getTaskId());
                } else if (args[1].equalsIgnoreCase("canned")) {
                    plugin.messageUtils.send(player, "IsRunning? " + plugin.totemManager.getTotem(args[2]).getSpawnTask().isCancelled());
                } else if (args[1].equalsIgnoreCase("save")) {
                    plugin.fileManager.saveTotems();
                }
            }
        } else if (args[0].matches("remove")) {
            plugin.cmdManager.deleteCmd.run(player, args);
        } else {
            plugin.messageUtils.send(player, plugin.respond.getHelp(player));
            return false;
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (plugin.fileManager.debug) System.out.println("--> args: " + Arrays.toString(args) + "  |  " + args.length);
        if (args.length == 1) {
            return plugin.cmdManager.subCommandList;
        }

        List<String> commandNeedsTotem = Arrays.asList("remove");
        if (args.length == 2 && commandNeedsTotem.contains(args[0])) {
                List<String> totemList = new ArrayList<>();
                for (Map.Entry<String, Totem> entry : plugin.totemManager.getTotemMap().entrySet()) {
                    String totemName = entry.getKey();
                    if (totemName != null) totemList.add(totemName);
                }
                return totemList;
        }
        return null;
    }
}
