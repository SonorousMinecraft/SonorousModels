package com.sereneoasis.physics;

import com.sereneoasis.listeners.ClientboundMoveEntityPacketFixed;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PhysicsDisplay extends Display.BlockDisplay {

    private PhysicsObject physicsObject;


    public PhysicsDisplay(Level world) {
        super(EntityType.BLOCK_DISPLAY, world);
    }

    private Location oldLocation;

    public void attachPhysics( PhysicsObject physicsObject){
        this.physicsObject = physicsObject;
        this.oldLocation = physicsObject.getLocation().clone();
    }

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();



    @Override
    public void tick() {
        super.tick();

//        if (passenger != null && getPassengers().isEmpty()){
//            VehicleRepresentation.dismountVehicle((Player) passenger.getBukkitEntity());
//        } else if (passenger == null && !getPassengers().isEmpty()){
//            passenger = (net.minecraft.world.entity.player.Player) getPassengers().get(0);
//        }

        if (physicsObject != null) {
            Location newLocation = physicsObject.getLocation();
            Vector diff = newLocation.clone().subtract(oldLocation.clone()).toVector();

            // Define how many intermediate packets to send and the delay
            int steps = 50; // Number of packets to send
            long delayBetweenPackets = 1; // Delay in milliseconds

//            // Calculate the amount to move each step
//            double stepX = diff.getX() / steps;
//            double stepY = diff.getY() / steps;
//            double stepZ = diff.getZ() / steps;

            // Gather all players before starting the interpolation
            Player[] players = Bukkit.getServer().getOnlinePlayers().toArray(new Player[0]);


            double coveredStepX = 0;
            double coveredStepY = 0;
            double coveredStepZ = 0;



            // Schedule sending packets at intervals
            for (int i = 1; i < steps; i++) {
                final int currentStep = i; // Final variable for the lambda


                // Calculate delta for the current step
                int remainingSteps = steps - currentStep;

                double remainingX = diff.getX()-coveredStepX;
                double remainingY = diff.getY()-coveredStepY;
                double remainingZ = diff.getZ()-coveredStepZ;

                short deltaX = (short) Math.round(remainingX * 4096/remainingSteps );
                short deltaY = (short) Math.round(remainingY * 4096/remainingSteps );
                short deltaZ = (short) Math.round(remainingZ * 4096/remainingSteps );


                coveredStepX+= (double) deltaX /4096;
                coveredStepY+= (double) deltaY /4096;
                coveredStepZ+= (double) deltaZ /4096;

//                oldLocation.add(coveredStepX, coveredStepY, coveredStepZ);

//                executorService.schedule(() -> {
//
//                    // Create the movement packet with deltas
//                    ClientboundMoveEntityPacket.PosRot moveEntityPacket =
//                            new ClientboundMoveEntityPacket.PosRot(this.getId(),
//                                    deltaX,
//                                    deltaY,
//                                    deltaZ,
//                                    (byte) radiansToByte(physicsObject.getRotationAngle()),
//                                    (byte) 0,
//                                    true);
//
//                    // Send the movement packet to each player
//                    for (Player player : players) {
//                        ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
//                        nmsPlayer.connection.send(moveEntityPacket);
//                    }
//                }, delayBetweenPackets * currentStep, TimeUnit.MILLISECONDS); // Schedule with delay
            }

            oldLocation = newLocation.clone(); // Update to the new oldLocation

            // Finally, move the entity to the new location
            this.move(MoverType.SELF, new Vec3(diff.toVector3f()));

            this.setRot((float)- Math.toDegrees(physicsObject.getRotationAngle()), 0);
        }
    }

    public static byte radiansToByte(double radians) {
        // Convert from radians to degrees
        double degrees = - radians * (180.0 / Math.PI);

        // Normalize the angle to be within the range [0, 360)
        degrees = degrees % 360;
        if (degrees < 0) {
            degrees += 360; // Ensure we have a positive angle.
        }

        // Convert degrees to a value in the range [0, 255]
        int byteValue = (int) Math.round((degrees / 360) * 255);

        // Ensure it fits within the byte range [0, 255]
        byteValue = Math.min(255, Math.max(0, byteValue));

        return (byte) byteValue; // Convert to byte and return
    }

}
