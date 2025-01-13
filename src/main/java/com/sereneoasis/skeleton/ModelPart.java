package com.sereneoasis.skeleton;

import java.util.HashMap;
import java.util.Map;

public class ModelPart {

    private final Map<String, ModelPart> children = new HashMap<>();

    // Constructor to initialize ModelPart with potential children
    public ModelPart() {
        // Initialize children if needed (optional)
    }

    // Add a child part
    public void addChild(String name, ModelPart child) {
        children.put(name, child);
    }

    // Method to get a child part by name
    public ModelPart getChild(String name) {
        return children.get(name);
    }

    // Optional: Method to get all children (for debugging)
    public Map<String, ModelPart> getChildren() {
        return new HashMap<>(children);
    }
}
