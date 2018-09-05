package io.apexcreations.apexbans.punishments;

import java.util.UUID;

public class Mute implements Punishment {

    private UUID uniqueId;
    private String reason;
    private String punisher;
    private long duration;
    private long created;

    public Mute(UUID uniqueId, String punisher, String reason, long duration, long startTime) {
        this.uniqueId = uniqueId;
        this.punisher = punisher;
        this.reason = reason;
        this.duration = duration;
        this.created = startTime;
    }

    @Override
    public String getTable() {
        return "apex_mutes";
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getReason() {
        return reason;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String getPunisher() {
        return punisher;
    }

    @Override
    public long getCreated() {
        return created;
    }
}