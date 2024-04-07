package net.trendydevelopment.commandscheduler.config;

import net.trendydevelopment.commandscheduler.Scheduler;
import net.trendydevelopment.commandscheduler.objects.SchedulerObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    Scheduler scheduler;

    FileConfiguration config;
    public ConfigManager(Scheduler scheduler) {
        this.scheduler = scheduler;
        config = loadConfigs();
        initScheduler(createSchedules());
    }

    private void initScheduler(List<SchedulerObject> schedulerObjectList) {
        schedulerObjectList.forEach(schedulerObject -> {
            BukkitScheduler bukkitScheduler = Bukkit.getServer().getScheduler();
            long time = schedulerObject.getTime()*20;
            bukkitScheduler.runTaskTimer(scheduler, schedulerObject::runCommands, time, time);
        });
    }

    private List<SchedulerObject> createSchedules() {
        List<SchedulerObject> schedulerObjectList = new ArrayList<>();
        ConfigurationSection cSection = config.getConfigurationSection("schedules");
        for (String schedulerKeySet : cSection.getKeys(false)) {
            schedulerObjectList.add(new SchedulerObject(schedulerKeySet, createTimer(cSection.getString(schedulerKeySet+".time")),cSection.getStringList(schedulerKeySet+".commands")));

        }
        return schedulerObjectList;
    }

    private long createTimer(String timeString) {
        long seconds = 0;
        String[] parts = timeString.split(" ");
        for (String part : parts) {
            String unit = part.substring(part.length() - 1);
            long value = Long.parseLong(part.substring(0, part.length() - 1));
            switch (unit) {
                case "h":
                    seconds += value * 60 * 60;
                    break;
                case "m":
                    seconds += value * 60;
                    break;
                case "s":
                    seconds += value;
                    break;
                default:
                    System.out.println("Invalid time format in scheduler.yml ["+timeString+"]");
                    return -1;
            }
        }
        return seconds;
    }
    private FileConfiguration loadConfigs() {
        File configFile = new File(scheduler.getDataFolder(), "scheduler.yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            scheduler.saveResource("scheduler.yml", false);
        }
        FileConfiguration configuration = new YamlConfiguration();
        try {
            configuration.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return configuration;
    }
}
