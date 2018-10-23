package net.lotrix.profile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileHandler {

    private Map<UUID, Profile> profiles = new HashMap<>();

    public Profile get(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    public void create(Profile profile) {
        if (has(profile)) {
            return;
        }
        profiles.put(profile.getUniqueId(), profile);
    }

    public void dispose(Profile profile) {
        if (!has(profile)) {
            return;
        }
        profiles.remove(profile.getUniqueId());
    }

    public boolean has(UUID uniqueId) {
        return profiles.containsKey(uniqueId);
    }

    public boolean has(Profile profile) {
        return has(profile.getUniqueId());
    }

    public void disposeAll() {
        profiles.values().forEach(this::dispose);
    }

    public Map<UUID, Profile> getProfiles() {
        return profiles;
    }
}
