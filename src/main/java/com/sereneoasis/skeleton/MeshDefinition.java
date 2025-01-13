package com.sereneoasis.skeleton;

public class MeshDefinition {
    private final PartDefinition root;

    public MeshDefinition() {
        // Initialize the root part
        this.root = new PartDefinition("root");
    }

    public PartDefinition getRoot() {
        return root;
    }
}

