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

    /**
     * Имя бота
     */
    @Value("${bot.name}")
    private String name;

    /**
     * Токен бота
     */
    @Value("${bot.token}")
    private String token;

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}