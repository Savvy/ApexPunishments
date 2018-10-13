package io.apexcreations.apexbans.commands;

import io.apexcreations.ApexCommand;
import io.apexcreations.apexbans.players.PunishedPlayer;
import io.apexcreations.apexbans.utils.Messages;
import io.apexcreations.apexbans.utils.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BanCommand extends ApexCommand {

    public BanCommand() {
        super("ban", "The ban command is used to prevent players from joining.", "apexbans.command.ban", false);
        setUsage("/<command> <player> [time] [reason]");
    }

    @Override
    public boolean executeCommand(CommandSender commandSender, String label, String[] args) {
        if (args.length <= 0) {
            return false;
        }

        long duration = -1;
        if (args.length >= 2) duration = TimeParser.parseTime(args[1]);

        String time = TimeParser.toString(duration);

        List<String> stringList = new ArrayList<>(Arrays.asList(args));
        String reason = stringList.subList(2, stringList.size()).toString().replace(", ", " ").replace("[", "").replace("]", "");

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        HashMap<String, String> map = new HashMap<>();
        map.put("punisher", commandSender.getName());
        map.put("nl", "\n");
        map.put("player", target.getName());
        map.put("reason", reason);
        map.put("time", time);

        if (target.isOnline()) {
            target.getPlayer().kickPlayer(Messages.get().getMessage("bans.activate", map));
        }

        PunishedPlayer punished = PunishedPlayer.of(target);
        punished.ban(commandSender.getName(), reason, duration);
        Messages.get().sendMessage(commandSender, "bans.punished", map);
        return true;
    }
}