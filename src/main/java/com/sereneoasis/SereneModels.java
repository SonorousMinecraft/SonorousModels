package com.sereneoasis;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SereneModels extends JavaPlugin {


    public static SereneModels plugin;


    @Override
    public void onEnable() {


        getLogger().log(Level.INFO, "com.sereneoasis.SereneModels was enabled successfully.");
        plugin = this;
//        this.getServer().getPluginManager().registerEvents(new SereneListener(), this);
        this.getCommand("SereneModels").setExecutor(new SereneCommand());

    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "com.sereneoasis.SereneModels was disabled successfully.");
    }


}
