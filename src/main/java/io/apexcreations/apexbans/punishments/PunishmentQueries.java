package io.apexcreations.apexbans.punishments;

public enum PunishmentQueries {

    CREATE_MUTE_TABLE("CREATE TABLE IF NOT EXISTS `apex_mutes` (" +
            " id INT(11) PRIMARY KEY  AUTO_INCREMENT," +
            " uniqueId VARCHAR(36) NOT NULL," +
            " reason VARCHAR(32) NOT NULL," +
            " punisher VARCHAR(16) NOT NULL," +
            " duration BIGINT NOT NULL," +
            " startTime BIGINT NOT NULL," +
            " active TINYINT NOT NULL" +
            ");"),
    CREATE_BAN_TABLE("CREATE TABLE IF NOT EXISTS `apex_bans` (" +
            " id INT(11) PRIMARY KEY AUTO_INCREMENT," +
            " uniqueId VARCHAR(36) NOT NULL," +
            " reason VARCHAR(32) NOT NULL," +
            " punisher VARCHAR(16) NOT NULL," +
            " duration BIGINT NOT NULL," +
            " startTime BIGINT NOT NULL," +
            " active TINYINT NOT NULL" +
            ");");

    private String sql;

    PunishmentQueries(String sql) {
        this.sql = sql;
    }

    public String getQuery() {
        return sql;
    }
}
