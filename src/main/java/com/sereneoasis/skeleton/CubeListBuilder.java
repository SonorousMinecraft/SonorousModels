package com.sereneoasis.skeleton;

import java.util.ArrayList;
import java.util.List;

public class CubeListBuilder {

    private final List<Cube> cubes;

    public CubeListBuilder() {
        this.cubes = new ArrayList<>();
    }

    public static CubeListBuilder create() {
        return new CubeListBuilder();
    }

    public CubeListBuilder texOffs(int x, int y) {
        // Set texture offset if needed
        // For simplicity, using a comment here that isn't actually going to change cube creation
        return this;
    }

    public CubeListBuilder addBox(float x, float y, float z, float width, float height, float depth, CubeDeformation deformation) {
        cubes.add(new Cube(x, y, z, width, height, depth, deformation));
        return this;
    }

    public List<Cube> buildCubes() {
        // Returns the list of cubes to be added to a part
        return this.cubes;
    }


    public CubeListBuilder mirror() {
        List<Cube> mirroredCubes = new ArrayList<>();

        // Iterate through the existing cubes and create mirrored versions
        for (Cube cube : cubes) {
            float mirroredX = -cube.getX(); // Assuming you're mirroring on the X-axis
            // Create a new Cube with mirrored position
            mirroredCubes.add(new Cube(mirroredX, cube.getY(), cube.getZ(), cube.getWidth(), cube.getHeight(), cube.getDepth(), cube.getDeformation()));
        }

        // Add the mirrored cubes to the original list
        this.cubes.addAll(mirroredCubes);
        return this; // Return the builder for chaining
    }

    public CubeListBuilder mirror(boolean shouldMirror) {
        if (!shouldMirror) {
            return this; // No mirroring is done; just return the builder
        }

        List<Cube> mirroredCubes = new ArrayList<>();

        // Iterate through the existing cubes and create mirrored versions
        for (Cube cube : cubes) {
            float mirroredX = -cube.getX(); // Assuming you're mirroring on the X-axis
            // Create a new Cube with mirrored position
            mirroredCubes.add(new Cube(mirroredX, cube.getY(), cube.getZ(), cube.getWidth(), cube.getHeight(), cube.getDepth(), cube.getDeformation()));
        }

        // Add the mirrored cubes to the original list
        this.cubes.addAll(mirroredCubes);
        return this; // Return the builder for chaining
    }

}

