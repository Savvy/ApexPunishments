package io.apexcreations.apexbans.commands;

import io.apexcreations.apexbans.players.PunishedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (args.length <= 0) {
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(Bukkit.getPlayerUniqueId(args[0]));

        PunishedPlayer punished = PunishedPlayer.of(target);
        punished.unban();
        return true;
    }
}