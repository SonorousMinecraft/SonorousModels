package com.sereneoasis.skeleton;

// Custom classes simulating the functionality in your original model
public class LayerDefinition {
    private final MeshDefinition meshDefinition;
    private final int width;
    private final int height;

    public LayerDefinition(MeshDefinition meshDefinition, int width, int height) {
        this.meshDefinition = meshDefinition;
        this.width = width;
        this.height = height;
    }

    public static LayerDefinition create(MeshDefinition meshDefinition, int width, int height) {
        return new LayerDefinition(meshDefinition, width, height);
    }

    public MeshDefinition getMeshDefinition() {
        return meshDefinition;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

