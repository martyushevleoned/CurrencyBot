package ru.urfu.controller.constant;

/**
 * Список всех команд воспринимаемых ботом
 */
public enum UserCommands {

    /**
     * Команда вызова главного меню
     */
    START("/start"),

    /**
     * Команда вызова списка всех доступных валют
     */
    CURRENCIES("/currencies"),

    /**
     * Команда вызова всех отслеживаемых валют
     */
    TRACK("/track"),

    /**
     * Команда вызова списка всех подключённых API
     */
    API("/api");

    /**
     * Текст команды
     */
    private final String command;

    UserCommands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
