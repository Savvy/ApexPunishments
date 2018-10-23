package net.lotrix.rules;

public class Rule implements Comparable<Rule> {

    private final int severity;
    private final Type type;
    private final String name, description;
    private final long duration;

    public Rule(String name, String description, int severity, Type type, long duration) {
        this.name = name;
        this.description = description;
        this.severity = severity;
        this.type = type;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public long getElapsed(long startTime) {
        return getDuration() - ((System.currentTimeMillis() - startTime) / 1000);
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSeverity() {
        return severity;
    }

    @Override
    public int compareTo(Rule o) {
        return Integer.compare(severity, o.severity);
    }
}
