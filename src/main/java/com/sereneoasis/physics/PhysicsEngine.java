package com.sereneoasis.physics;

import com.sereneoasis.SereneModels;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine {

    private static final double GRAVITY = -1.0; // Gravity constant
    private static final double TIME_STEP = 1.0 / 20.0; // 20 ticks/second
    private static final double DRAG_COEFFICIENT = 0.1; // Drag coefficient (adjust as needed)
    private final List<PhysicsObject> physicsObjects; // List to store physics objects

    public PhysicsEngine() {
        physicsObjects = new ArrayList<>(); // Initialize the list

        // Run the scheduler to update physics every tick (20 times a second)
        Bukkit.getScheduler().runTaskTimer(SereneModels.plugin, this::update, 0L, 1L);
    }

    // Method to add a new PhysicsObject to the engine
    public void addPhysicsObject(PhysicsObject physicsObject) {
        physicsObjects.add(physicsObject);
    }

    // Update method to iterate through all physics objects and update them
    public void update() {
        for (PhysicsObject physicsObject : physicsObjects) {
            // Apply gravity to the object
//            physicsObject.applyForce(0, GRAVITY * physicsObject.getMass(), 0); // Assuming getMass() is implemented

            // Calculate drag force based on current velocity
            Vector velocity = physicsObject.getVelocity(); // Get the current velocity vector
            double dragX = -DRAG_COEFFICIENT * velocity.getX();
            double dragY = -DRAG_COEFFICIENT * velocity.getY();
            double dragZ = -DRAG_COEFFICIENT * velocity.getZ();

            // Apply the drag force
            physicsObject.applyForce(dragX, dragY, dragZ);

            // Update the physics object
            physicsObject.update(TIME_STEP);
        }
    }

    // Optionally, create a method to remove physicsObjects
    public void removePhysicsObject(PhysicsObject physicsObject) {
        physicsObjects.remove(physicsObject);
    }
}