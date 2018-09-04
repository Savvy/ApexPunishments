package io.apexcreations.apexbans.players;

import java.util.UUID;

public class PunishmentEntry {

    private UUID uniqueId;
    private int id;
    private String punisher;
    private long duration;
    private long startTime;
    private String reason;

    public PunishmentEntry(int id, UUID uniqueId, String punisher, String reason, long duration, long startTime) {
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

    public long getElapsed() {
        return getDuration() - ((System.currentTimeMillis() - getStartTime()) / 1000);
    }

    public int getId() {
        return id;
    }
}