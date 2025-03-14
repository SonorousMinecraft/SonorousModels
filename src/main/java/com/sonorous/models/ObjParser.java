package com.sonorous.models;

import com.sonorous.skeleton.Cube;
import com.sonorous.skeleton.MeshDefinition;
import com.sonorous.skeleton.PartDefinition;
import com.sonorous.skeleton.PartPose;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {

    public static MeshDefinition loadMeshDefinition(String filename) throws IOException {
        List<PartDefinition> partDefinitions = new ArrayList<>();
        List<float[]> vertices = new ArrayList<>();
        List<int[]> faces = new ArrayList<>();

        readObjFile(filename, vertices, faces);
        List<Cube> cubes = createCubesFromFaces(vertices, faces);
        partDefinitions.add(new PartDefinition(cubes, new PartPose(0, 0, 0))); // Adjust pose as needed
        return new MeshDefinition(partDefinitions);
    }

    private static void readObjFile(String filename, List<float[]> vertices, List<int[]> faces) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");

                switch (tokens[0]) {
                    case "v":
                        vertices.add(parseVertex(tokens));
                        break;
                    case "f":
                        faces.add(parseFace(tokens));
                        break;
                    // Skip other lines like normals/texture if not needed
                    default:
                        break; // Ignore other unhandled cases
                }
            }
        }
    }

    private static float[] parseVertex(String[] tokens) {
        return new float[] {
                Float.parseFloat(tokens[1]), // X axis (left-right)
                Float.parseFloat(tokens[2]), // Y axis (up-down)
                Float.parseFloat(tokens[3])  // Z axis (forward-back)
        };
    }

    private static int[] parseFace(String[] tokens) {
        int[] face = new int[tokens.length - 1];
        for (int i = 1; i < tokens.length; i++) {
            String vertexIndex = tokens[i].split("/")[0]; // Extract vertex index
            face[i - 1] = Integer.parseInt(vertexIndex) - 1; // Convert to zero-based index
        }
        return face;
    }

    private static List<Cube> createCubesFromFaces(List<float[]> vertices, List<int[]> faces) {
        List<Cube> cubes = new ArrayList<>();

        for (int[] face : faces) {
            if (face.length >= 3) { // Need at least 3 vertices to form a polygon
                float[] p1 = vertices.get(face[0]);
                float[] p2 = vertices.get(face[1]);
                float[] p3 = vertices.get(face[2]);

                // Calculate cube properties based on vertices
                Cube cube = createCubeFromVertices(p1, p2, p3);
                if (cube != null) {
                    cubes.add(cube);
                }
            }
        }
        return cubes;
    }

    private static Cube createCubeFromVertices(float[] p1, float[] p2, float[] p3) {
        // Determine the min and max values to find the bounding box
        float minX = Math.min(p1[0], Math.min(p2[0], p3[0]));
        float maxX = Math.max(p1[0], Math.max(p2[0], p3[0]));
        float minY = Math.min(p1[1], Math.min(p2[1], p3[1]));
        float maxY = Math.max(p1[1], Math.max(p2[1], p3[1]));
        float minZ = Math.min(p1[2], Math.min(p2[2], p3[2]));
        float maxZ = Math.max(p1[2], Math.max(p2[2], p3[2]));

        // Calculate center and size of the cube
        float centerX = (minX + maxX) / 2.0f;
        float centerY = (minY + maxY) / 2.0f;
        float centerZ = (minZ + maxZ) / 2.0f;

        float width = maxX - minX;
        float height = maxY - minY;
        float depth = maxZ - minZ;

        // Create a cube with the calculated dimensions and position
        return new Cube(centerX, centerY, centerZ, width, height, depth);
    }
}