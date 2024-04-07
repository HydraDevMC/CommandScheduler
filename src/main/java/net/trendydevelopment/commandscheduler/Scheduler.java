package net.trendydevelopment.commandscheduler;

import net.trendydevelopment.commandscheduler.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Scheduler extends JavaPlugin {

    ConfigManager configManager;
    @Override
    public void onEnable() {
        createConfigs();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    // Loads or reloads config
    public void createConfigs() {
        configManager = new ConfigManager(this);
    }
}
