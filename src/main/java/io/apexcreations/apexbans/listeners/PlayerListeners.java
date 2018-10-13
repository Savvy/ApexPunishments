package io.apexcreations.apexbans.listeners;

import io.apexcreations.apexbans.players.PunishedPlayer;
import io.apexcreations.apexbans.punishments.Punishment;
import io.apexcreations.apexbans.utils.Messages;
import io.apexcreations.apexbans.utils.TimeParser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class PlayerListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        PunishedPlayer player = PunishedPlayer.of(event.getPlayer());
        if (player.isMuted()) {
            LinkedList<Punishment> entrySet = new LinkedList<>(player.getActiveMutes());
            Punishment entry = entrySet.getFirst();
            String time = TimeParser.toString(entry.getElapsed());

            HashMap<String, String> map = new HashMap<>();
            map.put("punisher", entry.getPunisher());
            map.put("reason", (entry.getReason().isEmpty() ? "Spamming" : entry.getReason()));
            map.put("time", time);
            Messages.get().sendMessage(event.getPlayer(), "mute.activate", map);
            Messages.get().sendMessage(event.getPlayer(), "mute.timeLeft", map);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        PunishedPlayer player = PunishedPlayer.of(event.getUniqueId());
        if (player.isBanned()) {
            LinkedList<Punishment> entrySet = new LinkedList<>(player.getActiveBans());
            Punishment entry = entrySet.getFirst();
            HashMap<String, String> map = new HashMap<>();
            map.put("punisher", event.getName());
            map.put("nl", "\n");
            map.put("players", entry.getPunisher());
            map.put("reason", entry.getReason());
            map.put("time", TimeParser.toString(entry.getDuration()));
            event.setKickMessage(Messages.get().getMessage("bans.activate", map));
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        try {
            PunishedPlayer player = PunishedPlayer.of(event.getPlayer().getUniqueId());
            player.save();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}