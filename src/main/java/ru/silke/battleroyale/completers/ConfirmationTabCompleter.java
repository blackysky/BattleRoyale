package ru.silke.battleroyale.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class ConfirmationTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        String[] confirmationId = {"remove_lead_player", "remove_yourself_from_lead_players"};
        String[] actions = {"confirm", "cancel"};

        // Если команда /confirm, то предлагаем ввести <confirmationId>
        if (command.getName().equalsIgnoreCase("confirm") && args.length == 1) {
            return Arrays.asList(confirmationId);
        }

        // Если команда /confirm <confirmationId>, то предлагаем ввести что-то из списка действий
        if (command.getName().equalsIgnoreCase("confirm") && args.length == 2) {
            return Arrays.asList(actions);
        }

        return null;
    }
}
