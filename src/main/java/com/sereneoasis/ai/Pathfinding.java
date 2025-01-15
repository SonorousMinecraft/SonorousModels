package com.sereneoasis.ai;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Pathfinding {

    private final ConcurrentHashMap<Location, Set<Location>> adjacencyMap;

    public Pathfinding(ConcurrentHashMap<Location, Set<Location>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }

    private double heuristic(Location from, Location to) {
        return from.distanceSquared(to);
    }

    private List<Location> reconstructPath(Node node) {
        List<Location> totalPath = new ArrayList<>();

        while (node != null) {
            totalPath.add(node.getLocation());
            node = node.getCameFrom();
        }
        Collections.reverse(totalPath); // Reverse the path to get it from start to goal
        return totalPath;
    }


    public List<Location> generatePath(Location startLoc, Location goalLoc) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
        Set<Location> closedSet = new HashSet<>();

        Node startNode = new Node(startLoc.getBlock().getLocation(), null, 0, heuristic(startLoc, goalLoc));
        openSet.add(startNode);

        Node lastValidNode = null;

        Bukkit.broadcastMessage("size of map is " + adjacencyMap.size());

        if (!adjacencyMap.containsKey(startLoc.getBlock().getLocation())){
            System.out.println("FUCK");
        }

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();

            lastValidNode = currentNode;

            if (currentNode.getLocation().distanceSquared(goalLoc) <= 1) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode.getLocation());

            for (Location neighbor : adjacencyMap.getOrDefault(currentNode.getLocation(), Collections.emptySet())) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = currentNode.getCost() + 1;

                Node neighborNode = new Node(neighbor, currentNode, tentativeGScore, heuristic(neighbor, goalLoc));

                Node existingNode = null;
                for (Node n : openSet) {
                    if (n.getLocation().equals(neighbor)) {
                        existingNode = n;
                        break;
                    }
                }

                if (existingNode != null) {
                    if (tentativeGScore >= existingNode.getCost()) {
                        continue;
                    }
                    openSet.remove(existingNode);
                }

                openSet.add(neighborNode);
            }
        }

        if (lastValidNode != null) {
            return reconstructPath(lastValidNode);
        }

        return new ArrayList<>();
    }
}
