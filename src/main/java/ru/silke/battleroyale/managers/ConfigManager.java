package ru.silke.battleroyale.managers;

import java.io.File;

import static ru.silke.battleroyale.main.plugin;

public class ConfigManager {

    /**
     * Создание папки для конфигов
     */
    public void createDataFolder() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
    }

    /**
     * Создание конфига
     */
    public void createConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveDefaultConfig();
        }
    }

}
