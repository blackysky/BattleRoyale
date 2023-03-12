package ru.silke.battleroyale;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.silke.battleroyale.commands.HpCommand;
import ru.silke.battleroyale.commands.LeadCommand;
import ru.silke.battleroyale.managers.ConfigManager;

@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
public final class main extends JavaPlugin {

    public static main plugin;
    private final String toast = "Silke war hier";

    ConfigManager configManager = new ConfigManager();

    @Override
    public void onEnable() {
        plugin = this;

        // Отсебятина
        getLogger().info(toast);

        // Команды
        getCommand("lead").setExecutor(new LeadCommand());
        getCommand("hp").setExecutor(new HpCommand());


        // Конфиг
        configManager.createDataFolder();
//        configManager.createConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
