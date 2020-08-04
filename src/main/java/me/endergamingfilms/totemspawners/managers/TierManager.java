package me.endergamingfilms.totemspawners.managers;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TierManager {
    private final TotemSpawners plugin;
    private final Map<String, Tiers> tierMap = new HashMap<>();

    public TierManager(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    public Tiers getTier(String identifier) {
        return tierMap.get(identifier);
    }

    public void add(Tiers tier) {
        tierMap.put(tier.getIdentifier(), tier);
    }

    public Map<String, Tiers> getTierMap() {
        return tierMap;
    }
}
