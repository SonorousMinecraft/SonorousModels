package com.sereneoasis.ai;

import org.bukkit.Location;

public class Node {

    private double cost;

    private double heuristic;

    private Location location;

    private Node cameFrom;

    public Node(Location location, Node cameFrom, double cost, double heuristic){
        this.location = location;
        this.cameFrom = cameFrom;
        this.cost = cost;
        this.heuristic = heuristic;
    }

    public double getF(){
        return cost + heuristic;
    }

    public Location getLocation() {
        return location;
    }

    public Node getCameFrom() {
        return cameFrom;
    }

    public double getCost() {
        return cost;
    }

    public double getHeuristic() {
        return heuristic;
    }
}
