package renderEngine.entity;

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
    private float[] transformPositions;
    private float[] colours;
    private int[] indices;
    private boolean[][] adjacencyMatrix;
    private int vaoId;
    private List<Integer> vboIdList;

    public Mesh(float[] positions, float[] colours, int[] indices, boolean[][] adjacencyMatrix) {
        this.positions = positions;
        this.transformPositions = new float[positions.length];
        this.colours = colours;
        this.indices = indices;
        this.adjacencyMatrix = adjacencyMatrix;
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
            transformPositions[i*3] = vertImage.get(0);
            transformPositions[i*3+1] = vertImage.get(1);
            transformPositions[i*3+2] = vertImage.get(2);
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
        boolean[][] am = new boolean[adjacencyMatrix.length][adjacencyMatrix.length];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, am[i], 0, adjacencyMatrix.length);
        }
        for (int i = 0; i < indices.length; i++) {
            int index = (i % 3 == 2) ? i - 2 : i + 1;
            if (!adjacencyMatrix[indices[i]][indices[index]]) {
                continue;
            }
            adjacencyMatrix[indices[i]][indices[index]] = false;
            Vector3f vertexA = new Vector3f(transformPositions[indices[i]*3], transformPositions[indices[i]*3+1], transformPositions[indices[i]*3+2]);
            Vector3f colourA = new Vector3f(colours[indices[i]*3], colours[indices[i]*3+1], colours[indices[i]*3+2]);
            Vector3f vertexB = new Vector3f(transformPositions[indices[index]*3], transformPositions[indices[index]*3+1], transformPositions[indices[index]*3+2]);
            Vector3f colourB = new Vector3f(colours[indices[index]*3], colours[indices[index]*3+1], colours[indices[index]*3+2]);
            if (Math.min(Math.min(vertexA.z(), vertexB.z()), planeZ) == planeZ ||
            Math.max(Math.max(vertexA.z(), vertexB.z()), planeZ) == planeZ) {
                continue;
            }
            float t = (planeZ-vertexA.z())/(vertexB.z()-vertexA.z());
            positionsList.add((1-t) * vertexA.x() + t * vertexB.x());
            positionsList.add((1-t) * vertexA.y() + t * vertexB.y());
            positionsList.add(0.0f);
            coloursList.add((1-t) * colourA.x() + t * colourB.x());
            coloursList.add((1-t) * colourA.y() + t * colourB.y());
            coloursList.add((1-t) * colourA.z() + t * colourB.z());
        }
        adjacencyMatrix = am;

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
            double angle = Math.atan2(positions[i+1]-centre[1], positions[i]-centre[0]);
            angles.add((angle<0)?2*Math.PI+angle:angle);
        }

        int[] orderedPositions = new int[positions.length/3];

        for (int i = 0; i < angles.size(); i++) {
            int index = -1;
            double minAngle = 7;
            for (int j = 0; j < angles.size(); j++) {
                if (Math.abs(angles.get(j)) < Math.abs(minAngle)) {
                    index = j;
                    minAngle = angles.get(j);
                }
            }
            angles.remove(index);
            angles.add(index, 10.0);
            orderedPositions[i] = index;
        }

        ArrayList<Integer> indicesList = new ArrayList<>();
        for (int i = 1; i < orderedPositions.length-1; i++) {
            indicesList.add(orderedPositions[0]);
            indicesList.add(orderedPositions[i]);
            indicesList.add(orderedPositions[i+1]);
        }

        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }

        return new Mesh(positions, colours, indices, null);
    }
}
