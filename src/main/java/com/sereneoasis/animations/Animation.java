package com.sereneoasis.animations;

import java.util.Map;

public class Animation {
    private boolean loop;
    private double animation_length;
    private Map<String, Bone> bones;

    // Getter for loop
    public boolean isLoop() {
        return loop;
    }

    // Setter for loop
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    // Getter for animation_length
    public double getAnimationLength() {
        return animation_length;
    }

    // Setter for animation_length
    public void setAnimationLength(double animation_length) {
        this.animation_length = animation_length;
    }

    // Getter for bones
    public Map<String, Bone> getBones() {
        return bones;
    }

    // Setter for bones
    public void setBones(Map<String, Bone> bones) {
        this.bones = bones;
    }
}