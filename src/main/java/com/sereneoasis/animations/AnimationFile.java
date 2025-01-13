package com.sereneoasis.animations;

import java.util.Map;

public class AnimationFile {
    private String format_version;
    private Map<String, Animation> animations;

    // Getter for format_version
    public String getFormatVersion() {
        return format_version;
    }

    // Setter for format_version
    public void setFormatVersion(String format_version) {
        this.format_version = format_version;
    }

    // Getter for animations
    public Map<String, Animation> getAnimations() {
        return animations;
    }

    // Setter for animations
    public void setAnimations(Map<String, Animation> animations) {
        this.animations = animations;
    }
}

