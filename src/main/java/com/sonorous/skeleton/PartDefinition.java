package com.sonorous.skeleton;


import java.util.List;

public class PartDefinition {
    private List<Cube> cubes;
    private PartPose pose;

    public PartDefinition(List<Cube> cubes, PartPose pose) {
        this.cubes = cubes;
        this.pose = pose;
    }

    public List<Cube> getCubes() {
        return cubes;
    }

    public PartPose getPose() {
        return pose;
    }
}