package com.sereneoasis;

import com.sereneoasis.manager.ModelManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SereneModels extends JavaPlugin {


    public static SereneModels plugin;

    private ModelManager modelManager;

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    @Override
    public void onEnable() {


        getLogger().log(Level.INFO, "SereneModels was enabled successfully.");
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new SereneListener(), this);
        this.getCommand("SereneModels").setExecutor(new SereneCommand());

        this.modelManager = new ModelManager();
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "SereneModels was disabled successfully.");
    }


}
