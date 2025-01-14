package com.sereneoasis.manager;

import com.sereneoasis.SereneModels;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public class ModelManager {

    private final Set<ModelInstance> modelInstanceSet = new HashSet<>();

    public ModelManager() {
        Bukkit.getScheduler().runTaskTimer(SereneModels.plugin, () -> {
            modelInstanceSet.forEach(ModelInstance::updateCubes);
            modelInstanceSet.forEach(ModelInstance::tickAnimationManager);

        }, 5L, 5L);
    }

    public Set<ModelInstance> getModelInstanceSet() {
        return modelInstanceSet;
    }

    public void addModel(ModelInstance modelInstance) {
        this.modelInstanceSet.add(modelInstance);
    }
}
