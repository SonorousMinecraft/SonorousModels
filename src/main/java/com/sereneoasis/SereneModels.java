package com.sereneoasis;

import com.sereneoasis.listeners.SereneListener;
import com.sereneoasis.manager.ModelManager;
import com.sereneoasis.physics.PhysicsEngine;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SereneModels extends JavaPlugin {


    public static SereneModels plugin;

    private ModelManager modelManager;

    public ModelManager getModelManager() {
        return this.modelManager;
    }

    private PhysicsEngine physicsEngine;

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    @Override
    public void onEnable() {


        getLogger().log(Level.INFO, "SereneModels was enabled successfully.");
        plugin = this;
        this.getServer().getPluginManager().registerEvents(new SereneListener(), this);
        this.getCommand("SereneModels").setExecutor(new SereneCommand());

        this.modelManager = new ModelManager();
        this.physicsEngine = new PhysicsEngine();


    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "SereneModels was disabled successfully.");
    }


}
