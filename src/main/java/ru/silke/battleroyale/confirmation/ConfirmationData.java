package ru.silke.battleroyale.confirmation;

import java.util.UUID;

/**
 * Класс, хранящий данные о запросе на подтверждение от игрока.
 */
public class ConfirmationData {
    private final String confirmationId;
    private final UUID playerUUID;
    private final long expirationTime;
    private final String confirmationMessage;
    private final ConfirmationHandler confirmationHandler;

    /**
     * Создает объект класса ConfirmationData.
     *
     * @param confirmationId      уникальный идентификатор подтверждения
     * @param confirmationMessage сообщение, которое нужно подтвердить
     * @param confirmationHandler обработчик подтверждения
     * @param playerId            идентификатор игрока, которому отправляется запрос на подтверждение
     * @param expirationTime      время истечения запроса на подтверждение в миллисекундах
     */
    public ConfirmationData(String confirmationId, String confirmationMessage, ConfirmationHandler confirmationHandler, UUID playerId, long expirationTime) {
        // Класс ConfirmationData хранит данные о запросе на подтверждение от игрока.
        // Данные включают в себя: уникальный идентификатор подтверждения, сообщение, которое нужно подтвердить,
        // обработчик подтверждения, идентификатор игрока и время истечения запроса на подтверждение.
        this.confirmationId = confirmationId;
        this.confirmationMessage = confirmationMessage;
        this.confirmationHandler = confirmationHandler;
        this.playerUUID = playerId;
        this.expirationTime = expirationTime;
    }

    /**
     * Возвращает уникальный идентификатор подтверждения.
     *
     * @return уникальный идентификатор подтверждения
     */
    public String getConfirmationId() {
        return confirmationId;
    }

    /**
     * Возвращает идентификатор игрока, которому отправляется запрос на подтверждение.
     *
     * @return идентификатор игрока
     */
    public UUID getPlayerUUID() {
        return playerUUID;
    }

    /**
     * Возвращает сообщение, которое нужно подтвердить.
     *
     * @return сообщение для подтверждения
     */
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    /**
     * Возвращает обработчик подтверждения.
     *
     * @return обработчик подтверждения
     */
    public ConfirmationHandler getConfirmationHandler() {
        return confirmationHandler;
    }

    /**
     * Возвращает время истечения запроса на подтверждение в миллисекундах.
     *
     * @return время истечения запроса на подтверждение
     */
    public long getExpirationTime() {
        return expirationTime;
    }
}
