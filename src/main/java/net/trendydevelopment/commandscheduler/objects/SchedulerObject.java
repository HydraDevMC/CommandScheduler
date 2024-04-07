package net.trendydevelopment.commandscheduler.objects;

import net.trendydevelopment.commandscheduler.Scheduler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class SchedulerObject {
    String name;

    long time;

    List<String> commands;

    public SchedulerObject(String name, long time, List<String> commands) {
        this.name = name;
        this.time = time;
        this.commands = commands;
    }

    public long getTime() {
        return time;
    }

    public void runCommands() {
        commands.forEach(s -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s));
    }
}
