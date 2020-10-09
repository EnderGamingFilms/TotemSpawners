package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TierManager {
    private final TotemSpawners plugin;
    private final Map<String, Tiers> tierMap = new HashMap<>();

    public TierManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    public Tiers getTier(String identifier) {
        return tierMap.get(identifier);
    }

    public Tiers getTierFromMaterial(Material material) {
        for (Map.Entry<String, Tiers> entry : tierMap.entrySet()) {
            Tiers tier = entry.getValue();
            if (tier == null) continue;
            // Check if material matches type in tier
            if (tier.getBlockMaterial() == material) {
                return tier;
            }
        }
        return null;
    }

    public void add(Tiers tier) {
        tierMap.put(tier.getIdentifier(), tier);
    }

    public Map<String, Tiers> getTierMap() {
        return tierMap;
    }
}
