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
        }
