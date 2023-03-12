package ru.silke.battleroyale.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.silke.battleroyale.confirmation.ConfirmationData;
import ru.silke.battleroyale.confirmation.ConfirmationManager;

import java.util.UUID;

public class ConfirmationCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            player.sendMessage("Используйте: /confirm <confirmationId> <action>");
            return true;
        }

        String confirmationId = args[0];
        String action = args[1];

        if (!action.equalsIgnoreCase("confirm") && !action.equalsIgnoreCase("cancel")) {
            player.sendMessage("Неправильное действие. Пожалуйста, используйте 'confirm' или 'cancel'.");
            return true;
        }

        // Получаем ConfirmationData из HashMap
        ConfirmationManager confirmationManager = new ConfirmationManager();
        UUID playerUUID = player.getUniqueId();
        ConfirmationData confirmationData = confirmationManager.getConfirmations().get(player.getUniqueId());

        // Проверяем, что ConfirmationData существует и что его ID соответствует переданному ID подтверждения
        if (confirmationData == null || !confirmationData.getConfirmationId().equals(confirmationId)) {
            player.sendMessage("Подтверждение не найдено.");
            return true;
        }

        // Проверяем, что игрок, пытающийся подтвердить или отменить, является тем же самым игроком, который запросил подтверждение
        if (!confirmationData.getPlayerUUID().equals(playerUUID)) {
            player.sendMessage("Вы не можете подтвердить или отменить это подтверждение, так как оно было запрошено другим игроком.");
            return true;
        }

        if (action.equalsIgnoreCase("confirm")) {
            // Вызываем обработчик подтверждения
            confirmationData.getConfirmationHandler().onConfirm(player);

            // Удаляем данные о подтверждении из HashMap
            confirmationManager.removeConfirmationData(playerUUID);

            player.sendMessage("Вы успешно подтвердили " + confirmationData.getConfirmationMessage());
        } else {
            // Удаляем данные о подтверждении из HashMap
            confirmationManager.removeConfirmationData(playerUUID);

            player.sendMessage("Вы отменили " + confirmationData.getConfirmationMessage());
        }

        return true;
    }

}

