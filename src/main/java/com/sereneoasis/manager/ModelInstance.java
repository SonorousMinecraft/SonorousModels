package com.sereneoasis.manager;

import com.sereneoasis.SereneModels;
import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.skeleton.Cube;
import com.sereneoasis.skeleton.LayerDefinition;
import com.sereneoasis.skeleton.MeshDefinition;
import com.sereneoasis.skeleton.PartDefinition;
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

    public ModelInstance(LayerDefinition layerDefinition, Location loc){

        MeshDefinition meshDefinition = layerDefinition.getMeshDefinition();
        PartDefinition rootPart = meshDefinition.getRoot();
        this.currentLoc = loc.clone();

        for (PartDefinition child : rootPart.getChildren()) { // Optionally iterate through children if applicable
            // Iterate through cubes of this part
            for (Cube cube : child.getCubes()) {
                // Calculate cube position in world space
                Location cubeLoc = loc.clone();
                // Use cube's properties to decide its placement
                float x = cube.getX(); // Assuming you have a getter in Cube to access these properties
                float y = cube.getY();
                float z = cube.getZ();
                Vector offset = new Vector(x, y, z);
                cubeLoc.add(offset);
                float width = cube.getWidth();
                float height = cube.getHeight();
                float depth = cube.getDepth();

                TempDisplayBlock tdb = new TempDisplayBlock(cubeLoc, Material.SLIME_BLOCK, 1, width, height, depth, true, Color.PURPLE);

                cubes.add(new Pair<>(tdb, offset));
            }
        }
        SereneModels.plugin.getModelManager().addModel(this);
    }

    public void updateLocation(Location newLoc){
        this.currentLoc = newLoc.clone();
    }

    public void updateCubes(){
        cubes.forEach(tempDisplayBlockVectorPair -> {
            TempDisplayBlock tdb = tempDisplayBlockVectorPair.getA();
            double deltaYaw = currentLoc.getYaw() - tdb.getLoc().getYaw();
            Vector offset = tempDisplayBlockVectorPair.getB().rotateAroundY(Math.toRadians(-deltaYaw));
            tempDisplayBlockVectorPair.getA().moveTo(currentLoc.clone().add(offset));
        });
    }
}
