package com.sereneoasis.manager;

import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.animations.Animation;
import com.sereneoasis.animations.AnimationFile;
import com.sereneoasis.animations.AnimationLoader;
import org.bukkit.Particle;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.HashMap;

public class AnimationManager {

    public static Animation flap;
    private final HashMap<TempDisplayBlock, Vector3f> tempDisplayBlockTranslationHashMap = new HashMap<>();
    private double time = 0;
    private final ModelInstance modelInstance;


    public AnimationManager(ModelInstance instance) {
        this.modelInstance = instance;
        AnimationFile animation = AnimationLoader.loadAnimation("flap.json");
        if (animation != null) {

            // Access the animations and bones
            // For example, looping through bones:
            flap = animation.getAnimations().get("flap");
        }
    }

    public void tick() {
        time += 0.05;
        if (time >= 1) {
            time = 0;
        }

        flap.getBones().forEach((name, bone) -> {
            modelInstance.getDisplayBlocksFromBone(name).forEach(tdb -> {
                tempDisplayBlockTranslationHashMap.putIfAbsent(tdb, tdb.getBlockDisplay().getTransformation().getTranslation());
                if (bone.hasRotation()) {
                    double[] rotation = bone.getRotationByTime(String.valueOf(time));


                    double pitch = rotation[0];
                    double yaw = rotation[1];
                    double roll = rotation[2];

                    if (name.contains("right")) {

                        Transformation transformation = tdb.getBlockDisplay().getTransformation();

//                        tdb.getLoc().getWorld().spawnParticle(Particle.CRIT, tdb.getLoc().add(Vector.fromJOML(transformation.getTranslation())), 1);
                        Quaternionf leftRotation = new Quaternionf();
                        if (yaw != 0 ) {
                            leftRotation = leftRotation.rotateY((float) Math.toRadians(yaw)); // yaw
                        }
                        if (pitch != 0) {
                            leftRotation = leftRotation.rotateX((float) Math.toRadians(pitch));
                        }
                        if (roll != 0 ) {
                            leftRotation = leftRotation.rotateZ((float) Math.toRadians(180 + roll));
                        }
                        transformation = new Transformation(transformation.getTranslation(), leftRotation, transformation.getScale(),transformation.getRightRotation() );
                        tdb.getBlockDisplay().setTransformation(transformation);

                    } else {

                        Transformation transformation = tdb.getBlockDisplay().getTransformation();
//                        tdb.getLoc().getWorld().spawnParticle(Particle.CRIT, tdb.getLoc().add(Vector.fromJOML(transformation.getTranslation())), 1);

                        Quaternionf leftRotation = new Quaternionf();
                        leftRotation = leftRotation.rotateY((float) Math.toRadians(yaw));
                        leftRotation = leftRotation.rotateX((float) Math.toRadians(pitch));

                        leftRotation = leftRotation.rotateZ((float) Math.toRadians(roll));
                        transformation = new Transformation(transformation.getTranslation(), leftRotation, transformation.getScale(), transformation.getRightRotation());
                        tdb.getBlockDisplay().setTransformation(transformation);
                    }
                }
                if (bone.hasPosition()) {
                    double[] position = bone.getPositionByTime(String.valueOf(time));
                    Transformation transformation = tdb.getBlockDisplay().getTransformation();
                    Vector3f translation = new Vector3f(tempDisplayBlockTranslationHashMap.get(tdb)).add(new Vector3f((float) (-position[0] / 16), (float) (-position[1] / 16), (float) (position[2] / 16)));
                    transformation = new Transformation(translation, transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation());
                    tdb.getBlockDisplay().setTransformation(transformation);
                }
            });
        });
    }


}
