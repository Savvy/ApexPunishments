package io.apexcreations.apexbans.utils;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

public class Messages {

    private static Messages instance;
    private final Logger logger;
    private final FileConfiguration config;

    private Messages(Plugin plugin) {
        instance = this;
        this.logger = plugin.getLogger();
        File file = new File(plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public static void init(Plugin plugin) {
        if (instance != null) {
            throw new IllegalStateException("Messages already initialized");
        }
        instance = new Messages(plugin);
    }

    public static Messages get() {
        return instance;
    }

    public void sendMessage(CommandSender sender, String message) {
        sendMessage(sender, message, Collections.emptyMap());
    }

    public void sendMessage(CommandSender sender, String message, Map<String, String> placeholders) {
        if (!isPresent(message)) {
            logger.severe("[Messages] Could not find message '" + message + "'!");
            return;
        }
        sender.sendMessage(translateAndReplace(config.getString(message), placeholders));
    }

    public String getMessage(String message, Map<String, String> placeholders) {
        if (!isPresent(message)) {
            logger.severe("[Messages] Could not find message '" + message + "'!");
            return null;
        }
        return translateAndReplace(config.getString(message), placeholders);
    }

    private boolean isPresent(String message) {
        return config.isSet(message);
    }

    private String translateAndReplace(String string, Map<String, String> placeholders) {
        return translate(StrSubstitutor.replace(string, placeholders, "%", "%"));
    }

    private String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}