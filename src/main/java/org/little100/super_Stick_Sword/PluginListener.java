package org.little100.super_Stick_Sword;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class PluginListener implements Listener {

    private final Super_Stick_Sword plugin;

    private LanguageManager languageManager;

    public PluginListener(Super_Stick_Sword plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (isSuperStickSword(itemInHand)) {
                event.setDamage(100.0);
            }
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        if (isSuperStickSword(event.getFuel())) {
            event.setBurnTime(1000000);
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();

        if (tryCustomCrafting(inventory)) {
            return;
        }

        blockVanillaCrafting(inventory);
    }

    private boolean tryCustomCrafting(CraftingInventory inventory) {
        ItemStack[] matrix = inventory.getMatrix();

        for (int i = 1; i <= 7; i++) {
            if (isMatrixOfSingleStickType(matrix, i, 9)) {
                inventory.setResult(getCompressedStickByLevel(i + 1));
                return true;
            }
        }

        if (isSuperSwordRecipe(matrix)) {
            inventory.setResult(ItemManager.SUPER_STICK_SWORD);
            return true;
        }

        return false;
    }

    private void blockVanillaCrafting(CraftingInventory inventory) {
        if (inventory.getRecipe() == null) return;

        if (!inventory.getRecipe().getResult().getType().getKey().getNamespace().equals("minecraft")) {
            return;
        }

        for (ItemStack item : inventory.getMatrix()) {
            if (item != null && item.getType() == Material.STICK && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasCustomModelData()) {
                    int cmd = meta.getCustomModelData();
                    if (cmd >= 1 && cmd <= 8) {
                        inventory.setResult(null);
                        return;
                    }
                }
            }
        }
    }

    private boolean isMatrixOfSingleStickType(ItemStack[] matrix, int requiredModel, int requiredCount) {
        int count = 0;
        for (ItemStack item : matrix) {
            if (item != null && item.getType() != Material.AIR) {
                if (isStickWithModel(item, requiredModel)) {
                    count++;
                } else {
                    return false;
                }
            }
        }
        return count == requiredCount;
    }

    private boolean isSuperSwordRecipe(ItemStack[] matrix) {
        int stick8Count = 0;
        int vanillaStickCount = 0;
        int otherItemCount = 0;

        for (ItemStack item : matrix) {
            if (item != null && item.getType() != Material.AIR) {
                if (isStickWithModel(item, 8)) {
                    stick8Count++;
                } else if (item.getType() == Material.STICK && (!item.hasItemMeta() || !item.getItemMeta().hasCustomModelData())) {
                    vanillaStickCount++;
                } else {
                    otherItemCount++;
                }
            }
        }
        return stick8Count == 2 && vanillaStickCount == 1 && otherItemCount == 0;
    }

    private boolean isStickWithModel(ItemStack item, int model) {
        if (item == null || item.getType() != Material.STICK || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && meta.getCustomModelData() == model;
    }

    private boolean isSuperStickSword(ItemStack item) {
        if (item == null || item.getType() != Material.WOODEN_SWORD || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return "super_stick_sword".equals(meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "custom_item"), PersistentDataType.STRING));
    }
    
    private ItemStack getCompressedStickByLevel(int level) {
        switch (level) {
            case 2: return ItemManager.COMPRESSED_STICK_2;
            case 3: return ItemManager.COMPRESSED_STICK_3;
            case 4: return ItemManager.COMPRESSED_STICK_4;
            case 5: return ItemManager.COMPRESSED_STICK_5;
            case 6: return ItemManager.COMPRESSED_STICK_6;
            case 7: return ItemManager.COMPRESSED_STICK_7;
            case 8: return ItemManager.COMPRESSED_STICK_8;
            default: return null;
        }
    }
}