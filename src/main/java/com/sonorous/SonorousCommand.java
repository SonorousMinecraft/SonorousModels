package com.sonorous;

import com.sonorous.models.ObjParser;
import com.sonorous.skeleton.*;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.IOException;

public class SonorousCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player) {
            Location loc = player.getLocation();
            loc.setDirection(new Vector(0, 0, 1));

            // Load model from JSON
            MeshDefinition meshDefinition;
            try {
                meshDefinition = ObjParser.loadMeshDefinition("model.obj"); // Adjust the path for your .obj
            } catch (IOException e) {
                player.sendMessage("Error loading model: " + e.getMessage());
                return false;
            }

            // Assuming meshDefinition contains one or more PartDefinitions
            for (PartDefinition part : meshDefinition.getPartDefinitions()) {
                // Get the pose for this part
                PartPose pose = part.getPose();

                // Calculate the pose offset if applicable
                Location partLocation = loc.clone();
                if (pose != null) {
                    partLocation.add(pose.getX(), pose.getY(), pose.getZ());
                }

                // Iterate through cubes of this part
                for (Cube cube : part.getCubes()) {
                    // Calculate cube position in world space
                    Location cubeLoc = partLocation.clone();

                    // Use cube's properties to locate it correctly
                    float x = cube.getX();
                    float y = cube.getY();
                    float z = cube.getZ();
                    cubeLoc.add(x, y, z);

                    float width = cube.getWidth();
                    float height = cube.getHeight();
                    float depth = cube.getDepth();

                    // Create new TempDisplayBlock
                    new TempDisplayBlock(cubeLoc, Material.SLIME_BLOCK, 1, width, height, depth, true, Color.PURPLE);
                }
            }
        }
        return true;
    }
}