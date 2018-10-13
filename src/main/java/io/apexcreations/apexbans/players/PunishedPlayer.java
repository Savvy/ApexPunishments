package io.apexcreations.apexbans.players;

import co.aikar.idb.DbRow;
import com.google.common.collect.Sets;
import io.apexcreations.apexbans.ApexBans;
import io.apexcreations.apexbans.punishments.Ban;
import io.apexcreations.apexbans.punishments.Mute;
import io.apexcreations.apexbans.punishments.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PunishedPlayer {

    private UUID uniqueId;
    private Set<Punishment> punishments;

    private PunishedPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.punishments = Sets.newConcurrentHashSet();

        try {
            load();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isMuted() {
        LinkedList<Punishment> entries = new LinkedList<>(punishments);
        if (!entries.isEmpty()) {
            for (Punishment entry : entries) {
                if (entry instanceof Mute) {
                    long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getCreated()) / 1000;
                    return time <= entry.getDuration() && entry.isActive();
                }
            }
        }
        return false;
    }

    public boolean isBanned() {
        LinkedList<Punishment> entries = new LinkedList<>(punishments);
        if (!entries.isEmpty()) {
            for (Punishment entry : entries) {
                if (entry instanceof Ban) {
                    long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getCreated()) / 1000;
                    return time <= entry.getDuration() && entry.isActive();
                }
            }
        }
        return false;
    }

    public void mute(String staff, String reason, long duration) {
        try {
            Punishment punishment = new Mute(uniqueId, staff, reason, duration, System.currentTimeMillis(), true);
            punishment.save();
            punishments.add(punishment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ban(String staff, String reason, long duration) {
        try {
            Punishment punishment = new Ban(uniqueId, staff, reason, duration, System.currentTimeMillis(), true);
            punishment.save();
            punishments.add(punishment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unban() {
        for (Punishment entry : punishments) {
            try {
                if (entry instanceof Ban) {
                    entry.setActive(false);
                }
                entry.save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void unmute() {
        for (Punishment entry : punishments) {
            try {
                if (entry instanceof Mute) {
                    entry.setActive(false);
                }
                entry.save();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<Punishment> getActiveMutes() {
        Set<Punishment> entries = Sets.newConcurrentHashSet();
        for (Punishment entry : punishments) {
            if (entry instanceof Mute) {
                long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getCreated()) / 1000;
                if (time <= entry.getDuration()) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    public Set<Punishment> getActiveBans() {
        Set<Punishment> entries = Sets.newConcurrentHashSet();
        for (Punishment entry : punishments) {
            if (entry instanceof Ban) {
                long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getCreated()) / 1000;
                if (time <= entry.getDuration()) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    private void load() throws SQLException {
        List<DbRow> rowList = ApexBans.getDatabase().getResults("SELECT * FROM `apex_bans` " +
                "WHERE `uniqueId`=?;", uniqueId.toString());
        for (DbRow row : rowList) {
            int id = row.getInt("id");
            String punisher = row.getString("punisher");
            String reason = row.getString("reason");
            long startTime = row.getLong("startTime");
            long duration = row.getLong("duration");
            long time = (duration == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - startTime) / 1000;
            boolean active = row.getInt("active") == 1;
            if (time <= duration) {
                punishments.add(new Ban(uniqueId, punisher, reason, duration, startTime, active));
            }
        }
        rowList = ApexBans.getDatabase().getResults("SELECT * FROM `apex_mutes` " +
                "WHERE `uniqueId`=?;", uniqueId.toString());
        for (DbRow row : rowList) {
            int id = row.getInt("id");
            String punisher = row.getString("punisher");
            String reason = row.getString("reason");
            long startTime = row.getLong("startTime");
            long duration = row.getLong("duration");
            boolean active = row.getInt("active") == 1;
            long time = (duration == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - startTime) / 1000;
            if (time <= duration) {
                punishments.add(new Mute(uniqueId, punisher, reason, duration, startTime, active));
            }
        }
        ApexBans.getInstance().addPlayer(this);
    }

    public void save() throws SQLException {
        for (Punishment entry : punishments) entry.save();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public static PunishedPlayer of(UUID uniqueId) {
        if (ApexBans.getInstance().containsPlayer(uniqueId)) {
            return ApexBans.getInstance().getPlayer(uniqueId);
        }
        return new PunishedPlayer(uniqueId);
    }

    public static PunishedPlayer of(OfflinePlayer target) {
        return of(target.getUniqueId());
    }

    public static PunishedPlayer of(Player player) {
        return of(player.getUniqueId());
    }

    public static PunishedPlayer of(String name) {
        return of(Bukkit.getOfflinePlayer(name));
    }
}