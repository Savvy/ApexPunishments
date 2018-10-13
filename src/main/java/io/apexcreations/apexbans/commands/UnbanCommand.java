package io.apexcreations.apexbans.commands;

import io.apexcreations.ApexCommand;
import io.apexcreations.apexbans.players.PunishedPlayer;
import io.apexcreations.apexbans.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class UnbanCommand extends ApexCommand {

    public UnbanCommand(String name) {
        super("unban", "Unban a player allowing them to join", "apexbans.command.unban", false);
        setUsage("/<command> <player>");
    }

    @Override
    public boolean executeCommand(CommandSender commandSender, String s, String[] args) {
        if (args.length <= 0) {
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        PunishedPlayer punished = PunishedPlayer.of(target);
        punished.unban();

        HashMap<String, String> map = new HashMap<>();
        map.put("punisher", commandSender.getName());
        map.put("player", target.getName());
        Messages.get().sendMessage(commandSender, "bans.unbanned", map);
        return true;
    }
}