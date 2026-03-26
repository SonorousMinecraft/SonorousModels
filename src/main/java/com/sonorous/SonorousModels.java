package com.sonorous;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SonorousModels extends JavaPlugin {


    public static SonorousModels plugin;


    @Override
    public void onEnable() {


        getLogger().log(Level.INFO, "com.sonorous.SonorousModels was enabled successfully.");
        plugin = this;
//        this.getServer().getPluginManager().registerEvents(new SonorousListener(), this);
        this.getCommand("SonorousModels").setExecutor(new SonorousCommand());

    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "com.sonorous.SonorousModels was disabled successfully.");
    }


}
