package org.little100.super_Stick_Sword;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LanguageManager {

    private final Super_Stick_Sword plugin;
    private final Map<String, YamlConfiguration> languages = new HashMap<>();
    private final Map<UUID, String> playerLanguages = new HashMap<>();
    private String defaultLanguage = "zh_cn";

    public LanguageManager(Super_Stick_Sword plugin) {
        this.plugin = plugin;
        loadLanguages();
    }

    private void loadLanguages() {
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) {
            langFolder.mkdirs();
            plugin.saveResource("lang/zh_cn.yml", false);
            plugin.saveResource("lang/en_us.yml", false);
            plugin.saveResource("lang/lzh.yml", false);
        }

        File[] langFiles = langFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (langFiles != null) {
            for (File file : langFiles) {
                String langCode = file.getName().replace(".yml", "");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                languages.put(langCode, config);
                plugin.getLogger().info("Loaded language: " + langCode);
            }
        }

        if (languages.isEmpty()) {
            loadLanguageFromResource("zh_cn");
            loadLanguageFromResource("en_us");
            loadLanguageFromResource("lzh");
        }
    }

    private void loadLanguageFromResource(String langCode) {
        InputStream is = plugin.getResource("lang/" + langCode + ".yml");
        if (is != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(is, StandardCharsets.UTF_8));
            languages.put(langCode, config);
            plugin.getLogger().info("Loaded language from resource: " + langCode);
        } else {
            plugin.getLogger().warning("Could not find language resource: " + langCode);
        }
    }

    public String getMessage(String key) {
        return getMessage(defaultLanguage, key);
    }

    public String getMessage(String lang, String key) {
        YamlConfiguration langConfig = languages.get(lang);
        if (langConfig != null && langConfig.contains(key)) {
            return langConfig.getString(key);
        }

        if (!lang.equals(defaultLanguage)) {
            langConfig = languages.get(defaultLanguage);
            if (langConfig != null && langConfig.contains(key)) {
                return langConfig.getString(key);
            }
        }

        return key;
    }

    public String getMessage(Player player, String key) {
        String lang = playerLanguages.getOrDefault(player.getUniqueId(), defaultLanguage);
        return getMessage(lang, key);
    }

    public void setPlayerLanguage(Player player, String lang) {
        if (languages.containsKey(lang)) {
            playerLanguages.put(player.getUniqueId(), lang);
        } else {
            plugin.getLogger().warning("Tried to set unknown language: " + lang + " for player " + player.getName());
        }
    }

    public String[] getAvailableLanguages() {
        return languages.keySet().toArray(new String[0]);
    }

    public void setDefaultLanguage(String lang) {
        if (languages.containsKey(lang)) {
            this.defaultLanguage = lang;
        } else {
            plugin.getLogger().warning("Tried to set unknown default language: " + lang);
        }
    }

    public String getItemName(String itemKey) {
        return getMessage("items." + itemKey);
    }

    public String getItemName(Player player, String itemKey) {
        return getMessage(player, "items." + itemKey);
    }
}