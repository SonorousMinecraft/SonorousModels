package com.sereneoasis;

import com.sereneoasis.manager.ModelInstance;
import com.sereneoasis.models.Dragon;
import com.sereneoasis.skeleton.LayerDefinition;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SereneCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Location loc = player.getLocation();
            loc.setDirection(new Vector(0, 0, -1));
            LayerDefinition zombieModel = Dragon.createBodyLayer();
            new ModelInstance(zombieModel, loc);
        }
        return true;
    }
}