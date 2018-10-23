package net.lotrix.profile.punishments;

import java.util.UUID;
import net.lotrix.rules.Rule;

public class Punishment {

    private final int id;
    private final long createdAt;
    private final UUID punisher;
    private final Rule rule;
    private final Note note;
    private boolean active;

    public Punishment(int id, UUID punisher, Rule rule,
            long createdAt, boolean active) {
        this.id = id;
        this.punisher = punisher;
        this.rule = rule;
        this.createdAt = createdAt;
        this.active = active;
        this.note = new Note();
    }

    public int getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }


    public Rule getRule() {
        return rule;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Note getNote() {
        return note;
    }

    public UUID getPunisher() {
        return punisher;
    }
}
