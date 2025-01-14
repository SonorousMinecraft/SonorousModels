package com.sereneoasis.manager;

import com.sereneoasis.SereneModels;
import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.skeleton.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ModelInstance {

    private final HashMap<TempDisplayBlock, Vector> cubes = new HashMap<>();

    public void putCube(TempDisplayBlock tdb, Vector offset){
        cubes.put(tdb, offset);
    }

    public Vector getCubeOffset(TempDisplayBlock tdb){
        return cubes.get(tdb);
    }

    private final HashMap<String, Set<TempDisplayBlock>> boneTempDisplayBlockMap = new HashMap<>();

    private AnimationManager animationManager;

    public void tickAnimationManager(){
        animationManager.tick();
    }

    public Set<TempDisplayBlock> getDisplayBlocksFromBone(String bone) {
        return boneTempDisplayBlockMap.get(bone);
    }

    private Location currentLoc;

    public ModelInstance(LayerDefinition layerDefinition, Location loc) {

        MeshDefinition meshDefinition = layerDefinition.getMeshDefinition();
        PartDefinition rootPart = meshDefinition.getRoot();
        this.currentLoc = loc.clone();

        processParts(rootPart, currentLoc);

        SereneModels.plugin.getModelManager().addModel(this);
        this.animationManager = new AnimationManager(this);
    }

    public void processParts(PartDefinition partDefinition, Location loc) {
        // Clone the current location to make it current for this part

        PartPose pose = partDefinition.getPose();
        // Process cubes of the current part

        for (Cube cube : partDefinition.getCubes()) {
            Location currentLocation = loc.clone();

            currentLocation.add(pose.getOffset());

            if (partDefinition.getName().contains("right")){
                TempDisplayBlock tdb = new TempDisplayBlock(currentLocation, Material.SLIME_BLOCK, 1,
                        cube.getWidth(), cube.getHeight(), cube.getDepth(),
                        -cube.getX(), cube.getY(), cube.getZ(),
                        true, Color.PURPLE);

                boneTempDisplayBlockMap.putIfAbsent(partDefinition.getName(), new HashSet<>());
                boneTempDisplayBlockMap.get(partDefinition.getName()).add(tdb);
                cubes.put(tdb, pose.getOffset().add(new Vector(cube.getX(), 0, 0)));
            } else {
                TempDisplayBlock tdb = new TempDisplayBlock(currentLocation, Material.SLIME_BLOCK, 1,
                        cube.getWidth(), cube.getHeight(), cube.getDepth(),
                        cube.getX(), cube.getY(), cube.getZ(),
                        true, Color.PURPLE);

                boneTempDisplayBlockMap.putIfAbsent(partDefinition.getName(), new HashSet<>());
                boneTempDisplayBlockMap.get(partDefinition.getName()).add(tdb);
                cubes.put(tdb, pose.getOffset());
            }

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
        cubes.forEach((tdb, value) -> {
            double deltaYaw =  tdb.getBlockDisplay().getLocation().getYaw() - currentLoc.getYaw();
            value.rotateAroundY(Math.toRadians(deltaYaw));
            tdb.moveTo(currentLoc.clone().add(value));
        });
    }
}
