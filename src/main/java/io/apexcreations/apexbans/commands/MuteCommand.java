package io.apexcreations.apexbans.commands;

import io.apexcreations.apexbans.players.PunishedPlayer;
import io.apexcreations.apexbans.utils.Messages;
import io.apexcreations.apexbans.utils.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(Bukkit.getPlayerUniqueId(args[0]));
        if (target.isOnline()) {
            Messages.get().sendMessage(commandSender, "mute.silenced", Collections.emptyMap());
        }

        long duration = -1;
        if (args.length >= 2) duration = TimeParser.parseTime(args[1]);

        List<String> stringList = new ArrayList<>(Arrays.asList(args));
        String reason = stringList.subList(2, stringList.size()).toString().replace(", ", " ").replace("[", "").replace("]", "");

        PunishedPlayer punished = PunishedPlayer.of(target);
        punished.mute(commandSender.getName(), reason, duration);
        HashMap<String, String> map = new HashMap<>();
        map.put("target", target.getName());
        map.put("reason", reason);
        Messages.get().sendMessage(commandSender, "mute.punished", map);
        return true;
    }
}