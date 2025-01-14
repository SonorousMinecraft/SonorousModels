package com.sereneoasis.animations;

import java.util.*;

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


    // Method to get interpolated position at a specific time
    public double[] getPositionByTime(String time) {
        double t = Double.parseDouble(time);
        List<Double> keys = new ArrayList<>();
        for (String key : position.keySet()) {
            keys.add(Double.parseDouble(key));
        }
        Collections.sort(keys);

        // Find indices of the nearest times to the requested time
        int nearestTimeIndex = Collections.binarySearch(keys, t);
        if (nearestTimeIndex < 0) {
            nearestTimeIndex = -nearestTimeIndex - 1;
        }

        double[] interpolatedPosition;

        if (nearestTimeIndex <= 0) { // Before the first point
            interpolatedPosition = position.get(keys.get(0).toString());
        } else if (nearestTimeIndex >= keys.size()) { // After the last point
            interpolatedPosition = position.get(keys.get(keys.size() - 1).toString());
        } else { // Between two points
            double t1 = keys.get(nearestTimeIndex - 1);
            double t2 = keys.get(nearestTimeIndex);
            double[] v1Position = position.get(Double.toString(t1));
            double[] v2Position = position.get(Double.toString(t2));
            double alpha = (t - t1) / (t2 - t1);

            interpolatedPosition = new double[v1Position.length];
            for (int i = 0; i < interpolatedPosition.length; i++) {
                interpolatedPosition[i] = v1Position[i] + alpha * (v2Position[i] - v1Position[i]);
            }
        }
        return interpolatedPosition;
    }

    // Method to get interpolated rotation at a specific time
    public double[] getRotationByTime(String time) {
        double t = Double.parseDouble(time);
        List<Double> keys = new ArrayList<>();
        for (String key : rotation.keySet()) {
            keys.add(Double.parseDouble(key));
        }
        Collections.sort(keys);

        // Find indices of the nearest times to the requested time
        int nearestTimeIndex = Collections.binarySearch(keys, t);
        if (nearestTimeIndex < 0) {
            nearestTimeIndex = -nearestTimeIndex - 1;
        }

        double[] interpolatedRotation;

        if (nearestTimeIndex <= 0) { // Before the first point
            interpolatedRotation = rotation.get(keys.get(0).toString());
        } else if (nearestTimeIndex >= keys.size()) { // After the last point
            interpolatedRotation = rotation.get(keys.get(keys.size() - 1).toString());
        } else { // Between two points
            double t1 = keys.get(nearestTimeIndex - 1);
            double t2 = keys.get(nearestTimeIndex);
            double[] v1Rotation = rotation.get(Double.toString(t1));
            double[] v2Rotation = rotation.get(Double.toString(t2));
            double alpha = (t - t1) / (t2 - t1);

            interpolatedRotation = new double[v1Rotation.length];
            for (int i = 0; i < interpolatedRotation.length; i++) {
                interpolatedRotation[i] = v1Rotation[i] + alpha * (v2Rotation[i] - v1Rotation[i]);
            }
        }
        return interpolatedRotation;
    }

    public boolean hasRotation(){
        return rotation != null;
    }

    public boolean hasPosition(){
        return position != null;
    }
}