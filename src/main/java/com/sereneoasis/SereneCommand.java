package com.sereneoasis;

import com.sereneoasis.manager.ModelInstance;
import com.sereneoasis.models.ZombieModel;
import com.sereneoasis.skeleton.Cube;
import com.sereneoasis.skeleton.LayerDefinition;
import com.sereneoasis.skeleton.MeshDefinition;
import com.sereneoasis.skeleton.PartDefinition;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.util.Vector;

public class SereneCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Location loc = player.getLocation();
            loc.setDirection(new Vector(0,0,1));
            LayerDefinition zombieModel = ZombieModel.createBodyLayer();
            new ModelInstance(zombieModel, loc);
        }
        return true;
    }
}