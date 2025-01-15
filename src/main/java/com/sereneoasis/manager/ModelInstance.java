package com.sereneoasis.manager;

import com.sereneoasis.SereneModels;
import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.ai.NavigationMesh;
import com.sereneoasis.ai.Pathfinding;
import com.sereneoasis.skeleton.*;
import com.sereneoasis.util.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
    private Location targetLoc;

    private final NavigationMesh navigationMesh;
    private boolean isNavMeshRefreshing = false;

    private List<Location> path;



    public ModelInstance(LayerDefinition layerDefinition, Location loc) {

        MeshDefinition meshDefinition = layerDefinition.getMeshDefinition();
        PartDefinition rootPart = meshDefinition.getRoot();
        this.currentLoc = loc.clone();

        processParts(rootPart, currentLoc);

        this.animationManager = new AnimationManager(this);
        SereneModels.plugin.getModelManager().addModel(this);

        this.navigationMesh = new NavigationMesh();
        this.navigationMesh.updateMesh(this.currentLoc);


//        BukkitTask task = Bukkit.getScheduler().runTaskTimer(SereneModels.plugin, () -> {
//
//            if (!Bukkit.getScheduler().isCurrentlyRunning(taskId) && path == null) {
//                Bukkit.broadcastMessage("generated path");
//
//            }
//        }, 100L, 20L);

//        this.updateLocation(this.currentLoc.clone().add(0,0,100));
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
//                cubes.put(tdb, pose.getOffset().add(new Vector(cube.getX(), 0, 0)));
                cubes.put(tdb, pose.getOffset());
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

    public void updateGoalLocation(Location newLoc) {
        this.targetLoc = newLoc.clone();
        if (isNavMeshRefreshing){
            return;
        }

        if ( !this.navigationMesh.getAdjacencyMap().containsKey(newLoc.getBlock().getLocation()) || !this.navigationMesh.getAdjacencyMap().containsKey(currentLoc.getBlock().getLocation()) ) {

            CompletableFuture<Void> future = this.navigationMesh.updateMesh(this.currentLoc);
            this.isNavMeshRefreshing = true;

            Bukkit.broadcastMessage("refreshing navmesh");

            future.thenRun(() -> {
                this.isNavMeshRefreshing = false;
                this.path = new Pathfinding(navigationMesh.getAdjacencyMap()).generatePath(currentLoc, newLoc);
                if (!path.isEmpty()) {
                    Bukkit.broadcastMessage("path genereated " + path.size());
                }

            });
        } else {
            this.path = new Pathfinding(navigationMesh.getAdjacencyMap()).generatePath(currentLoc, newLoc);
            if (!path.isEmpty()) {
                Bukkit.broadcastMessage("path genereated " + path.size());
            }
        }

    }

    public void updateCubes() {

        if (path != null && !path.isEmpty()) {
            Vector facing = Vectors.getDirectionBetweenLocations(currentLoc, targetLoc);

            this.currentLoc = path.get(0).setDirection(facing);

            path.remove(0);
        }

        cubes.forEach((tdb, value) -> {

            double deltaYaw =  tdb.getBlockDisplay().getLocation().getYaw() - currentLoc.getYaw();
            value.rotateAroundY(Math.toRadians(deltaYaw));

//            double deltaPitch =  tdb.getBlockDisplay().getLocation().getPitch() - currentLoc.getPitch();
//
//            value.rotateAroundX(Math.toRadians(deltaPitch));

            tdb.moveTo(currentLoc.clone().add(value));

        });
    }
}
