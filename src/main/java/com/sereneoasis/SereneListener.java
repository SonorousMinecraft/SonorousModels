package com.sereneoasis;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SereneListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent moveEvent){
        SereneModels.plugin.getModelManager().getModelInstanceSet().forEach(modelInstance -> modelInstance.updateLocation(moveEvent.getTo()));
    }
}
