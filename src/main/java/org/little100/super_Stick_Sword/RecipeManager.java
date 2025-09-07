package org.little100.super_Stick_Sword;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeManager {

    public static void init(Super_Stick_Sword plugin) {
        addInitialRecipes(plugin);
    }

    private static void addInitialRecipes(Super_Stick_Sword plugin) {
        ShapelessRecipe recipe1 = new ShapelessRecipe(new NamespacedKey(plugin, "compressed_stick_1"), ItemManager.COMPRESSED_STICK_1);
        recipe1.addIngredient(9, Material.STICK);
        Bukkit.addRecipe(recipe1);
    }
}