package com.sonorous.skeleton;

public class CubeDeformation {
    private final float[] deformationValues; // Ensure this matches your JSON structure

    public CubeDeformation(float[] deformationValues) {
        this.deformationValues = deformationValues;
    }

    public float[] getDeformationValues() {
        return deformationValues;
    }
}