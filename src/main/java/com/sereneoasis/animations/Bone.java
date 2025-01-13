package com.sereneoasis.animations;

import java.util.Map;

import java.util.Map;

public class Bone {
    private Map<String, double[]> rotation;
    private Map<String, double[]> position;

    // Getter for rotation
    public Map<String, double[]> getRotation() {
        return rotation;
    }

    // Setter for rotation
    public void setRotation(Map<String, double[]> rotation) {
        this.rotation = rotation;
    }

    // Getter for position
    public Map<String, double[]> getPosition() {
        return position;
    }

    // Setter for position
    public void setPosition(Map<String, double[]> position) {
        this.position = position;
    }
}