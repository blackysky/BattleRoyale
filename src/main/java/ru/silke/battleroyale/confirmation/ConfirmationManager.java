package ru.silke.battleroyale.confirmation;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Класс для управления запросами на подтверждение действий игроков
 */
public class ConfirmationManager {

    /**
     * Коллекция для хранения текущих запросов на подтверждение.
     * Ключ - UUID игрока, значение - ConfirmationData, содержащий информацию о запросе.
     */
    private final HashMap<UUID, ConfirmationData> confirmations = new HashMap<>();

    /**
     * Отправляет запрос на подтверждение действия игроку
     *
     * @param player              Игрок, которому отправляется запрос на подтверждение
     * @param confirmationId      Уникальный идентификатор запроса на подтверждение
     * @param confirmationMessage Сообщение, которое будет отображаться в запросе на подтверждение
     */
    public void sendConfirmation(Player player, String confirmationId, String confirmationMessage) {
        // Проверяем, не ждёт ли игрок уже какого-то подтверждения
        if (confirmations.containsKey(player.getUniqueId())) {
            player.sendMessage("Подтверждение уже отправлено!");
            return;
        }

        // Создаем сообщение для подтверждения
        TextComponent confirmationText = new TextComponent(confirmationMessage);

        // Создаем кнопки для подтверждения и отмены
        TextComponent confirmButton = new TextComponent("[Подтвердить]");
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Нажмите, чтобы подтвердить")});
        confirmButton.setColor(ChatColor.GREEN);
        confirmButton.setBold(true);
        confirmButton.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/confirm " + confirmationId + " confirm"));
        confirmButton.setHoverEvent(hoverEvent);

        TextComponent cancelButton = new TextComponent("[Отмена]");
        HoverEvent hoverEvent1 = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{new TextComponent("Нажмите, чтобы отменить")});
        cancelButton.setColor(ChatColor.RED);
        cancelButton.setBold(true);
        cancelButton.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/confirm " + confirmationId + " cancel"));
        cancelButton.setHoverEvent(hoverEvent1);

        // Добавляем кнопки к сообщению
        confirmationText.addExtra(" ");
        confirmationText.addExtra(confirmButton);
        confirmationText.addExtra(" ");
        confirmationText.addExtra(cancelButton);

        // Отправляем сообщение с кнопками игроку
        player.spigot().sendMessage(confirmationText);

        // Добавляем данные о запросе на подтверждение в список
        confirmations.put(player.getUniqueId(), new ConfirmationData(
                confirmationId,
                confirmationMessage,
                (Player confirmedPlayer) -> {
                    // Обработчик подтверждения - что нужно сделать, если игрок подтвердил
                    confirmedPlayer.sendMessage("Подтверждено!");
                    // Здесь можно добавить код, который нужно выполнить при подтверждении
                },
                player.getUniqueId(),
                System.currentTimeMillis() + 10000)
        );
    }

    /**
     * Обрабатывает ответ на запрос на подтверждение
     *
     * @param player             Игрок, который ответил на запрос
     * @param confirmationId     Идентификатор подтверждения
     * @param confirmationAnswer Ответ на подтверждение (confirm/cancel)
     */
    public void processConfirmation(Player player, String confirmationId, String confirmationAnswer) {
        // Проверяем, есть ли запрос на подтверждение для данного игрока
        UUID playerId = player.getUniqueId();
        if (!confirmations.containsKey(playerId)) {
            player.sendMessage("Запрос на подтверждение не найден!");
            return;
        }

        ConfirmationData confirmationData = confirmations.get(playerId);

        // Проверяем, что идентификатор подтверждения совпадает с тем, что был отправлен
        if (!confirmationData.getConfirmationId().equals(confirmationId)) {
            player.sendMessage("Идентификатор подтверждения не совпадает!");
            return;
        }

        // Проверяем, что время на подтверждение не истекло
        if (confirmationData.getExpirationTime() < System.currentTimeMillis()) {
            player.sendMessage("Время на подтверждение истекло!");
            confirmations.remove(player.getUniqueId());
            return;
        }

        // Вызываем обработчик подтверждения
        confirmationData.getConfirmationHandler().onConfirm(player);

        // Удаляем данные о подтверждении из списка
        confirmations.remove(player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Операция успешно выполнена!");
    }

    /**
     * Удаляет данные о подтверждении для данного игрока.
     *
     * @param playerUUID UUID игрока, для которого нужно удалить данные о подтверждении
     */
    public void removeConfirmationData(UUID playerUUID) {
        confirmations.remove(playerUUID);
    }

    /**
     * Возвращает HashMap со всеми данными о подтверждении.
     *
     * @return HashMap, содержащий все данные о подтверждении
     */
    public HashMap<UUID, ConfirmationData> getConfirmations() {
        return confirmations;
    }
}