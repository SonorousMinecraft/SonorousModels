package com.sonorous.skeleton;

import java.util.List;

public class LayerDefinition {
    private final List<Cube> cubes; // Assuming LayerDefinition consists of multiple cubes

    public LayerDefinition(List<Cube> cubes) {
        this.cubes = cubes;
    }

    public List<Cube> getCubes() {
        return cubes;
    }
}