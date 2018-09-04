package io.apexcreations.apexbans.utils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TimeParser {

    public static long parseTime(String time) {
        Duration duration;

        if (time.equalsIgnoreCase("perm")) {
            duration = Duration.ofMillis(Long.MAX_VALUE);
        } else if (time.contains("D") || time.contains("d")) {
            duration = Duration.parse("P".concat(time).toUpperCase());
        } else {
            duration = Duration.parse("PT".concat(time).toUpperCase());
        }

        return duration.getSeconds();
    }

    public static String toString(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) -
                TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                TimeUnit.DAYS.toMinutes(day) -
                TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                TimeUnit.DAYS.toSeconds(day) -
                TimeUnit.HOURS.toSeconds(hours) -
                TimeUnit.MINUTES.toSeconds(minute);
        return day + " day(s) " + hours + " hour(s) " + minute + " minute(s) " + second + " second(s)";
    }
}