package com.sereneoasis.skeleton;

import java.util.ArrayList;
import java.util.List;

public class PartDefinition {
    private final String name;
    private final List<PartDefinition> children; // Children parts
    private final List<Cube> cubes; // List of cubes associated with this part

    public PartDefinition(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.cubes = new ArrayList<>();
    }

    public PartDefinition addOrReplaceChild(String childName, CubeListBuilder cubeListBuilder, PartPose pose) {
        // Create a new child part and add it
        PartDefinition child = new PartDefinition(childName);
        child.cubes.addAll(cubeListBuilder.buildCubes());
        this.children.add(child);
        return child;
    }

    // Additional methods for part management could be added here

    public List<PartDefinition> getChildren(){
        return children;
    }

    public List<Cube> getCubes() {
        return cubes;
    }
}
