package net.lotrix.profile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.lotrix.profile.punishments.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Profile {

    private final UUID uniqueId;
    private final Set<Punishment> punishments;

    public Profile(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.punishments = new HashSet<>();
    }

    public Set<Punishment> getPunishments() {
        return punishments;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }
}
