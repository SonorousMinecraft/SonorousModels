package com.sereneoasis.manager;

import com.sereneoasis.TempDisplayBlock;
import com.sereneoasis.animations.Animation;
import com.sereneoasis.animations.AnimationFile;
import com.sereneoasis.animations.AnimationLoader;
import org.bukkit.Location;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

public class AnimationManager {

    public static Animation flap;

    private double time = 0;

    private ModelInstance modelInstance;

    private Vector oldPositionChange = new Vector();

    public AnimationManager(ModelInstance instance){
        this.modelInstance = instance;
        AnimationFile animation = AnimationLoader.loadAnimation("flap.json");
        if (animation != null) {

            // Access the animations and bones
            // For example, looping through bones:
            flap = animation.getAnimations().get("flap");


        }
    }

    public void tick(){
        time+=0.25;
        if (time > 1){
            time = 0;
        }

//        flap.getBones().forEach((name, bone) -> {
//            TempDisplayBlock tdb = modelInstance.getDisplayBlockFromBone(name);
//            if (bone.getRotation() != null){
//                double[] rotation = bone.getRotation().get(String.valueOf(time));
//                if (rotation != null){
//                    double pitch = rotation[0];
//
//                    double yaw = rotation[1];
//                    double roll = rotation[2];
//
////                    Vector forward = tdb.getLoc().getDirection();
//////                    forward.rotateAroundX(Math.toRadians(pitch));
//////                    forward.rotateAroundY(Math.toRadians(yaw));
////                    forward.rotateAroundZ(Math.toRadians(roll));
////                    Location tempLoc = tdb.getLoc().setDirection(forward);
////                    tdb.setRotation((float) Math.toDegrees(tempLoc.getYaw()), (float)  Math.toDegrees(tempLoc.getPitch()));
//
//                    Transformation transformation = tdb.getBlockDisplay().getTransformation();
//                }
//            }
//            if (bone.getPosition() != null){
//                double[] position = bone.getPosition().get(String.valueOf(time));
//                if (position.length != 0){
//                    Vector newPos = new Vector(position[0]/16, position[1]/16, position[2]/16);
//                    Vector change = newPos.clone().subtract(oldPositionChange);
//                    this.oldPositionChange = change;
//                    modelInstance.putCube(tdb, modelInstance.getCubeOffset(tdb).clone().add(change) );
//                }
//            }
            
//            System.out.println("Rotations: " + bone.getRotation());
//            System.out.println("Positions: " + bone.getPosition());
//        });
    }
}
