package com.sereneoasis.ai;

import com.sereneoasis.SereneModels;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class NavigationMesh {

    private ConcurrentHashMap<Location, Set<Location>> adjacencyMap = new ConcurrentHashMap<>();

    public NavigationMesh(){

    }

    public ConcurrentHashMap<Location, Set<Location>> getAdjacencyMap(){
        return this.adjacencyMap;
    }


    public CompletableFuture<Void> updateMesh(Location loc){

        CompletableFuture<Void> future = new CompletableFuture<>();

        World world  =  loc.getWorld();
        int radius = 4;
        int chunkStartX = loc.getChunk().getX() - radius/2;
        int chunkStartZ = loc.getChunk().getZ() - radius/2;
        Set<ChunkSnapshot> chunkSnapshots = new HashSet<>();
        for (int chunkX = chunkStartX ; chunkX < chunkStartX + radius ; chunkX ++ ){
            for (int chunkZ = chunkStartZ ; chunkZ < chunkStartZ + radius ; chunkZ ++ ) {
                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot(false ,false, false);

                chunkSnapshots.add(chunkSnapshot);
            }
        }

        int minWorldHeight = world.getMinHeight();
        int maxWorldHeight = world.getMaxHeight();

        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(SereneModels.plugin, () -> {

            Set<Location> locations = new HashSet<>();

            for (ChunkSnapshot chunkSnapshot : chunkSnapshots) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = minWorldHeight ; y < maxWorldHeight; y++) {
                            if (isAccessible(chunkSnapshot.getBlockType(x, y, z))) {
                                locations.add(new Location(world, 16 * chunkSnapshot.getX() + x, y, 16 * chunkSnapshot.getZ() + z));
                            }
                        }
                    }
                }
            }

            adjacencyMap.clear();
            for (Location locA : locations) {
                Set<Location> neighbors = new HashSet<>();

                // Check all 6 possible neighbors
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            // Check only immediate neighbors (dx, dy, dz can only be -1, 0, or 1)
                            if (Math.abs(dx) + Math.abs(dy) + Math.abs(dz) == 1) {
                                Location neighborLoc = locA.clone().add(dx, dy, dz);
                                // If neighbor is in the locations set, add it
                                if (locations.contains(neighborLoc)) {
                                    neighbors.add(neighborLoc);
                                }
                            }
                        }
                    }
                }

                // Update adjacency map
                adjacencyMap.put(locA, neighbors);
            }
            future.complete(null);
        });
        return future;


    }

    private boolean isAccessible(Material material){
        return !material.isSolid();
    }
}
