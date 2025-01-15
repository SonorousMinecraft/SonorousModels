package com.sereneoasis.physics;

import com.sereneoasis.TempDisplayBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class PhysicsObject {

    private static final double MAX_SPEED = 10.0; // Example max speed
    private final double mass;
    private final World world;
    private double x;
    private double y;
    private double z;
    private double velocityX = 0; // Initialize to zero
    private double velocityY = 0; // Initialize to zero
    private double velocityZ = 0; // Initialize to zero
    private double accelerationX = 0; // Initialize to zero
    private double accelerationY = 0; // Initialize to zero
    private double accelerationZ = 0; // Initialize to zero
    // Rotation properties
    private double rotationAngle; // Current rotation in radians
    private double angularVelocity; // Angular speed
    private double angularAcceleration; // Rate of change of angular velocity


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }



    public PhysicsObject(Location loc, double mass) {
        this.mass = mass;
        this.world = loc.getWorld();
        x = loc.getX();
        y = loc.getY();
        z = loc.getZ();

        // Initialize rotation properties
        this.rotationAngle = -Math.toRadians(loc.getYaw());
        this.angularVelocity = 0;
        this.angularAcceleration = 0;
    }

    public Location getLocation(){
        Location loc = new Location(world, x, y, z);
        loc.setYaw((float) -Math.toDegrees(rotationAngle));
        return loc;
    }

    // Getters and setters for rotation properties
    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle % (2 * Math.PI); // Keep the angle within 0 to 2π
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getAngularAcceleration() {
        return angularAcceleration;
    }

    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }

    // Apply linear force to the object
    public void applyForce(double forceX, double forceY, double forceZ) {
        accelerationX += forceX / mass;
        accelerationY += forceY / mass;
        accelerationZ += forceZ / mass;
    }

    public void applyScalarForceReduction(double forceX, double forceY, double forceZ) {
        accelerationX /= forceX;
        if (accelerationX < 1) {
            accelerationX = 0;
        }
        accelerationY /= forceY;
        if (accelerationY < 1) {
            accelerationY = 0;
        }
        accelerationZ /= forceZ;
        if (accelerationZ < 1) {
            accelerationZ = 0;
        }
    }

    // Apply torque (angular force) to the object
    public void applyTorque(double torque) {
        // Angular acceleration = torque / momentOfInertia
        double momentOfInertia = mass; // For simplicity, using mass as moment of inertia
        angularAcceleration += torque / momentOfInertia;
    }

    // Update method to apply rotational mechanics
    public void update(double timeStep) {

        // Create the current location based on x, y, and z
        Location currentLocation = new Location(world, x, y, z);

        // Calculate the new position based on current velocity
        double newX = x + velocityX * timeStep;
        double newY = y + velocityY * timeStep;
        double newZ = z + velocityZ * timeStep;

        // Define the new target location with initial Y coordinate
        Location targetLocation = new Location(world, newX, newY, newZ);

        // Create the direction vector and the maximum distance
        Vector direction = targetLocation.toVector().subtract(currentLocation.toVector());
        double maxDistance = direction.length();

        // Check if there is a movement
        if (maxDistance > 0) {
            RayTraceResult rayTraceResult = world.rayTraceBlocks(currentLocation, direction, maxDistance + 2);

            // Check if the ray trace result is not null
            if (rayTraceResult != null) {
                Block block = rayTraceResult.getHitBlock();
                if (block != null && !block.isPassable()) {

                    Vector newPosition = rayTraceResult.getHitPosition();
                    x = newPosition.getX();
                    y = newPosition.getY();
                    z = newPosition.getZ();

                    if (rayTraceResult.getHitBlockFace() != null ){
                        Vector reboundDirection = rayTraceResult.getHitBlockFace().getDirection();
                        if (reboundDirection.getX() != 0){
                            velocityX = 0;
                            accelerationX = 0;
                            x += reboundDirection.getX();
                        }
                        if (reboundDirection.getY() != 0){
                            velocityY = 0;
                            accelerationY= 0;
                            y += reboundDirection.getY();
                        }

                        if (reboundDirection.getZ() != 0){
                            velocityZ = 0;
                            accelerationZ = 0;
                            z += reboundDirection.getZ();

                        }



                    }
                } else {
                    // No collision, update the entity's position
                    x = newX;
                    y = newY;
                    z = newZ;
                }
            } else {
                // No collision, update the entity's position
                x = newX;
                y = newY;
                z = newZ;
            }

        }


// Update linear velocities (apply acceleration)
        velocityX += accelerationX * timeStep;
        velocityY += accelerationY * timeStep;
        velocityZ += accelerationZ * timeStep;

// Calculate the current speed after updating velocities
        double currentSignedSpeed = getSpeed();

// Check if the signed speed exceeds MAX_SPEED
        if (Math.abs(currentSignedSpeed) > MAX_SPEED) {
            // Get the current velocity as a Vector
            Vector currentVelocity = new Vector(velocityX, velocityY, velocityZ);

            // Normalize the velocity vector to keep its direction
            Vector normalizedVelocity = currentVelocity.normalize();

            // Scale it to MAX_SPEED, maintaining the direction
            Vector cappedVelocity = normalizedVelocity.multiply(MAX_SPEED);

            // Update velocity components to ensure speed limit is respected
            velocityX = cappedVelocity.getX();
            velocityY = cappedVelocity.getY();
            velocityZ = cappedVelocity.getZ();
        }

        // Update angular velocity and rotation
        angularVelocity += angularAcceleration * timeStep;
        setRotationAngle(rotationAngle + angularVelocity * timeStep);

        // Reset angular acceleration after applying
        angularAcceleration = 0; // Reset to zero or update accordingly

        // Move entity to new position
//        entity.moveTo(new Location(world, x, y, z));

//        entity.setYRot((float) -Math.toDegrees(rotationAngle));
    }

    public Vector getVelocity() {
        return new Vector(velocityX, velocityY, velocityZ);
    }


    // Returns signed speed reflecting direction
    public double getSpeed() {
        Vector velocity = getVelocity();

        // Calculate signed speed based on the rotation angle
        double forwardX = Math.cos(getRotationAngle());
        double forwardZ = Math.sin(getRotationAngle());

        // Calculate the dot product of velocity and forward direction to get signed speed
        double signedSpeed = (velocity.getX() * forwardX + velocity.getZ() * forwardZ);

        return signedSpeed;
    }

    public void halt() {
        this.velocityX = 0;
        this.velocityY = 0;
        this.velocityZ = 0;

        this.accelerationX = 0;
        this.accelerationY = 0;
        this.accelerationZ = 0;

    }



    public double getMass() {
        return mass;
    }
}
