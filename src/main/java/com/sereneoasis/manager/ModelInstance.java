package com.sereneoasis.manager;

import com.sereneoasis.SereneModels;
import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.ai.NavigationMesh;
import com.sereneoasis.ai.Pathfinding;
import com.sereneoasis.physics.PhysicsObject;
import com.sereneoasis.skeleton.*;
import com.sereneoasis.util.Vectors;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;

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

    private PhysicsObject physicsObject;

    public void tickAnimationManager(){
        animationManager.tick();
    }

    public Set<TempDisplayBlock> getDisplayBlocksFromBone(String bone) {
        return boneTempDisplayBlockMap.get(bone);
    }

    private Location targetLoc;

    private final NavigationMesh navigationMesh;
    private boolean isNavMeshRefreshing = false;

    private List<Location> path;



    public ModelInstance(LayerDefinition layerDefinition, Location loc) {

        MeshDefinition meshDefinition = layerDefinition.getMeshDefinition();
        PartDefinition rootPart = meshDefinition.getRoot();

        physicsObject = new PhysicsObject(loc, 30);

        SereneModels.plugin.getPhysicsEngine().addPhysicsObject(physicsObject);

        processParts(rootPart, loc);

        this.animationManager = new AnimationManager(this);
        SereneModels.plugin.getModelManager().addModel(this);

        this.navigationMesh = new NavigationMesh();
        this.navigationMesh.updateMesh(loc);


    }



    public void processParts(PartDefinition partDefinition, Location loc) {
        // Clone the current location to make it current for this part

        PartPose pose = partDefinition.getPose();
        // Process cubes of the current part

        for (Cube cube : partDefinition.getCubes()) {
            Location currentLocation = loc.clone();

            currentLocation.add(pose.getOffset());

            if (partDefinition.getName().contains("right")){
                TempDisplayBlock tdb = new TempDisplayBlock(currentLocation, Material.OBSIDIAN, 1,
                        cube.getWidth(), cube.getHeight(), cube.getDepth(),
                        -cube.getX(), cube.getY(), cube.getZ(),
                        true, Color.PURPLE);

                boneTempDisplayBlockMap.putIfAbsent(partDefinition.getName(), new HashSet<>());
                boneTempDisplayBlockMap.get(partDefinition.getName()).add(tdb);
//                cubes.put(tdb, pose.getOffset().add(new Vector(cube.getX(), 0, 0)));
                cubes.put(tdb, pose.getOffset());
                tdb.attachPhysicsEngine(physicsObject);
            } else {
                TempDisplayBlock tdb = new TempDisplayBlock(currentLocation, Material.OBSIDIAN, 1,
                        cube.getWidth(), cube.getHeight(), cube.getDepth(),
                        cube.getX(), cube.getY(), cube.getZ(),
                        true, Color.PURPLE);

                boneTempDisplayBlockMap.putIfAbsent(partDefinition.getName(), new HashSet<>());
                boneTempDisplayBlockMap.get(partDefinition.getName()).add(tdb);
                cubes.put(tdb, pose.getOffset());
                tdb.attachPhysicsEngine(physicsObject);

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

        if ( !this.navigationMesh.getAdjacencyMap().containsKey(newLoc.getBlock().getLocation()) || !this.navigationMesh.getAdjacencyMap().containsKey(physicsObject.getLocation().getBlock().getLocation()) ) {

            CompletableFuture<Void> future = this.navigationMesh.updateMesh(this.physicsObject.getLocation());
            this.isNavMeshRefreshing = true;

            Bukkit.broadcastMessage("refreshing navmesh");

            future.thenRun(() -> {
                this.isNavMeshRefreshing = false;
                this.path = new Pathfinding(navigationMesh.getAdjacencyMap()).generatePath(physicsObject.getLocation(), newLoc);
                if (!path.isEmpty()) {
                    Bukkit.broadcastMessage("path genereated " + path.size());
                }

            });
        } else {
            this.path = new Pathfinding(navigationMesh.getAdjacencyMap()).generatePath(physicsObject.getLocation(), newLoc);
            if (!path.isEmpty()) {
                Bukkit.broadcastMessage("path genereated " + path.size());
            }
        }

    }

    public void updateCubes() {

        if (path != null && !path.isEmpty()) {
            Vector facing = Vectors.getDirectionBetweenLocations(physicsObject.getLocation(), targetLoc);

            Location nextPathLoc = path.get(0).setDirection(facing);

            if (nextPathLoc.distanceSquared(physicsObject.getLocation()) < 1) {
                path.remove(0);
            }

            double currentYawDegs = Math.toDegrees(physicsObject.getRotationAngle());
            double desiredYawDegs = -nextPathLoc.getYaw();

            if (desiredYawDegs < 0) {
                desiredYawDegs += 360;
            }

            double deltaYaw = (desiredYawDegs - currentYawDegs) % 360 ; //following conventions i.e. increase means turning leftwards
            System.out.println(deltaYaw);
            if (Math.abs(deltaYaw) < ANGLE_CHANGE + 1){
                Bukkit.broadcastMessage("acceleration");
                accelerate();
            } else {
//                brake();
                physicsObject.halt();
                if (deltaYaw > 180) {
                    turnLeft();
                } else {
                    turnRight();
                }
            }

        }


//        cubes.forEach((tdb, value) -> {
//
//            double deltaYaw =  tdb.getBlockDisplay().getLocation().getYaw() - physicsObject.getLocation().getYaw();
//            value.rotateAroundY(Math.toRadians(deltaYaw));
//
//            tdb.setRotation(physicsObject.getLocation().getYaw(), physicsObject.getLocation().getPitch());
//
//            tdb.moveTo(physicsObject.getLocation().clone().add(value.clone().rotateAroundAxis( Vectors.getRightSideNormalisedVector(physicsObject.getLocation()),-Math.toRadians(physicsObject.getLocation().getPitch()))));
//
//        });


    }

    private static final double ACCELERATION_FORCE = 1.0; // Adjust as necessary
    // Method to accelerate the vehicle
    public void accelerate() {
        // Apply acceleration to the seat's physics object in the direction it's facing
        Vector direction = new Vector(Math.sin(physicsObject.getRotationAngle()), 0,
                Math.cos(physicsObject.getRotationAngle()));
        physicsObject.applyForce(direction.getX() * ACCELERATION_FORCE, 0, direction.getZ() * ACCELERATION_FORCE);
    }

    // Constants
    private static final double BRAKE_FORCE = 5.0; // Magnitude of the brake force
    private static final double MIN_SPEED = 0.5; // Minimum speed to prevent reversal

    // Method to apply brakes to the vehicle
    public void brake() {
        // Calculate current speed
        double currentSpeed = physicsObject.getSpeed();

        // Check if the vehicle is moving and applies brakes accordingly
        if (currentSpeed > MIN_SPEED) {
            // Forward direction based on the vehicle's rotation
            Vector forwardDirection = new Vector(Math.sin(physicsObject.getRotationAngle()),
                    0,
                    Math.cos(physicsObject.getRotationAngle()));

            // Calculate the new speed after applying brake force
            double newSpeed = currentSpeed - BRAKE_FORCE/physicsObject.getMass();

            if (newSpeed < 0) {
                newSpeed = 0; // Prevent speed from going negative
            }

            // Calculate resulting force to apply based on new speed
            double resultingForceMagnitude = currentSpeed - newSpeed; // Calculate the force based on deceleration

            // Apply the braking force (the force is in the opposite direction of movement)
            // Ensure we only apply force if speed was above minimum
            if (resultingForceMagnitude > 0) {
                Vector brakeDirection = forwardDirection.clone().multiply(-1); // Invert the direction
                physicsObject.applyForce(brakeDirection.getX() * resultingForceMagnitude,
                        0,
                        brakeDirection.getZ() * resultingForceMagnitude);
            } else {
                physicsObject.halt();
            }
        } else {
            physicsObject.halt();
        }
    }

    private static final double ANGLE_CHANGE = 10;

    // Method to turn the vehicle left
    public void turnLeft() {
        // Change to a predefined angle (negative) for turning left
        double turnAngle = Math.toRadians(-ANGLE_CHANGE);
        updateRotationAngle(turnAngle);
    }

    // Method to turn the vehicle right
    public void turnRight() {
        // Change to a predefined angle (positive) for turning right
        double turnAngle = Math.toRadians(ANGLE_CHANGE); // Adjust this value for how sharply to turn
        updateRotationAngle(turnAngle);
    }

    // Helper method to update the rotation angle based on a given change
    private void updateRotationAngle(double turnAngle) {
        // Update the rotation angle of the physics object


        double currentAngle = physicsObject.getRotationAngle();
        double newAngle = currentAngle + (turnAngle);

        physicsObject.setRotationAngle(newAngle);


        cubes.forEach((tdb, value) -> {

            value.rotateAroundY(turnAngle);
//
//            tdb.setRotation(physicsObject.getLocation().getYaw(),0 );

            tdb.moveTo(physicsObject.getLocation().clone().add(value.clone()));

        });


    }
}
