package ru.urfu.controller.constant;

/**
 * Список всех команд, воспринимаемых ботом
 */
public enum UserCommand {

    /**
     * Команда получения главного меню
     */
    START("/start"),

    /**
     * Команда получения списка всех доступных валют
     */
    CURRENCIES("/currencies"),

    /**
     * Команда получения всех отслеживаемых валют
     */
    TRACK("/track"),

    /**
     * Команда получения списка всех подключённых API
     */
    API("/api");

    /**
     * Текст команды
     */
    private final String command;

    UserCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
