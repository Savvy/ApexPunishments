package io.apexcreations.apexbans.punishments;

import io.apexcreations.apexbans.ApexBans;

import java.sql.SQLException;
import java.util.UUID;

public interface Punishment {

    String getTable();

    UUID getUniqueId();

    String getReason();

    long getDuration();

    String getPunisher();

    long getCreated();

    void setActive(boolean active);

    boolean isActive();

    default long getElapsed() {
        return getDuration() - ((System.currentTimeMillis() - getCreated()) / 1000);
    }

    default void save() throws SQLException {
        long time = (getDuration() == -1) ? Long.MAX_VALUE : (System.currentTimeMillis() - getCreated()) / 1000;
        if (time <= getDuration()) {
            ApexBans.getDatabase().executeUpdate("REPLACE INTO `" + getTable() + "`" +
                    "(uniqueId, reason, punisher, duration, startTime, active)" +
                    "VALUES (?, ?, ?, ?, ?, ?);", getUniqueId().toString(), getReason(),
                    getPunisher(), getDuration(), getCreated(), isActive());
        }
    }
}