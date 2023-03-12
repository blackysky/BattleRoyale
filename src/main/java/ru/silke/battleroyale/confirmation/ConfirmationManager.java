package ru.silke.battleroyale.confirmation;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.silke.battleroyale.managers.LeadManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static ru.silke.battleroyale.main.plugin;

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
    public void sendConfirmation(Player player, String target, String confirmationId, String confirmationMessage) {
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
                System.currentTimeMillis() + 10000, target)
        );
    }

    public void processConfirmation(UUID playerUUID, String confirmationId, boolean confirmed) {
        FileConfiguration config = plugin.getConfig();
        ConfirmationManager confirmationManager = new ConfirmationManager();
        LeadManager leadManager = new LeadManager();
        String[] leadPlayers = leadManager.getLeadPlayers();

        if (!confirmed) {
            // Если игрок ответил "нет", то сообщаем об отмене действия
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                player.sendMessage("Действие было отменено.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
            }
            return;
        }

        if (confirmationId.equals("remove_yourself_from_lead_players")) {
            ConfirmationData confirmationData = confirmationManager.getConfirmation(playerUUID);
            String target = confirmationData.getTarget();
            // Удаление игрока из списка ведущих
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
            plugin.saveConfig();

            Player executor = Bukkit.getPlayer(playerUUID);
            executor.sendMessage("Вы удалены из списка ведущих!");
            executor.playSound(executor.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
        } else if (confirmationId.equals("remove_lead_player")) {
            // Удаление игрока из списка ведущих
            ConfirmationData confirmationData = confirmationManager.getConfirmation(playerUUID);
            String target = confirmationData.getTarget();
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
            plugin.saveConfig();

            Player executor = Bukkit.getPlayer(playerUUID);
            executor.sendMessage("Игрок " + ChatColor.GREEN + target + ChatColor.WHITE + " удален из списка ведущих!");
            executor.playSound(executor.getLocation(), "minecraft:entity.experience_orb.pickup", 1, 1);
        }
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
     * Возвращает данные о подтверждении для данного игрока.
     *
     * @param playerUUID UUID игрока, для которого нужно получить данные о подтверждении
     * @return Данные о подтверждении
     */
    public ConfirmationData getConfirmation(UUID playerUUID) {
        return confirmations.get(playerUUID);
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