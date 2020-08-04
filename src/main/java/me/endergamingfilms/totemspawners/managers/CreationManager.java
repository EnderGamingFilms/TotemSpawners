package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import me.endergamingfilms.totemspawners.managers.listeners.OnCreationToolUse;
import me.endergamingfilms.totemspawners.managers.listeners.OnHotbarSwitch;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CreationManager {
    private final TotemSpawners plugin;
    public final NamespacedKey key;
    private final Map<UUID, Totem> creationMap = new HashMap<>();
    private final Map<UUID, Integer> creationTasks = new HashMap<>();
    private ItemStack selectionTool;

    public CreationManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
        this.key = new NamespacedKey(instance, "TotemTool");
        // Setup Selection Tool Item
        makeSelectionTool();
        // Register SelectionToolListeners
        instance.getServer().getPluginManager().registerEvents(new OnCreationToolUse(instance), instance);
        instance.getServer().getPluginManager().registerEvents(new OnHotbarSwitch(instance), instance);
    }

    public void makeSelectionTool() {
        Material toolMat = Material.getMaterial(plugin.fileManager.selectionToolType);
        String matName = plugin.fileManager.selectionToolName;
        List<String> matLore = plugin.fileManager.selectionToolLore;
        if (!matLore.isEmpty()) {
            List<String> colorized = new ArrayList<>();
            for (String s : matLore) {
                colorized.add(plugin.messageUtils.colorize(s));
            }
            matLore = colorized;
        }

        if (toolMat != null) {
            selectionTool = new ItemStack(toolMat);
            ItemMeta meta = selectionTool.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.messageUtils.colorize(matName));
            meta.setLore(matLore);
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
            selectionTool.setItemMeta(meta);
        }
    }

    public ItemStack getSelectionTool() {
        return this.selectionTool;
    }

    public void giveSelectionTool(Player player) {
        player.getInventory().addItem(selectionTool);
    }

    public void startSelection(Player player, final String[] args, final ItemStack spawnEgg) {
        int cancellationTime = 60;
        // Create new Totem object
        Totem totem = new Totem(args[1], player.getWorld(), spawnEgg.getType().getKey().getKey().split("_")[0]);
        // Add player & totem to creaitionMap
        creationMap.put(player.getUniqueId(), totem);
        // Give player the selection too & state creation process
        giveSelectionTool(player);
        plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select the core block"));
        // Put time-limit on totem creation
        int creationTaskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (player.getInventory().contains(selectionTool)) {
                    cancelCreation(player);
                    // Send timeout message
                    plugin.messageUtils.send(player, plugin.respond.totemCreationTimeout());
                }
            }
        }, cancellationTime * 20L);
        creationTasks.put(player.getUniqueId(), creationTaskID);
    }

    public void cancelCreation(Player player) {
        // Take selection tool
        takeSelectionTool(player);
        // Remove player from creation maps
        creationMap.remove(player.getUniqueId());
        creationTasks.remove(player.getUniqueId());
    }

    public void takeSelectionTool(Player player) {
        player.getInventory().remove(selectionTool);
    }

    public Map<UUID, Totem> getCreationMap() {
        return this.creationMap;
    }

    public Map<UUID, Integer> getCreationTasks() {
        return this.creationTasks;
    }
}

