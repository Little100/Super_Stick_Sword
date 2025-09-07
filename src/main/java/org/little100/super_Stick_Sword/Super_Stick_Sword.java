package org.little100.super_Stick_Sword;

import org.bukkit.plugin.java.JavaPlugin;
import org.little100.super_Stick_Sword.commands.LanguageCommand;

public final class Super_Stick_Sword extends JavaPlugin {

    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        languageManager = new LanguageManager(this);
        
        ItemManager.init(this, languageManager);
        RecipeManager.init(this);
        getServer().getPluginManager().registerEvents(new PluginListener(this), this);
        
        getCommand("sss").setExecutor(new LanguageCommand(this));
        getCommand("sss").setTabCompleter(new LanguageCommand(this));
        
        getLogger().info("Super Stick Sword has been enabled!");
    }
    
    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("Super Stick Sword has been disabled!");
    }
}
