package net.lotrix;

import co.aikar.idb.BukkitDB;
import io.apexcreations.apexbans.utils.Messages;
import net.lotrix.profile.ProfileHandler;
import net.lotrix.rules.RuleHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class PunishmentPlugin extends JavaPlugin {

    private final ProfileHandler profileHandler = new ProfileHandler();
    private final RuleHandler ruleHandler = new RuleHandler();

    public void onEnable() {
        saveDefaultConfig();

        Messages.init(this);

        BukkitDB.createHikariDatabase(this, getConfig().getString("mysql.username"),
                getConfig().getString("mysql.password"), getConfig().getString("mysql.database"),
                getConfig().getString("mysql.hostname"));

    }

    @Override
    public void onDisable() {
        this.profileHandler.disposeAll();
    }

    public ProfileHandler getProfileHandler() {
        return profileHandler;
    }

    public RuleHandler getRuleHandler() {
        return ruleHandler;
    }
}
