package com.sereneoasis.manager;

import com.sereneoasis.SereneModels;
import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.skeleton.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import oshi.util.tuples.Pair;

import java.util.HashSet;
import java.util.Set;

public class ModelInstance {

    private final Set<Pair<TempDisplayBlock, Vector>> cubes = new HashSet<>();
    private Location currentLoc;

    public ModelInstance(LayerDefinition layerDefinition, Location loc) {

        MeshDefinition meshDefinition = layerDefinition.getMeshDefinition();
        PartDefinition rootPart = meshDefinition.getRoot();
        this.currentLoc = loc.clone();

        processParts(rootPart, currentLoc);

        SereneModels.plugin.getModelManager().addModel(this);
    }

    public void processParts(PartDefinition partDefinition, Location loc) {
        // Clone the current location to make it current for this part

        PartPose pose = partDefinition.getPose();
        // Process cubes of the current part

        for (Cube cube : partDefinition.getCubes()) {
            // Calculate cube position in world space
            float x = cube.getX();
            float y = cube.getY();
            float z = cube.getZ();

            Vector offset = new Vector(x, y, z);
            if (pose != null) {
                Bukkit.broadcastMessage(partDefinition.getName());
                offset.subtract(pose.getOffset());
            }

            Location currentLocation = loc.clone();

            currentLocation.add(offset);

            // Creating the temporary display block
            float width = cube.getWidth();
            float height = cube.getHeight();
            float depth = cube.getDepth();

            TempDisplayBlock tdb = new TempDisplayBlock(currentLocation, Material.SLIME_BLOCK, 1, width, height, depth, true, Color.PURPLE);
            cubes.add(new Pair<>(tdb, offset));
        }

        // Process children parts
        for (PartDefinition child : partDefinition.getChildren()) {
            // Recursively process each child part
            processParts(child, loc);
        }
    }

    public void updateLocation(Location newLoc) {
        this.currentLoc = newLoc.clone();
    }

    public void updateCubes() {
        cubes.forEach(tempDisplayBlockVectorPair -> {
            TempDisplayBlock tdb = tempDisplayBlockVectorPair.getA();
            double deltaYaw = currentLoc.getYaw() - tdb.getLoc().getYaw();
            Vector offset = tempDisplayBlockVectorPair.getB().rotateAroundY(Math.toRadians(-deltaYaw));
            tempDisplayBlockVectorPair.getA().moveTo(currentLoc.clone().add(offset));
        });
    }
}
