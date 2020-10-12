package me.endergamingfilms.totemspawners.utils;

import me.endergamingfilms.totemspawners.TotemSpawners;
import me.endergamingfilms.totemspawners.managers.Tiers;
import me.endergamingfilms.totemspawners.managers.Totem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
        setupTotems();
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
            newTier.setCustomName(String.valueOf(tiers.get(str + ".Name") ));
            Material mat = Material.matchMaterial(String.valueOf(tiers.get(str + ".Block-Type")));
            newTier.setBlockMaterial(mat);
            newTier.setDamageMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Damage-Modifier"))));
            newTier.setHealthMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Health-Modifier"))));
//            newTier.setArmorMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Armor-Modifier"))));
            newTier.setSpeedMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Speed-Modifier"))));
            // Check if there is a "flags" list
            if (tiers.getList(str + ".Flags") != null) {
                newTier.setFlags(tiers.getStringList(str + ".Flags"));
            }

//            newTier.setKnockbackMod(Double.parseDouble(String.valueOf(tiers.get(str + ".Knockback-Modifier"))));
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
            tiers.set(str + ".Name", tier.getCustomName()); //.replace("§", "&"));
            tiers.set(str + ".Block-Type", tier.getBlockMaterial().getKey().getKey());
            tiers.set(str + ".Damage-Modifier", tier.getDamageMod());
            tiers.set(str + ".Health-Modifier", tier.getHealthMod());
//            tiers.set(str + ".Armor-Modifier", tier.getArmorMod());
            tiers.set(str + ".Speed-Modifier", tier.getSpeedMod());
//            tiers.set(str + ".Knockback-Modifier", tier.getKnockbackMod());
            if (tier.getFlags() != null) {
//                tiers.set(str + ".Flags", tier.convertFlagsToString());
                tiers.set(str + ".Flags", tier.getFlags());
            }
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
     * |-------------- Totems.yml --------------|
     */
    public void setupTotems() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        totemsFile = new File(plugin.getDataFolder(), "totems.yml");

        if (!totemsFile.exists()) {
            try {
                totemsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.messageUtils.log(MessageUtils.LogLevel.SEVERE, "&cThere was an issue creating totems.yml");
            }
        }
        totems = YamlConfiguration.loadConfiguration(totemsFile);
    }

    public FileConfiguration gettotems() {
        return totems;
    }

    public File gettotemsFile() {
        return totemsFile;
    }

    public void readTotems() {
        // Check if the totems.yml is empty
        try {
            BufferedReader reader = new BufferedReader(new FileReader(totemsFile));
            boolean empty = reader.readLine() == null;
            if (empty) return;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the file is not empty
        List<String> totemList = config.getStringList("TotemList");
        for (String str : totemList) {
            if (str == null) continue;
            // Create new portal object
            World world = Bukkit.getWorld(totems.getString(str + ".World"));
            Totem totem = new Totem(str, world, String.valueOf(totems.get(str + ".Spawning.type")));
            // Get Spawning Data
            try {
                totem.setSpawningPeriod(Integer.parseInt(str + ".Spawning.period"));
                totem.setMaxSpawnLimit(Integer.parseInt(str + ".Spawning.max-limit"));
                totem.setMinSpawnLimit(Integer.parseInt(str + ".Spawning.min-limit"));
                totem.setMaxSpawnRadius(Integer.parseInt(str + ".Spawning.max-radius"));
                totem.setMinSpawnRadius(Integer.parseInt(str + ".Spawning.min-radius"));
            } catch (NumberFormatException ignore) {

            }
            // Get CoreBlock Data
            Location coreBlockLoc = new Location(totem.getWorld(),
                    Double.parseDouble(String.valueOf(totems.get(str + ".CoreBlock.x"))),
                    Double.parseDouble(String.valueOf(totems.get(str + ".CoreBlock.y"))),
                    Double.parseDouble(String.valueOf(totems.get(str + ".CoreBlock.z"))));
            totem.setCoreBlock(coreBlockLoc);
            // Get TierBlock Data
            Location tierBlockLoc = new Location(totem.getWorld(),
                    Double.parseDouble(String.valueOf(totems.get(str + ".TierBlock.x"))),
                    Double.parseDouble(String.valueOf(totems.get(str + ".TierBlock.y"))),
                    Double.parseDouble(String.valueOf(totems.get(str + ".TierBlock.z"))));
            totem.setTierBlock(tierBlockLoc);
            // Assign Tier to Totem
            totem.setTier(plugin.totemManager.tierManager.getTierFromMaterial(totem.getWorld().getBlockAt(totem.getTierBlock()).getType()));
            // Add Totem to the tracking list
            plugin.totemManager.addTotem(totem);
            // Add new spawning task
            plugin.totemManager.spawningManager.createWaveTask(totem);
        }

    }

    public void saveTotems() {
        List<String> totemList = new ArrayList<>();
        plugin.totemManager.getTotemMap().forEach((k,v) -> {
            totemList.add(k);
        });
        if (totemList.isEmpty()) return; // Don't do anything if there's nothing to do
        config.set("TotemList", totemList);

        for (String str : totemList) {
            Totem totem = plugin.totemManager.getTotem(str);
            if (totem == null) continue;
            // Set World Data
            totems.set(str + ".World", totem.getWorld().getName());
            // Set Spawning Data
            totems.set(str + ".Spawning.type", totem.getMobType());
            totems.set(str + ".Spawning.period", totem.getSpawningPeriod());
            totems.set(str + ".Spawning.max-limit", totem.getMaxSpawnLimit());
            totems.set(str + ".Spawning.min-limit", totem.getMinSpawnLimit());
            totems.set(str + ".Spawning.max-radius", totem.getMaxSpawnRadius());
            totems.set(str + ".Spawning.min-radius", totem.getMinSpawnRadius());
            // Set CoreBlock Data
            totems.set(str + ".CoreBlock.x", totem.getCoreBlock().getX());
            totems.set(str + ".CoreBlock.y", totem.getCoreBlock().getY());
            totems.set(str + ".CoreBlock.z", totem.getCoreBlock().getZ());
            // Set TierBlock Data
            totems.set(str + ".TierBlock.x", totem.getTierBlock().getX());
            totems.set(str + ".TierBlock.y", totem.getTierBlock().getY());
            totems.set(str + ".TierBlock.z", totem.getTierBlock().getZ());
        }

        // Save totems & config File
        try {
            totems.save(totemsFile);
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeTotemFromFile(String path) {
        totems.set(path, null);
    }

    public void reloadTotems() {
        totems = YamlConfiguration.loadConfiguration(totemsFile);
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
        plugin.totemManager.creationManager.makeSelectionTool();
        // Stage 4 - Read in totems and tiers
        reloadTotems();
        readTotems();
        reloadTiers();
        readTiers();
    }
    //------------------------------------------
}
