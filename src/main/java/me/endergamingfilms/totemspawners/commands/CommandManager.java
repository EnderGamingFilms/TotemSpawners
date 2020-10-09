package me.endergamingfilms.totemspawners.commands;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    private final TotemSpawners plugin;
    public List<BaseCommand> commandList = new ArrayList<>();
    public List<String> subCommandList = new ArrayList<>();
    public TotemCommand gatewaysCmd;
    public ReloadCommand reloadCmd;
    public CreateCommand createCmd;
    public RemoveCommand deleteCmd;

    public CommandManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    public void registerCommands() {
        // Make Commands Accessible
        commandList.add(gatewaysCmd = new TotemCommand("totemspawners", plugin, "totems"));
        // Register Sub-Commands "/market command"
        reloadCmd = new ReloadCommand(plugin);
        subCommandList.add("reload");
        createCmd = new CreateCommand(plugin);
        subCommandList.add("create");
        deleteCmd = new RemoveCommand(plugin);
        subCommandList.add("remove");
        if (plugin.fileManager.debug) subCommandList.add("test");
//        subCommandList.add("list");
//        subCommandList.add("givekey");
        subCommandList.add("help");
        // Register BaseCommands "/command"
        for (BaseCommand command : commandList) {
            command.register();
        }
    }
}
