package me.endergamingfilms.totemspawners.utils;

import me.endergamingfilms.totemspawners.TotemSpawners;
import me.endergamingfilms.totemspawners.managers.Tiers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static final boolean BOOLEAN = false;
    public static final int INT = 0;
    public static final double DOUBLE = 0.0;
    public static final String STRING = "";
    public static final String[] LIST = new String[0];
    private final TotemSpawners plugin;

    public FileManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    /**
     * |-------------- Settings --------------|
     */
    public int defaultPortalOnTime;
    public boolean debug;
    public String selectionToolName;
    public String selectionToolType;
    public List<String> selectionToolLore;
    //------------------------------------------

    /**
     * |-------------- Files --------------|
     */
    private FileConfiguration config;
    private FileConfiguration messages;
    private FileConfiguration tiers;
    private FileConfiguration totems;
    private File configFile;
    private File messageFile;
    private File tiersFile;
    private File totemsFile;
    //------------------------------------------

    public void setup() {
        setupConfig();
        // Load settings
        reloadSettings();
        // Load everything else
        setupMessages();
        // Setup Tiers
        setupTiers();
        // Setup Totems
//        setupGateways();
    }

    /**
     * |-------------- Config.yml --------------|
     */
    public void setupConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", true);
                plugin.messageUtils.log(MessageUtils.LogLevel.WARNING, "&eConfig.yml did not exist so one was created");
            } catch (Exception e) {
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Config.yml");
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadSettings() {
        this.selectionToolName = plugin.messageUtils.grabConfig("CreationTool.name", STRING);
        this.selectionToolType = plugin.messageUtils.grabConfig("CreationTool.type", STRING);
        this.selectionToolLore = (List<String>) plugin.messageUtils.grabConfig("CreationTool.lore", LIST);
        this.defaultPortalOnTime = plugin.messageUtils.grabConfig("default-open-time", INT);
        this.debug = plugin.messageUtils.grabConfig("debug", BOOLEAN);
    }
    //------------------------------------------

    /**
     * |-------------- Message.yml --------------|
     */
    public void setupMessages() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        messageFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messageFile.exists()) {
            try {
                plugin.saveResource("messages.yml", true);
                plugin.messageUtils.log(MessageUtils.LogLevel.WARNING, "&eMessages.yml did not exist so one was created");
            } catch (Exception e) {
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Messages.yml");
            }
        }
        messages = YamlConfiguration.loadConfiguration(messageFile);
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public File getMessagesFile() {
        return messageFile;
    }

    public void reloadMessages() {
        messages = YamlConfiguration.loadConfiguration(messageFile);
        plugin.messageUtils.prefix = messages.getString("prefix");
    }
    //------------------------------------------

    /**
     * |-------------- Tiers.yml --------------|
     */
    public void setupTiers() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        tiersFile = new File(plugin.getDataFolder(), "tiers.yml");

        if (!tiersFile.exists()) {
            try {
                plugin.saveResource("tiers.yml", true);
                plugin.messageUtils.log(MessageUtils.LogLevel.WARNING, "&eTiers.yml did not exist so one was created");
            } catch (Exception e) {
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating Tiers.yml");
            }
        }
    tiers = YamlConfiguration.loadConfiguration(tiersFile);
}

    public FileConfiguration getTiers() {
        return tiers;
    }

    public File getTiersFile() {
        return tiersFile;
    }

    public void readTiers() {
        // Check if the tiers.yml is empty
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tiersFile));
            boolean empty = reader.readLine() == null;
            reader.close();
            if (empty) return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If the file is not empty
        List<String> tierList = config.getStringList("TierList");
        for (String str : tierList) {
            if (str == null) continue;
            // Create new Tier object
            Tiers newTier = new Tiers(str);
            // Get Tier Data
            System.out.println("--> name: " + newTier.getCustomName());
            newTier.setCustomName(String.valueOf(tiers.get(str + ".Name") ));
            System.out.println("-->str: " + str);
            Material mat = Material.matchMaterial(String.valueOf(tiers.get(str + ".Block-Type")));
            System.out.println("-->material: " + tiers.get(str + ".Block-Type"));
            newTier.setBlockMaterial(mat);
            newTier.setDamageMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Damage-Modifier"))));
            newTier.setHealthMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Damage-Modifier"))));
            newTier.setArmorMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Damage-Modifier"))));
            newTier.setSpeedMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Damage-Modifier"))));
            newTier.setSpeedMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Knockback-Modifier"))));
            plugin.totemManager.tierManager.add(newTier);
        }
    }

    public void saveTiers() {
        List<String> tierList = new ArrayList<>();
        plugin.totemManager.tierManager.getTierMap().forEach((k,v) -> {
            tierList.add(k);
        });
        // Add the tiers to the config
        config.set("TierList", tierList);

        tierList.forEach(str -> {
            Tiers tier = plugin.totemManager.tierManager.getTier(str);
            // Set Tier Data
            System.out.println("--> " + tierList.toString());
            tiers.set(str + ".Name", tier.getCustomName()); //.replace("§", "&"));
            tiers.set(str + ".Block-Type", tier.getBlockMaterial().getKey().getKey());
            tiers.set(str + ".Damage-Modifier", tier.getDamageMod());
            tiers.set(str + ".Health-Modifier", tier.getHealthMod());
            tiers.set(str + ".Armor-Modifier", tier.getArmorMod());
            tiers.set(str + ".Speed-Modifier", tier.getSpeedMod());
            tiers.set(str + ".Knockback-Modifier", tier.getKnockbackMod());
        });

        // Save Tiers & Config File
        try {
            tiers.save(tiersFile);
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTierFromFile(String path) {
        tiers.set(path, null);
    }

    public void reloadTiers() {
        tiers = YamlConfiguration.loadConfiguration(tiersFile);
    }

    //------------------------------------------

    /**
     * |-------------- General File Functions --------------|
     */
    public void reloadAll() {
        // Stage 1 - Reload messages.yml
        reloadMessages();
        // Stage 2 - Reload config.yml and load settings into plugin
        reloadConfig();
        reloadSettings();
        // Stage 3 - Remake Items
//        plugin.portalManager.selectionHandler.makeSelectionTool();
        // Stage 4 - Read in portals and keys
//        reloadGateways();
//        readGateways();
    }
    //------------------------------------------
}
