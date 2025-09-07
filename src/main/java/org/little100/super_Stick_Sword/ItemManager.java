package org.little100.super_Stick_Sword;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemManager {

    public static ItemStack COMPRESSED_STICK_1;
    public static ItemStack COMPRESSED_STICK_2;
    public static ItemStack COMPRESSED_STICK_3;
    public static ItemStack COMPRESSED_STICK_4;
    public static ItemStack COMPRESSED_STICK_5;
    public static ItemStack COMPRESSED_STICK_6;
    public static ItemStack COMPRESSED_STICK_7;
    public static ItemStack COMPRESSED_STICK_8;
    public static ItemStack SUPER_STICK_SWORD;
    
    private static LanguageManager languageManager;
    private static Super_Stick_Sword plugin;

    public static void init(Super_Stick_Sword pluginInstance, LanguageManager langManager) {
        plugin = pluginInstance;
        languageManager = langManager;
        
        COMPRESSED_STICK_1 = createCompressedStick(plugin, 1, "compressed_stick_1");
        COMPRESSED_STICK_2 = createCompressedStick(plugin, 2, "compressed_stick_2");
        COMPRESSED_STICK_3 = createCompressedStick(plugin, 3, "compressed_stick_3");
        COMPRESSED_STICK_4 = createCompressedStick(plugin, 4, "compressed_stick_4");
        COMPRESSED_STICK_5 = createCompressedStick(plugin, 5, "compressed_stick_5");
        COMPRESSED_STICK_6 = createCompressedStick(plugin, 6, "compressed_stick_6");
        COMPRESSED_STICK_7 = createCompressedStick(plugin, 7, "compressed_stick_7");
        COMPRESSED_STICK_8 = createCompressedStick(plugin, 8, "compressed_stick_8");
        SUPER_STICK_SWORD = createSuperStickSword(plugin);
    }

    private static ItemStack createCompressedStick(Super_Stick_Sword plugin, int level, String itemKey) {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = languageManager.getItemName(itemKey);
            meta.setDisplayName("§r" + name);
            meta.setCustomModelData(level);
            NamespacedKey key = new NamespacedKey(plugin, "custom_item");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "compressed_stick_" + level);

            try {
                meta.getClass().getMethod("setTranslationKey", String.class).invoke(meta, "item.super_stick_sword.compressed_stick_" + level);
            } catch (Exception e) {
                // 草 走 忽略
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createSuperStickSword(Super_Stick_Sword plugin) {
        ItemStack item = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String name = languageManager.getItemName("super_stick_sword");
            String lore = languageManager.getMessage("items.super_stick_sword_lore");
            
            meta.setDisplayName("§c" + name);
            meta.setCustomModelData(1);
            meta.setLore(Collections.singletonList(lore));
            
            NamespacedKey key = new NamespacedKey(plugin, "custom_item");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "super_stick_sword");
            
            try {
                meta.getClass().getMethod("setTranslationKey", String.class).invoke(meta, "item.super_stick_sword.super_stick_sword");
            } catch (Exception e) {
                // 草 走 忽略
            }
            
            item.setItemMeta(meta);
        }
        return item;
    }
    
    public static void updatePlayerItems(Player player) {
        // 更新玩家物品栏
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                ItemStack updatedItem = updateItemIfCustom(item, player);
                if (updatedItem != null) {
                    player.getInventory().setItem(i, updatedItem);
                }
            }
        }
        
        // 更新装备栏
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                ItemStack updatedItem = updateItemIfCustom(item, player);
                if (updatedItem != null) {
                    item.setItemMeta(updatedItem.getItemMeta());
                }
            }
        }
        
        // 更新副手
        ItemStack offHandItem = player.getInventory().getItemInOffHand();
        if (offHandItem != null && offHandItem.getType() != Material.AIR) {
            ItemStack updatedItem = updateItemIfCustom(offHandItem, player);
            if (updatedItem != null) {
                player.getInventory().setItemInOffHand(updatedItem);
            }
        }
        
        player.updateInventory();
    }
    
    private static ItemStack updateItemIfCustom(ItemStack item, Player player) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        
        NamespacedKey key = new NamespacedKey(plugin, "custom_item");
        String customItemType = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        
        if (customItemType == null) {
            return null;
        }
        if (customItemType.startsWith("compressed_stick_")) {
            int level = Integer.parseInt(customItemType.substring("compressed_stick_".length()));
            String name = languageManager.getItemName(player, "compressed_stick_" + level);
            meta.setDisplayName("§r" + name);
            item.setItemMeta(meta);
            return item;
        } else if (customItemType.equals("super_stick_sword")) {
            String name = languageManager.getItemName(player, "super_stick_sword");
            String lore = languageManager.getMessage(player, "items.super_stick_sword_lore");
            
            meta.setDisplayName("§c" + name);
            meta.setLore(Collections.singletonList(lore));
            item.setItemMeta(meta);
            return item;
        }
        
        return null;
    }
}