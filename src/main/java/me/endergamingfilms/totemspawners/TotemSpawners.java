package me.endergamingfilms.totemspawners;

import me.endergamingfilms.totemspawners.commands.CommandManager;
import me.endergamingfilms.totemspawners.managers.TotemManager;
import me.endergamingfilms.totemspawners.utils.FileManager;
import me.endergamingfilms.totemspawners.utils.MessageUtils;
import me.endergamingfilms.totemspawners.utils.Responses;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class TotemSpawners extends JavaPlugin {
    public final FileManager fileManager = new FileManager(this);
    public final MessageUtils messageUtils = new MessageUtils(this);
    public final Responses respond = new Responses(this);
    public final CommandManager cmdManager = new CommandManager(this);
    public TotemManager totemManager;

    @Override
    public void onEnable() {
        //Load Files
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading config files.");
        fileManager.setup();

        // Register commands
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading plugin commands.");
        cmdManager.registerCommands();

        // Register Listeners/Managers
        messageUtils.log(MessageUtils.LogLevel.INFO, "&9Loading in totems.");
        totemManager = new TotemManager(this);

        // Setup Totems
        fileManager.readTiers();
        fileManager.readTotems();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        fileManager.saveTiers();
        fileManager.saveTotems();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
    }
}
