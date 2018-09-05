package io.apexcreations.apexbans.players;

import io.apexcreations.apexbans.ApexBans;

import java.sql.SQLException;
import java.util.UUID;

public class PunishmentEntry {

    private UUID uniqueId;
    private int id;
    private String punisher;
    private long duration;
    private long startTime;
    private String reason;
    private int type;

    public PunishmentEntry(int id, int type, UUID uniqueId, String punisher, String reason, long duration, long startTime) {
        this.id = id;
        this.reason = reason;
        this.duration = duration;
        this.uniqueId = uniqueId;
        this.punisher = punisher;
        this.startTime = startTime;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public long getDuration() {
        return duration;
    }

    public String getReason() {
        return reason;
    }

    public String getPunisher() {
        return punisher;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getType() {
        return type;
    }

    public long getElapsed() {
        return getDuration() - ((System.currentTimeMillis() - getStartTime()) / 1000);
    }

    public int getId() {
        return id;
    }

    public void save() throws SQLException {
        long time = (getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - getStartTime()) / 1000;
        if (time <= getDuration()) {
            ApexBans.getDatabase().executeUpdate("REPLACE INTO `punishments`" +
                    "(uniqueId, type, punisher, duration, startTime)" +
                    "VALUES (?, ?, ?, ?, ?);", uniqueId.toString(), type, punisher, duration, startTime);
        }
    }
}