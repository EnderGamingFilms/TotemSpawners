package me.endergamingfilms.totemspawners.utils;

import me.endergamingfilms.totemspawners.TotemSpawners;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.endergamingfilms.totemspawners.utils.MessageUtils.NL;

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
        if (player.hasPermission("totemspawners.command.remove"))
            message.addExtra(getHelp("list") + NL);
        message.addExtra(plugin.messageUtils.colorize("       &7Author: " + plugin.getDescription().getAuthors().get(0) +
                "&7       |       Version: " + plugin.getDescription().getVersion()));
        return message;
    }
    //------------------------------------------

}
