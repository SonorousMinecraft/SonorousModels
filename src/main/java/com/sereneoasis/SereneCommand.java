package com.sereneoasis;

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
            MeshDefinition meshDefinition = zombieModel.getMeshDefinition();
            PartDefinition rootPart = meshDefinition.getRoot();

            for (PartDefinition child : rootPart.getChildren()) { // Optionally iterate through children if applicable
                // Iterate through cubes of this part
                for (Cube cube : child.getCubes()) {
                    // Calculate cube position in world space
                    Location cubeLoc = loc.clone();
                    // Use cube's properties to decide its placement
                    float x = cube.getX(); // Assuming you have a getter in Cube to access these properties
                    float y = cube.getY();
                    float z = cube.getZ();
                    cubeLoc.add(x, y, z);
                    float width = cube.getWidth();
                    float height = cube.getHeight();
                    float depth = cube.getDepth();

                    new TempDisplayBlock(cubeLoc, Material.SLIME_BLOCK, 1, width, height, depth, true, Color.PURPLE);
                    // Spawn block display for the cube
                    // spawn block display at (x, y, z) with size (width, height, depth)
                    // You might want to use an appropriate block type with its position and size
                    // Example:
                    // spawnBlockDisplay(x, y, z, width, height, depth);
                    // This will depend on your implementation of the block display logic.
                    // For example:
                    // spawnBlockDisplay(x, y, z);
                }
            }
        }
        return true;
    }
}