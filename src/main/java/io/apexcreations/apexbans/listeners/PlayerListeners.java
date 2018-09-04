package io.apexcreations.apexbans.listeners;

import io.apexcreations.apexbans.players.PunishedPlayer;
import io.apexcreations.apexbans.players.PunishmentEntry;
import io.apexcreations.apexbans.utils.Messages;
import io.apexcreations.apexbans.utils.TimeParser;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.HashMap;
import java.util.LinkedList;

public class PlayerListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        PunishedPlayer player = PunishedPlayer.of(event.getPlayer());
        if (player.isMuted()) {
            LinkedList<PunishmentEntry> entrySet = new LinkedList<>(player.getActiveMutes());
            PunishmentEntry entry = entrySet.getFirst();
            String time = TimeParser.toString(entry.getElapsed());

            HashMap<String, String> map = new HashMap<>();
            map.put("punisher", entry.getPunisher());
            map.put("reason", (entry.getReason().isEmpty() ? "Spamming" : entry.getReason()));
            map.put("time", time);
            Messages.get().sendMessage(event.getPlayer(), "mute.activate", map);

            map = new HashMap<>();
            map.put("time", time);
            Messages.get().sendMessage(event.getPlayer(), "mute.timeLeft", map);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        PunishedPlayer player = PunishedPlayer.of(event.getUniqueId());
        if (player.isBanned()) {
            LinkedList<PunishmentEntry> entrySet = new LinkedList<>(player.getActiveMutes());
            PunishmentEntry entry = entrySet.getFirst();
            event.setKickMessage(ChatColor.RED + "You were banned for "
                    + ChatColor.DARK_RED + (entry.getReason().isEmpty() ? "Spamming" : entry.getReason()) + ChatColor.RED
                    + " by " + ChatColor.DARK_RED + entry.getPunisher());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
        }
    }
}