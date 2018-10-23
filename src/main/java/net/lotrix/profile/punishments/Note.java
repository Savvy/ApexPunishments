package net.lotrix.profile.punishments;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class Note {

    private final LinkedHashSet<String> notes;

    public Note() {
        this.notes = new LinkedHashSet<>();
    }

    public void addNote(String string) {
        notes.add(string);
    }

    public boolean removeNote(int index) {
        int i = 1;
        Iterator<String> noteItr = notes.iterator();
        while (noteItr.hasNext()) {
            if (index == i) {
                noteItr.remove();
                return true;
            }
            i++;
        }
        return false;
    }
}
