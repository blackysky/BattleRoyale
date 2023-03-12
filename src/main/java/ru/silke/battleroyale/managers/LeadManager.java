package ru.silke.battleroyale.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.silke.battleroyale.main;

import java.util.Arrays;

public class LeadManager {

    FileConfiguration config = main.plugin.getConfig();
    private String[] leadPlayers = config.getStringList("leadPlayers").toArray(new String[0]);

    /**
     * Добавляет игрока в список ведущих
     *
     * @param target Игрок
     */
    public void addLeadPlayer(Player executor, String target) {

        // Если игрок уже есть в списке ведущих, то выходим предупреждение
        if (Arrays.asList(leadPlayers).contains(target)) {
            executor.sendMessage("Игрок " + ChatColor.GREEN + target + ChatColor.WHITE + " уже есть в списке ведущих!");
            executor.playSound(executor.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
            return;
        }

        // Добавляем игрока в список ведущих
        String[] newLeadPlayers = new String[leadPlayers.length + 1];
        System.arraycopy(leadPlayers, 0, newLeadPlayers, 0, leadPlayers.length);
        newLeadPlayers[newLeadPlayers.length - 1] = target;
        leadPlayers = newLeadPlayers;

        config.set("leadPlayers", Arrays.toString(leadPlayers));
        main.plugin.saveConfig();

        executor.sendMessage("Игрок " + ChatColor.GREEN + target + ChatColor.WHITE + " добавлен в список ведущих!");
        executor.playSound(executor.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
    }

    /**
     * Удаляет игрока из списка ведущих
     *
     * @param target Игрок
     */
    public void removeLeadPlayer(Player executor, String target) {

        // Если игрока нет в списке ведущих, то выходим предупреждение
        if (!Arrays.asList(leadPlayers).contains(target)) {
            executor.sendMessage("Игрок " + ChatColor.GREEN + target + ChatColor.WHITE + " не найден в списке ведущих!");
            executor.playSound(executor.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
            return;
        }

        // Удаляем игрока из списка ведущих
        String[] newLeadPlayers = new String[leadPlayers.length - 1];
        int index = 0;
        for (String leadPlayer : leadPlayers) {
            if (!leadPlayer.equals(target)) {
                newLeadPlayers[index] = leadPlayer;
                index++;
            }
        }
        leadPlayers = newLeadPlayers;

        config.set("leadPlayers", Arrays.toString(leadPlayers));
        main.plugin.saveConfig();

        executor.sendMessage("Игрок " + ChatColor.GREEN + target + ChatColor.WHITE + " удален из списка ведущих!");
        executor.playSound(executor.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
    }

    public String[] getLeadPlayers() {
        return leadPlayers;
    }

    public String printLeadPlayers() {
        // print lead players from array
        String[] newLeadPlayers = new String[leadPlayers.length];
        int index = 0;
        for (String leadPlayer : leadPlayers) {
            newLeadPlayers[index] = "- " + ChatColor.GREEN + leadPlayer;
            index++;
        }

        // add an empty string to the beginning of the array
        String[] newArray = new String[newLeadPlayers.length + 1];
        newArray[0] = "";
        System.arraycopy(newLeadPlayers, 0, newArray, 1, newLeadPlayers.length);

        return String.join("\n", newArray);
    }


}
