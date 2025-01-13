package com.sereneoasis;

import com.sereneoasis.util.Vectors;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftBlockDisplay;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Vector3d;

import java.util.*;

/**
 * @author Sakrajin
 * Represents temporary display blocks (which are entiites) and handles reverting
 */
public class TempDisplayBlock {
    private static final PriorityQueue<TempDisplayBlock> REVERT_QUEUE = new PriorityQueue<>(100, (t1, t2) -> (int) (t1.revertTime - t2.revertTime));

    private static final Set<TempDisplayBlock> TEMP_DISPLAY_BLOCK_SET = new HashSet<>();
    public static Random random = new Random();
    private final BlockDisplay blockDisplay;
    private long revertTime;

    private final double width;
    private final double height;
    private final double depth;

    public TempDisplayBlock(Location loc, Material block, final long revertTime, double width, double height, double depth, boolean glowing, Color color) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.blockDisplay = (BlockDisplay) loc.getWorld().spawn(loc, EntityType.BLOCK_DISPLAY.getEntityClass(), (entity) ->
        {
            BlockDisplay bDisplay = (BlockDisplay) entity;
            bDisplay.setBlock(block.createBlockData());
            Transformation transformation = bDisplay.getTransformation();

            transformation.getTranslation().set(new Vector3d(-width / 2, -height / 2, -depth / 2));
            transformation.getScale().set(width, height, depth);

            bDisplay.setViewRange(100);

            bDisplay.setTransformation(transformation);
            if (glowing) {
                bDisplay.setGlowing(true);
                bDisplay.setGlowColorOverride(color);
            }

        });

        this.revertTime = System.currentTimeMillis() + revertTime;
        REVERT_QUEUE.add(this);
        TEMP_DISPLAY_BLOCK_SET.add(this);
    }


    public static Set<TempDisplayBlock> getTempDisplayBlockSet() {
        return TEMP_DISPLAY_BLOCK_SET;
    }

    public static PriorityQueue<TempDisplayBlock> getRevertQueue() {
        return REVERT_QUEUE;
    }

    public void automaticRevert() {
        if (blockDisplay != null) {
            blockDisplay.remove();
        }
        REVERT_QUEUE.remove();
    }

    public void revert() {
        blockDisplay.remove();
    }

    public long getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(long newRevertTime) {
        revertTime = newRevertTime;
    }

    public void moveTo(Location newLoc) {
        //this.blockDisplay.teleport(newLoc);

        try {
            newLoc.checkFinite();
            Vector diff = Vectors.getDirectionBetweenLocations(blockDisplay.getLocation(), newLoc);

            ((CraftBlockDisplay) blockDisplay).getHandle().move(MoverType.SELF, new Vec3(diff.getX(), diff.getY(), diff.getZ()));
            ((CraftBlockDisplay) blockDisplay).getHandle().setRot(newLoc.getYaw(), 0);

        } catch (IllegalArgumentException exception) {
            SereneModels.plugin.getLogger().warning("Block display new location invalid");
        }


    }

    private boolean isLocSolid(List<Location> locs) {
        return locs.stream().anyMatch(location -> location.getBlock().getType().isSolid());
    }

//    public void moveToAndMaintainFacing(Location newLoc) {
////        ((CraftBlockDisplay) blockDisplay).getHandle().moveTo(newLoc.getX(), newLoc.getY(), newLoc.getZ(), ((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());
//
////        blockDisplay.setTeleportDuration(0);
////        blockDisplay.teleport(newLoc);
//
////        ((CraftBlockDisplay) blockDisplay).getHandle().noPhysics = false;
//        try {
//            newLoc.checkFinite();
//            Vector diff = Vectors.getDirectionBetweenLocations(blockDisplay.getLocation(), newLoc);
//
//            ((CraftBlockDisplay) blockDisplay).getHandle().move(MoverType.SELF, new Vec3(diff.getX(), diff.getY(), diff.getZ()));
//            ((CraftBlockDisplay) blockDisplay).getHandle().setRot(((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());
//
//            Location loc = newLoc.clone();
//            loc.setPitch(((CraftBlockDisplay) blockDisplay).getPitch());
//            loc.setYaw(((CraftBlockDisplay) blockDisplay).getYaw());
//
//            Vector left = Vectors.getLeftSideNormalisedVector(loc);
//            Vector right = Vectors.getRightSideNormalisedVector(loc);
//
//            Vector forward = new Vector(0, 0, 1).rotateAroundX(-Math.toRadians(((CraftBlockDisplay) blockDisplay).getPitch())).rotateAroundY(-Math.toRadians(((CraftBlockDisplay) blockDisplay).getYaw()));
////            Vector left = forward.clone().rotateAroundY(Math.toRadians(90));
////            Vector right = forward.clone().rotateAroundY(Math.toRadians(-90));
//
//
//            //bottom left
//            Location closeBottomLeft = loc.clone().add(Vectors.getDown(loc, size / 2)).add(left.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
//            Location farBottomLeft = loc.clone().add(Vectors.getDown(loc, size / 2)).add(left.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
//            Location closeBottomRight = loc.clone().add(Vectors.getDown(loc, size / 2)).add(right.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
//            Location farBottomRight = loc.clone().add(Vectors.getDown(loc, size / 2)).add(right.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
//
//            // top right
//            Location closeTopRight = loc.clone().add(Vectors.getUp(loc, size / 2)).add(right.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
//            Location farTopRight = loc.clone().add(Vectors.getUp(loc, size / 2)).add(right.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
//            Location closeTopLeft = loc.clone().add(Vectors.getUp(loc, size / 2)).add(left.clone().multiply(size / 2)).subtract(forward.clone().multiply(size / 2));
//            Location farTopLeft = loc.clone().add(Vectors.getUp(loc, size / 2)).add(left.clone().multiply(size / 2)).add(forward.clone().multiply(size / 2));
//
//            List<Location> locs = List.of(closeBottomLeft, farBottomLeft, closeBottomRight, farBottomRight, closeTopRight, farTopRight, closeTopLeft, farTopLeft);
//
////            Location maxLoc =  boundingBox.getMax().toLocation(blockDisplay.getWorld());
////            Location minLoc = boundingBox.getMin().toLocation(blockDisplay.getWorld());
//
//            if (!isInvisible() && !this.getBlockDisplay().isGlowing() && isLocSolid(locs)) {
//                this.setInvisible();
//            } else if (this.isInvisible() && !isLocSolid(locs)) {
//                setVisible();
//            }
//
//
//        } catch (IllegalArgumentException exception) {
//            SereneModels.plugin.getLogger().warning("Block display new location invalid");
//        }

//        ((CraftBlockDisplay) blockDisplay).getHandle().teleportTo(newLoc.getX(), newLoc.getY(), newLoc.getZ());
//        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(((CraftBlockDisplay) blockDisplay).getYaw(), ((CraftBlockDisplay) blockDisplay).getPitch());
//    }

    public void rotate(float yaw, float pitch) {
        ((CraftBlockDisplay) blockDisplay).getHandle().setRot(yaw, pitch);
    }

    public BlockDisplay getBlockDisplay() {
        return blockDisplay;
    }

    public void setVisible() {
        ((CraftBlockDisplay) blockDisplay).getHandle().setViewRange(100);
    }

    public void setInvisible() {
        ((CraftBlockDisplay) blockDisplay).getHandle().setViewRange(0);
    }

    private boolean isInvisible() {
        return ((CraftBlockDisplay) blockDisplay).getHandle().getViewRange() == 0;

    }

    public Location getLoc() {
        return blockDisplay.getLocation();
    }


}
