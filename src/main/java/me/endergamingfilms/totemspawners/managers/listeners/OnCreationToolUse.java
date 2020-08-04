package me.endergamingfilms.totemspawners.managers.listeners;

import me.endergamingfilms.totemspawners.TotemSpawners;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OnCreationToolUse implements Listener {
    private final TotemSpawners plugin;

    public OnCreationToolUse(@NotNull final TotemSpawners instance) {
        this.plugin = instance;
    }

    @EventHandler
    void onBlockInteract(PlayerInteractEvent event) {
        // Only Run Code for main hand
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        // If no item in hand then return
        Player player = event.getPlayer();
        if ((player.getItemInHand().getType() == Material.AIR)) return;
        // If the item has no item meta then return
        if (player.getItemInHand().getItemMeta() == null) return;
        // Only Check Right Hand (main hand)
        PersistentDataContainer container = player.getItemInHand().getItemMeta().getPersistentDataContainer();
        // Check if held item is the selectionTool
        if (container.has(plugin.totemManager.creationManager.key, PersistentDataType.STRING)) {
            // Always cancel the event for the selection tool
            event.setCancelled(true);
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Block clickedBlock = event.getClickedBlock();
                if (clickedBlock != null) {
                    // Return if there are currently no creations going on
                    if (plugin.totemManager.creationManager.getCreationMap().isEmpty()) return;
                    plugin.totemManager.creationManager.getCreationMap().forEach((k, v) -> {
                        if (player.getUniqueId() == k) {
                            if (v.getCoreBlock() == null) {
                                v.setCoreBlock(clickedBlock.getLocation());
                                plugin.messageUtils.send(player, plugin.messageUtils.format("&7Please select the tier block"));
                            } else if (v.getTierBlock() == null) {
                                System.out.println("--> " + plugin.totemManager.tierManager.getTierMap().values());
                                plugin.totemManager.tierManager.getTierMap().forEach((k1, v1) -> {
                                    System.out.println("--> clicked block: " + clickedBlock.getType().getKey().getKey());
                                    System.out.println("--> tierBlock: " + v1.getBlockMaterial().getKey().getKey());
                                    if (clickedBlock.getType() != v1.getBlockMaterial()) {
                                        plugin.messageUtils.send(player, plugin.messageUtils.format("&cInvalid tier block!"));
                                    } else {
                                        v.setTierBlock(clickedBlock.getLocation());
                                    }
                                });
                            }
                        }
                    });
                }
            }
            // Check if totem is complete & take tool
            if (plugin.totemManager.creationManager.getCreationMap().get(player.getUniqueId()).getTierBlock() != null &&
                    plugin.totemManager.creationManager.getCreationMap().get(player.getUniqueId()).getCoreBlock() != null) {
                plugin.totemManager.createTotem(event.getPlayer());
            }
        }
    }
}
