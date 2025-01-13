package com.sereneoasis.util;


import org.bukkit.Color;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * @author Sakrajin
 * Methods which are related to blocks
 */
public class Blocks {


    public static Block getBelowBlock(Block b, double distance) {
        Location loc = b.getLocation();
        Block block = b;
        if (loc.getWorld().rayTraceBlocks(loc, new Vector(0, -1, 0), distance, FluidCollisionMode.NEVER) != null) {
            block = loc.getWorld().rayTraceBlocks(loc, new Vector(0, -1, 0), distance, FluidCollisionMode.NEVER).getHitBlock();
        }
        return block;
    }


    public static Block getFacingBlock(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER, true) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER, true);
            block = rayTraceResult.getHitBlock();
        }
        return block;
    }

    public static Block getFacingBlock(Location loc, Vector dir, double distance) {
        if (loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER, true) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER, true);
            return rayTraceResult.getHitBlock();
        }
        return null;
    }


    public static Location getFacingBlockLoc(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER, true) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.NEVER, true);
            Location hitLoc = new Location(loc.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ());
            return hitLoc;
        }
        return null;
    }

    public static Location getFacingBlockLoc(Location loc, Vector dir, double distance) {
        if (loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER, true) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER, true);
            Location hitLoc = new Location(loc.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ());
            return hitLoc;
        }
        return null;
    }

    public static boolean playerLookingAtBlockDisplay(Player player, BlockDisplay target, double maxDistance, double size) {
        Location loc = target.getLocation();

        Location lowest = loc.clone().add(Vectors.getDown(loc, size / 2)).add(Vectors.getLeftSide(player, size / 2)).subtract(loc.getDirection().multiply(size / 2));
//        Particles.spawnParticle(Particle.FLAME, lowest, 1, 0, 0);
        Location highest = loc.clone().add(Vectors.getUp(loc, size / 2)).add(Vectors.getRightSide(player, size / 2)).add(loc.getDirection().multiply(size / 2));
//        Particles.spawnParticle(Particle.FLAME, highest, 1, 0, 0);
        BoundingBox boundingBox = new BoundingBox(lowest.getX(), lowest.getY(), lowest.getZ(), highest.getX(), highest.getY(), highest.getZ());
        boundingBox.expand(0.1, 0.1, 0.1);
        Location playerLoc = player.getEyeLocation().clone();
        Vector dir = player.getEyeLocation().getDirection().clone().normalize();
        RayTraceResult rayTraceResult = boundingBox.rayTrace(playerLoc.toVector(), dir, maxDistance);
        if (rayTraceResult != null) {
            return true;
        }
        return false;
    }


    public static Block getFacingBlockOrLiquid(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS) != null) {
            block = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS).getHitBlock();
        }
        return block;
    }

    public static Block getFacingBlockOrLiquid(Location loc, Vector dir, double distance) {
        Block block = null;
        if (loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.ALWAYS) != null) {
            block = loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.ALWAYS).getHitBlock();
        }
        return block;
    }

    public static BlockFace getFacingBlockFace(Location loc, Vector dir, double distance) {
        BlockFace blockFace = null;
        if (loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER) != null) {
            blockFace = loc.getWorld().rayTraceBlocks(loc, dir, distance, FluidCollisionMode.NEVER).getHitBlockFace();
        }
        return blockFace;
    }

    public static Location getFacingBlockOrLiquidLoc(Player player, double distance) {
        Location loc = player.getEyeLocation().clone();
        if (loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS) != null) {
            RayTraceResult rayTraceResult = loc.getWorld().rayTraceBlocks(loc, loc.getDirection(), distance, FluidCollisionMode.ALWAYS);
            return new Location(loc.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ());
        }
        return null;
    }

    public static Set<Block> getBlocksAroundPoint(Location loc, double radius) {
        Set<Block> blocks = new HashSet<>();
        for (double y = -radius; y < radius; y++) {
            for (double x = -radius; x < radius; x++) {
                for (double z = -radius; z < radius; z++) {
                    Block block = loc.clone().add(x, y, z).getBlock();
                    if (block.getLocation().distanceSquared(loc) < ((radius) * (radius))) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }

    public static Set<Block> getSolidBlocksAroundPoint(Location loc, double radius) {
        Set<Block> blocks = getBlocksAroundPoint(loc, radius);
        blocks.removeIf(block -> block.isPassable());
        return blocks;
    }


    public static List<Block> getBlocksAroundPoint(Location loc, double radius, Material type) {
        List<Block> blocks = new ArrayList<Block>();
        for (double y = -radius; y < radius; y++) {
            for (double x = -radius; x < radius; x++) {
                for (double z = -radius; z < radius; z++) {
                    Block block = loc.clone().add(x, y, z).getBlock();
                    if (block.getType() == type && block.getLocation().distanceSquared(loc) < ((radius) * (radius))) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }


    public static boolean isTopBlock(Block b) {
        if (b.getLocation().add(0, 1, 0).getBlock().getType().isSolid()) {
            return false;
        }
        return true;
    }

    public static boolean isSolid(Location loc) {
        return !loc.getBlock().isPassable();
    }


}