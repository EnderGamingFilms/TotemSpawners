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

    public String totemWrongKey() {
        return plugin.messageUtils.getFormattedMessage("totem-invalid-key");
    }

    public String totemOpeningMessage(final String name) {
        return plugin.messageUtils.getFormattedMessage("totem-opening-soon", name);
    }

    public String totemOpened(final String name) {
        return plugin.messageUtils.getFormattedMessage("totem-opened", name);
    }

    public String totemClosed(final String name) {
        return plugin.messageUtils.getFormattedMessage("totem-closed", name);
    }

    public String totemAlreadyOpen() {
        return plugin.messageUtils.getFormattedMessage("totem-already-opened");
    }

    public String totemAlreadyClosed() {
        return plugin.messageUtils.getFormattedMessage("totem-already-closed");
    }

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

    public String totemKeyUsed() {
        return plugin.messageUtils.getFormattedMessage("totem-key-used");
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
        if (player.hasPermission("totemspawnersreload"))
            message.addExtra(getHelp("reload") + NL);
        if (player.hasPermission("totemspawnerscommand.create"))
            message.addExtra(getHelp("create") + NL);
        if (player.hasPermission("totemspawnerscommand.remove"))
            message.addExtra(getHelp("remove") + NL);
        if (player.hasPermission("totemspawnerscommand.remove"))
            message.addExtra(getHelp("list") + NL);
        message.addExtra(plugin.messageUtils.colorize("       &7Author: " + plugin.getDescription().getAuthors().get(0) +
                "&7       |       Version: " + plugin.getDescription().getVersion()));
        return message;
    }
    //------------------------------------------

}
