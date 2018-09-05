package io.apexcreations.apexbans.players;

import co.aikar.idb.DbRow;
import com.google.common.collect.Sets;
import io.apexcreations.apexbans.ApexBans;
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
    private Set<PunishmentEntry> muteEntries;
    private Set<PunishmentEntry> banEntries;

    private PunishedPlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.muteEntries = Sets.newConcurrentHashSet();
        this.banEntries = Sets.newConcurrentHashSet();

        load();
        ApexBans.getInstance().addPlayer(this);
    }

    public boolean isMuted() {
        LinkedList<PunishmentEntry> entries = new LinkedList<>(muteEntries);
        if (!entries.isEmpty()) {
            PunishmentEntry entry = entries.getFirst();
            long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getStartTime()) / 1000;
            return time <= entry.getDuration();
        }
        return false;
    }

    public boolean isBanned() {
        LinkedList<PunishmentEntry> entries = new LinkedList<>(banEntries);
        if (!entries.isEmpty()) {
            PunishmentEntry entry = entries.getFirst();
            long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getStartTime()) / 1000;
            return time <= entry.getDuration();
        }
        return false;
    }

    public void mute(String staff, String reason, long duration) {
        muteEntries.add(new PunishmentEntry(muteEntries.size() + 1, 0, uniqueId, staff, reason, duration, System.currentTimeMillis()));
    }

    public void ban(String staff, String reason, long duration) {
        banEntries.add(new PunishmentEntry(banEntries.size() + 1, 1, uniqueId, staff, reason, duration, System.currentTimeMillis()));
    }

    public Set<PunishmentEntry> getActiveMutes() {
        Set<PunishmentEntry> entries = Sets.newConcurrentHashSet();
        for (PunishmentEntry entry : muteEntries) {
            long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getStartTime()) / 1000;
            if (time <= entry.getDuration()) {
                entries.add(entry);
            }
        }
        return entries;
    }

    public Set<PunishmentEntry> getActiveBans() {
        Set<PunishmentEntry> entries = Sets.newConcurrentHashSet();
        for (PunishmentEntry entry : banEntries) {
            long time = (entry.getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - entry.getStartTime()) / 1000;
            if (time <= entry.getDuration()) {
                entries.add(entry);
            }
        }
        return entries;
    }

    public void load() {
        try {
            List<DbRow> rowList = ApexBans.getDatabase().getResults("SELECT * FROM `punishments` WHERE `uniqueId`=?;", uniqueId.toString());
            for (DbRow row : rowList) {
                int id = row.getInt("id");
                int type = row.getInt("type");
                String punisher = row.getString("punisher");
                String reason = row.getString("reason");
                long startTime = row.getLong("startTime");
                long duration  = row.getLong("duration");
                long time = (duration == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - startTime) / 1000;
                if (time <= duration) {
                    PunishmentEntry entry = new PunishmentEntry(id, type, uniqueId, punisher, reason, duration, startTime);
                    if (type == 0) muteEntries.add(entry); else banEntries.add(entry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            for (PunishmentEntry entry : muteEntries) entry.save();
            for (PunishmentEntry entry : banEntries) entry.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        return of(Bukkit.getPlayerUniqueId(name));
    }
}