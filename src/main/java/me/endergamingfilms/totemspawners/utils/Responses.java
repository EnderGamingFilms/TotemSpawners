package me.endergamingfilms.totemspawners.utils;

import me.endergamingfilms.totemspawners.TotemSpawners;
import me.endergamingfilms.totemspawners.managers.Totem;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static me.endergamingfilms.totemspawners.utils.MessageUtils.NL;
import static me.endergamingfilms.totemspawners.utils.MessageUtils.SPACE;

public class Responses {
    private final TotemSpawners plugin;

    public Responses(@NotNull final TotemSpawners instance) {
        plugin = instance;
    }

    /**
     * |-------------- Basic Responses --------------|
     */
    public String noPerms() {
        return plugin.messageUtils.getFormattedMessage("no-permission.message",
                plugin.fileManager.getMessages().getBoolean("no-permission.prefix"));
    }

    public String pluginReload(final long time) {
        return plugin.messageUtils.format("&dAll config files have been reloaded. &7(" + time + "ms)");
    }

    public String nonPlayer() {
        return plugin.messageUtils.getFormattedMessage("non-player");
    }

    public TextComponent listTotems() {
        TextComponent message = new TextComponent();
        message.addExtra(plugin.messageUtils.colorize("&6&lTotem List"));
        for (Map.Entry<String, Totem> entry : plugin.totemManager.getTotemMap().entrySet()) {
            if (entry == null) continue;
            message.addExtra(plugin.messageUtils.colorize(NL + "&7&m                        ") + NL);
            message.addExtra(plugin.messageUtils.getFormattedMessage("totem-list.name", entry.getValue().getTotemName(), false) + NL);
            message.addExtra(plugin.messageUtils.getFormattedMessage("totem-list.tier", entry.getValue().getTier().getCustomName(), false)
                    + plugin.messageUtils.colorize(" &r&7(" + entry.getValue().getTier().getIdentifier() + ")") + NL);
            TextComponent tpLoc = new TextComponent();
            String location = entry.getValue().getCoreBlock().getX() + SPACE + entry.getValue().getCoreBlock().getY() + SPACE + entry.getValue().getCoreBlock().getZ();
            tpLoc.addExtra(plugin.messageUtils.colorize("&eLocation: &7" + location + SPACE +"(" + entry.getValue().getWorld().getName() + ")"));
            tpLoc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + plugin.messageUtils.grabRaw("totem-list.tp-command")
                    + SPACE + location + SPACE + entry.getValue().getWorld().getName()));
            message.addExtra(tpLoc);
        }
        return message;
    }

    //------------------------------------------

    /**
     * |-------------- Totem Responses --------------|
     */
    public String totemExists() {
        return plugin.messageUtils.getFormattedMessage("totem-exists");
    }

    public String totemDoesNotExists() {
        return plugin.messageUtils.getFormattedMessage("totem-missing");
    }

    public String totemCreationTimeout() {
        return plugin.messageUtils.getFormattedMessage("totem-creation-timeout");
    }

    public String totemDeleted() {
        return plugin.messageUtils.getFormattedMessage("totem-deletion");
    }

    //------------------------------------------

    /**
     * |-------------- Help/Usage Responses --------------|
     */
    public String getHelp(final String cmd) {
        return plugin.messageUtils.getFormattedMessage("help." + cmd, false);
    }

    public TextComponent getHelp(Player player) {
        TextComponent message = new TextComponent();
        message.addExtra(plugin.messageUtils.getFormattedMessage("help.header", false) + NL);
        if (player.hasPermission("totemspawners.reload"))
            message.addExtra(getHelp("reload") + NL);
        if (player.hasPermission("totemspawners.command.create"))
            message.addExtra(getHelp("create") + NL);
        if (player.hasPermission("totemspawners.command.remove"))
            message.addExtra(getHelp("remove") + NL);
        if (player.hasPermission("totemspawners.command.list"))
            message.addExtra(getHelp("list") + NL);
        message.addExtra(plugin.messageUtils.colorize("       &7Author: " + plugin.getDescription().getAuthors().get(0) +
                "&7       |       Version: " + plugin.getDescription().getVersion()));
        return message;
    }
    //------------------------------------------

}
