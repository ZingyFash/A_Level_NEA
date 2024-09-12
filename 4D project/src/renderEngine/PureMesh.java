package renderEngine;

import java.util.ArrayList;
import java.util.List;

public class PureMesh {

    private float[] positions;
    private float[] colours;
    private int[] indices;

    public PureMesh(float[] positions, float[] colours, int[] indices) {
        this.positions = positions;
        this.colours = colours;
        this.indices = indices;
    }

    public Mesh convertToMesh(float planeZ) {
        float[][] vertices = findVertices(planeZ);

        int[] indices = findIndices(vertices[0]);

        return new Mesh(vertices[0], vertices[1], indices);
    }

    private int[] findIndices(float[] vertices) {
        float[] centre = new float[2];
        float totalX = 0;
        float totalY = 0;
        for (int i = 0; i < vertices.length / 2; i++) {
            totalX += vertices[i];
            totalY += vertices[i+1];
        }
        centre[0] = totalX / (vertices.length/2.0f);
        centre[1] = totalY / (vertices.length/2.0f);

        List<Double> angles = new ArrayList<>();

        for (int i = 0; i < vertices.length / 2; i++) {
            angles.add(Math.atan2(vertices[i*2+1]-centre[1], vertices[i*2]-centre[0]));
        }

        int[] indices = new int[vertices.length/2];

        for (int i = 0; i < angles.size(); i++) {
            int index = -1;
            double minAngle = 7;
            for (int j = 0; j < angles.size(); j++) {
                if (angles.get(j) < minAngle) {
                    index = j;
                    minAngle = angles.get(j);
                }
            }
            angles.remove(index);
            angles.add(index, 10.0);
            indices[i] = index;
        }

        return indices;
    }

    private float[][] findVertices(float planeZ) {
        List<Float> vertices = new ArrayList<>();
        List<Float> newColours = new ArrayList<>();
        for (int i = 0; i < indices.length; i++) {
            float[] vertexA = {positions[indices[i]], positions[indices[i] + 1], positions[indices[i] + 2]};
            float[] colourA = {colours[indices[i]], colours[indices[i] + 1], colours[indices[i] + 2]};
            float[] vertexB = {positions[indices[i+1]], positions[indices[i+1] + 1], positions[indices[i+1] + 2]};
            float[] colourB = {colours[indices[i+1]], colours[indices[i+1] + 1], colours[indices[i+1] + 2]};
            if (Math.min(Math.min(vertexA[2], vertexB[2]), planeZ) == planeZ ||
                    Math.max(Math.max(vertexA[2], vertexB[2]), planeZ) == planeZ) {
                continue;
            }
            if (vertexA[2] == vertexB[2]) {
                vertices.add(vertexA[0]);
                vertices.add(vertexA[1]);
                vertices.add(0.0f);
                newColours.add(colourA[0]);
                newColours.add(colourA[1]);
                newColours.add(colourA[2]);
                vertices.add(vertexB[0]);
                vertices.add(vertexB[1]);
                vertices.add(0.0f);
                newColours.add(colourB[0]);
                newColours.add(colourB[1]);
                newColours.add(colourB[2]);
                continue;
            }
            float t = (planeZ - vertexA[2])/(vertexB[2]-vertexA[2]);
            vertices.add((1-t)*vertexA[0] + t*vertexB[0]);
            vertices.add((1-t)*vertexA[1] + t*vertexB[1]);
            vertices.add(0.0f);
            newColours.add((1-t)*colourA[0] + t*colourB[0]);
            newColours.add((1-t)*colourA[1] + t*colourB[1]);
            newColours.add((1-t)*colourA[2] + t*colourB[2]);
        }
        float[][] r = new float[2][newColours.size()];
        for (int i = 0; i < newColours.size(); i++) {
            r[0][i] = vertices.get(i);
            r[1][i] = newColours.get(i);
        }
        return r;
    }
}
