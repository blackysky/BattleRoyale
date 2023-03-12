package ru.silke.battleroyale.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.silke.battleroyale.managers.LeadManager;

import java.util.Arrays;

public class LeadCommand implements CommandExecutor {

    LeadManager leadManager = new LeadManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (Arrays.asList(leadManager.getLeadPlayers()).contains(player.getName())) {
                if (args.length == 0) {
                    player.sendMessage("Использование: " + ChatColor.GREEN + "/lead <add/remove/list>");
                    player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
                    return true;
                }

                switch (args[0]) {
                    case "add":
                        if (args.length == 1) {
                            player.sendMessage("Использование: " + ChatColor.GREEN + "/lead add <игрок>");
                            player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
                            return true;
                        } else {
                            String target = args[1];
                            leadManager.addLeadPlayer(player, target);
                        }
                        break;
                    case "remove":
                        if (args.length == 1) {
                            player.sendMessage("Использование: " + ChatColor.GREEN + "/lead remove <игрок>");
                            player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
                            return true;
                        } else {
                            String target2 = args[1];
                            leadManager.removeLeadPlayer(player, target2);
                        }
                        break;
                    case "list":
                        player.sendMessage("Список ведущих: " + ChatColor.GREEN + leadManager.printLeadPlayers());
                        player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
                        break;
                }
            } else {
                player.sendMessage("У вас нет прав на использование этой команды!");
                player.playSound(player.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
            }
        } else {
            sender.sendMessage("Только игрок может использовать эту команду!");
        }

        return true;
    }
}
