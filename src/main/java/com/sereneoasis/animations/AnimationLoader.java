package com.sereneoasis.animations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnimationLoader {

    public static AnimationFile loadAnimation(String filePath) {
        Gson gson = new GsonBuilder().create();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return gson.fromJson(br, AnimationFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
