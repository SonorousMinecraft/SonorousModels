package com.sonorous.skeleton;

import java.util.List;

public class MeshDefinition {
    private final List<PartDefinition> partDefinitions;

    public MeshDefinition(List<PartDefinition> partDefinitions) {
        this.partDefinitions = partDefinitions;
    }

    public List<PartDefinition> getPartDefinitions() {
        return partDefinitions;
    }
}