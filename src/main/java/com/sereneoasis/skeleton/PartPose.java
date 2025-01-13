package com.sereneoasis.skeleton;

public class PartPose {
    private final float offsetX, offsetY, offsetZ;

    private PartPose(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public static PartPose offset(float x, float y, float z) {
        return new PartPose(x, y, z);
    }

    // Additional pose-related methods could be added here
}
