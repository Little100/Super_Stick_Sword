package org.little100.super_Stick_Sword.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.little100.super_Stick_Sword.ItemManager;
import org.little100.super_Stick_Sword.LanguageManager;
import org.little100.super_Stick_Sword.Super_Stick_Sword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageCommand implements CommandExecutor, TabCompleter {

    private final Super_Stick_Sword plugin;
    private final LanguageManager languageManager;

    public LanguageCommand(Super_Stick_Sword plugin) {
        this.plugin = plugin;
        this.languageManager = plugin.getLanguageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            // 显示帮助信息
            sender.sendMessage(ChatColor.YELLOW + "/sss language [" + String.join("|", languageManager.getAvailableLanguages()) + "]");
            return true;
        }

        String langCode = args[0].toLowerCase();
        
        // 检查语言是否可用
        if (Arrays.asList(languageManager.getAvailableLanguages()).contains(langCode)) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                languageManager.setPlayerLanguage(player, langCode);
                
                // 更新物品
                ItemManager.updatePlayerItems(player);
                
                String message = languageManager.getMessage(langCode, "commands.language_changed");
                if (message == null || message.isEmpty()) {
                    message = "Language changed to " + langCode;
                }
                player.sendMessage(ChatColor.GREEN + message);
            } else {
                languageManager.setDefaultLanguage(langCode);
                sender.sendMessage(ChatColor.GREEN + "Server default language changed to " + langCode);
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Available languages: " + String.join(", ", languageManager.getAvailableLanguages()));
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            String partialArg = args[0].toLowerCase();
            return Arrays.stream(languageManager.getAvailableLanguages())
                    .filter(lang -> lang.startsWith(partialArg))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}