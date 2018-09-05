package io.apexcreations.apexbans.punishments;

import java.util.UUID;

public class Ban implements Punishment {

    private UUID uniqueId;
    private String reason;
    private String punisher;
    private long duration;
    private long created;
    private boolean active;

    public Ban(UUID uniqueId, String punisher, String reason, long duration, long created, boolean active) {
        this.uniqueId = uniqueId;
        this.punisher = punisher;
        this.reason = reason;
        this.duration = duration;
        this.created = created;
        this.active = active;
    }

    @Override
    public String getTable() {
        return "apex_bans";
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

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}