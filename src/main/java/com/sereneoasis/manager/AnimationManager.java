package com.sereneoasis.manager;

import com.sereneoasis.animations.Animation;
import com.sereneoasis.animations.AnimationFile;
import com.sereneoasis.animations.AnimationLoader;

public class AnimationManager {

    public AnimationManager(){
        AnimationFile animation = AnimationLoader.loadAnimation("flap.json");
        if (animation != null) {

            // Access the animations and bones
            // For example, looping through bones:
            Animation flapAnimation = animation.getAnimations().get("flap");
            flapAnimation.getBones().forEach((name, bone) -> {
                System.out.println("Bone: " + name);
                System.out.println("Rotations: " + bone.getRotation());
                System.out.println("Positions: " + bone.getPosition());
            });
        }
    }
}
