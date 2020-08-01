package me.endergamingfilms.totemspawners.commands;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        } else if (args[0].equalsIgnoreCase("test")) { // --------- Test Command --------- \\
            // TODO: Test Code Here
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
        if (args.length == 1) {
            return plugin.cmdManager.subCommandList;
        }

        List<String> commandNeedsPortal = Arrays.asList("remove", "givekey");
        if (args.length == 2 && commandNeedsPortal.contains(args[0])) {
//                List<String> portalList = new ArrayList<>();
//                for (Map.Entry<String, Portal> entry : plugin.portalManager.getActivePortals().entrySet()) {
//                    String portalName = entry.getKey();
//                    if (portalName != null) portalList.add(portalName);
//                }
//                return portalList;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("givekey")) {
            List<String> players = new ArrayList<>();
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                players.add(p.getName());
            }
            return players;
        }
        return null;
    }
}
