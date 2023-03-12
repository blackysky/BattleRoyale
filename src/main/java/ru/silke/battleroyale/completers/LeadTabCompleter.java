package ru.silke.battleroyale.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class LeadTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        String[] leadCommands = {"add", "remove", "list"};

        // Аргументы для команды /lead
        // /lead add
        // /lead remove
        // /lead list
        if (command.getName().equalsIgnoreCase("lead")) {
            if (args.length == 1) {
                return Arrays.asList(leadCommands);
            }
        }

        // Аргументы для команды /lead add и /lead remove
        // /lead add <игрок>
        // /lead remove <игрок>
        // Проще говоря, выводим список игроков
        if (command.getName().equalsIgnoreCase("lead") && args.length == 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                return null;
            }
        }

        return null;
    }
}
