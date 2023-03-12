package ru.silke.battleroyale.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.silke.battleroyale.confirmation.ConfirmationManager;
import ru.silke.battleroyale.main;

import java.util.Arrays;

public class LeadManager {

    FileConfiguration config = main.plugin.getConfig();
    ConfirmationManager confirmationManager = new ConfirmationManager();
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

        if (target.equals(executor.getName())) {
            confirmationManager.sendConfirmation(executor, target, "remove_yourself_from_lead_players", "Вы уверены, что хотите удалить себя из списка ведущих?");
        } else {
            confirmationManager.sendConfirmation(executor, target, "remove_lead_player", "Вы уверены, что хотите удалить игрока " + ChatColor.GREEN + target + ChatColor.WHITE + " из списка ведущих?");
        }
    }

    /**
     * Возвращает список ведущих
     *
     * @return Список ведущих
     */
    public String[] getLeadPlayers() {
        return leadPlayers;
    }

    /**
     * Возвращает список ведущих в виде строки
     *
     * @return Список ведущих
     */
    public String printLeadPlayers() {
        String[] newLeadPlayers = new String[leadPlayers.length];
        int index = 0;
        for (String leadPlayer : leadPlayers) {
            newLeadPlayers[index] = "- " + ChatColor.GREEN + leadPlayer;
            index++;
        }

        String[] newArray = new String[newLeadPlayers.length + 1];
        newArray[0] = "";
        System.arraycopy(newLeadPlayers, 0, newArray, 1, newLeadPlayers.length);

        return String.join("\n", newArray);
    }


}
