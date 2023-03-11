package ru.silke.battleroyale;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    public static main plugin;

    @Override
    public void onEnable() {
        plugin = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
