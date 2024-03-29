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

public class UnmuteCommand extends ApexCommand {

    public UnmuteCommand(String name) {
        super("unmute", "Unmute a player allowing them to speak", "", false);
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
        map.put("player", target.getName());
        Messages.get().sendMessage(commandSender, "mutes.unmuted", map);
        return true;
    }
}