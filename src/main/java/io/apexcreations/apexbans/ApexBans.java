package io.apexcreations.apexbans;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.Database;
import io.apexcreations.CommandHandler;
import io.apexcreations.apexbans.commands.BanCommand;
import io.apexcreations.apexbans.commands.MuteCommand;
import io.apexcreations.apexbans.commands.UnbanCommand;
import io.apexcreations.apexbans.commands.UnmuteCommand;
import io.apexcreations.apexbans.listeners.PlayerListeners;
import io.apexcreations.apexbans.players.PunishedPlayer;
import io.apexcreations.apexbans.punishments.PunishmentQueries;
import io.apexcreations.apexbans.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ApexBans extends JavaPlugin {

    private static ApexBans instance;
    private static Database database;
    private CommandHandler commandHandler;

    private Map<UUID, PunishedPlayer> players = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        Messages.init(this);

        saveDefaultConfig();

        database = BukkitDB.createHikariDatabase(this, getConfig().getString("mysql.username"),
                getConfig().getString("mysql.password"), getConfig().getString("mysql.database"),
                getConfig().getString("mysql.hostname"));

        try {
            database.executeUpdate(PunishmentQueries.CREATE_MUTE_TABLE.getQuery());
            database.executeUpdate(PunishmentQueries.CREATE_BAN_TABLE.getQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("unban").setExecutor(new UnbanCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    @Override
    public void onDisable() {
        database.close();
        instance = null;
    }

    public boolean containsPlayer(UUID id) {
        return players.containsKey(id);
    }

    public PunishedPlayer getPlayer(UUID id) {
        return players.get(id);
    }

    public void addPlayer(PunishedPlayer player) {
        this.players.put(player.getUniqueId(), player);
    }

    public static ApexBans getInstance() {
        return instance;
    }

    public static Database getDatabase() {
        return database;
    }
}