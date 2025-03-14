package com.sonorous.skeleton;

import java.util.ArrayList;
import java.util.List;

public class CubeListBuilder {
        private final List<Cube> cubes;

        public CubeListBuilder() {
                this.cubes = new ArrayList<>();
        }

        public void addCube(Cube cube) {
                cubes.add(cube);
        }

        public List<Cube> getCubes() {
                return cubes;
        }
}