package com.senrua.StreamerCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.Arrays;
import java.util.List;
public class StreamerCommands extends JavaPlugin {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void saveDefaultConfig() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", false);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("streamer")) {
            if (!sender.hasPermission("cenoic.streamer")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage("Usage: /streamer <arg1> [arg2]");
                return true;
            }

            String arg1 = args[0].toLowerCase();
            String arg2 = args.length >= 2 ? args[1] : "";

            List<String> commands = config.getStringList("Commands." + arg1);
            if (commands == null || commands.isEmpty()) {
                sender.sendMessage("Invalid argument for <arg1>.");
                return true;
            }

            List<String> noArg2Commands = Arrays.asList("live", "offline");

            for (String command : commands) {
                if (command.contains("<arg2>") && arg2.isEmpty() && !noArg2Commands.contains(arg1)) {
                    sender.sendMessage("Missing required argument <arg2>.");
                    return true;
                }

                command = command.replace("<arg3>", sender.getName());
                command = command.replace("<arg2>", arg2);

                getServer().dispatchCommand(getServer().getConsoleSender(), command);
                sender.sendMessage("Executed command: " + command);
            }

            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
    }
}
