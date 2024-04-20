package ru.urfu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Считывает параметры бота из application.properties
 */
@Configuration
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;

    /**
     * Имя бота
     */
    public String getName() {
        return name;
    }

    /**
     * Токен бота
     */
    public String getToken() {
        return token;
    }
}