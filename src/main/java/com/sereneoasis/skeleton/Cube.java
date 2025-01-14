package com.sereneoasis.skeleton;

public class Cube {
    private final float x, y, z;
    private final float width, height, depth;
    private final CubeDeformation deformation;

    public Cube(float x, float y, float z, float width, float height, float depth, CubeDeformation deformation) {
        this.x = -(x + width / 2) / 16;
        this.y = -(y + height / 2) / 16;
        this.z = z / 16;
        this.width = width / 16;
        this.height = height / 16;
        this.depth = depth / 16;
        this.deformation = deformation;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getDepth() {
        return depth;
    }

    public CubeDeformation getDeformation() {
        return deformation;
    }
}
