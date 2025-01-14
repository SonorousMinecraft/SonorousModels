package com.sereneoasis.skeleton;

import org.bukkit.util.Vector;

public class PartPose {
    private final float offsetX, offsetY, offsetZ;

    private float rotationX, rotationY, rotationZ;


    private PartPose(float offsetX, float offsetY, float offsetZ) {
        this.offsetX = offsetX/16;
        this.offsetY = -offsetY/16;
        this.offsetZ = -offsetZ/16;
    }

    private PartPose(float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ) {
        this.offsetX = offsetX/16;
        this.offsetY = -offsetY/16;
        this.offsetZ = -offsetZ/16;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
    }

    public static PartPose offset(float x, float y, float z) {
        return new PartPose(x, y, z);
    }

    public static PartPose offsetAndRotation(float x, float y, float z, float rx, float ry, float rz) {
        return new PartPose(x, y, z, rx, ry, rz);
    }

    public float getOffsetX() {
        return offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public float getOffsetZ() {
        return offsetZ;
    }

    public Vector getOffset() {
        return new Vector(offsetX, offsetY, offsetZ);
    }

    // Additional pose-related methods could be added here
}
