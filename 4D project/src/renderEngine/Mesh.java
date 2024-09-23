package renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int numVertices;
    private float[] positions;
    private float[] colours;
    private int vaoId;
    private List<Integer> vboIdList;

    public Mesh(float[] positions, float[] colours, int[] indices) {
        this.positions = positions;
        this.colours = colours;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            this.numVertices = indices.length;
            vboIdList = new ArrayList<>();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            int positionsVboId = glGenBuffers();
            vboIdList.add(positionsVboId);
            FloatBuffer positionsBuffer = stack.callocFloat(positions.length);
            positionsBuffer.put(0, positions);
            glBindBuffer(GL_ARRAY_BUFFER, positionsVboId);
            glBufferData(GL_ARRAY_BUFFER, positionsBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            int coloursVboId = glGenBuffers();
            vboIdList.add(coloursVboId);
            FloatBuffer coloursBuffer = stack.callocFloat(colours.length);
            coloursBuffer.put(0, colours);
            glBindBuffer(GL_ARRAY_BUFFER, coloursVboId);
            glBufferData(GL_ARRAY_BUFFER, coloursBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            int indicesVboId = glGenBuffers();
            vboIdList.add(coloursVboId);
            IntBuffer indicesBuffer = stack.callocInt(indices.length);
            indicesBuffer.put(0, indices);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }
    }

    public void transformVertices(Matrix4f modelMatrix) {
        for (int i = 0; i < positions.length/3; i++) {
            Vector4f vert = new Vector4f(positions[i*3], positions[i*3+1], positions[i*3+2], 1);
            Vector4f vertImage = vert.mul(modelMatrix);
            positions[i*3] = vertImage.get(0);
            positions[i*3+1] = vertImage.get(1);
            positions[i*3+2] = vertImage.get(2);
        }
    }


    public void cleanup() {
        vboIdList.forEach(GL30::glDeleteBuffers);
        glDeleteVertexArrays(vaoId);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getVaoId() {
        return vaoId;
    }

    public Mesh slice2D(float planeZ) {
        ArrayList<Float> positionsList = new ArrayList<>();
        ArrayList<Float> coloursList = new ArrayList<>();
        for (int i = 0; i < positions.length; i+=3) {
            Vector3f vertexA = new Vector3f(positions[i], positions[i+1], positions[i+2]);
            Vector3f colourA = new Vector3f(colours[i], colours[i+1], colours[i+2]);
            Vector3f vertexB;
            Vector3f colourB;
            if (i%9 != 6) {
                vertexB = new Vector3f(positions[i+3], positions[i+4], positions[i+5]);
                colourB = new Vector3f(colours[i+3], colours[i+4], colours[i+5]);
            } else {
                vertexB = new Vector3f(positions[i-6], positions[i-5], positions[i-4]);
                colourB = new Vector3f(colours[i-6], colours[i-5], colours[i-4]);
            }
            if (Math.min(Math.min(vertexA.z(), vertexB.z()), planeZ) == planeZ ||
            Math.max(Math.max(vertexA.z(), vertexB.z()), planeZ) == planeZ) {
                continue;
            }
            float t = (planeZ-vertexA.z())/(vertexB.z()-vertexA.z());
            positionsList.add((1-t) * vertexA.x() + t * vertexB.x());
            positionsList.add((1-t) * vertexA.y() + t * vertexB.y());
            positionsList.add(planeZ);
            coloursList.add((1-t) * colourA.x() + t * colourB.x());
            coloursList.add((1-t) * colourA.y() + t * colourB.y());
            coloursList.add((1-t) * colourA.z() + t * colourB.z());
        }

        float[] positions = new float[positionsList.size()];
        float[] colours = new float[coloursList.size()];
        for (int i = 0; i < positionsList.size(); i++) {
            positions[i] = positionsList.get(i);
            colours[i] = coloursList.get(i);
        }

        float[] centre = new float[2];
        float totalX = 0;
        float totalY = 0;
        for (int i = 0; i < positions.length; i+=3) {
            totalX += positions[i];
            totalY += positions[i+1];
        }
        centre[0] = totalX / (positions.length/3.0f);
        centre[1] = totalY / (positions.length/3.0f);

        List<Double> angles = new ArrayList<>();

        for (int i = 0; i < positions.length; i+=3) {
            angles.add(Math.atan2(positions[i+1]-centre[1], positions[i]-centre[0]));
        }

        int[] indices = new int[positions.length/2];

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

        return new Mesh(positions, colours, indices);
    }
}
