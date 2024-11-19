package renderEngine.entity;

import org.joml.Vector2f;
import org.joml.Vector4f;
import renderEngine.entity.Mesh;

import java.util.ArrayList;
import java.util.Random;

import static renderEngine.Meshes.MeshData.Tetrahedra.edges;

public class Mesh4D {
    private int numVertices;
    private float[] positions;
    private float[] transPos;
    private float[] colours;
    private int[][][] indices;

    public Mesh4D (float[] positions, float[] colours, int[][][] indices) {
        this.positions = positions;
        this.transPos = new float[positions.length];
        this.colours = colours;
        this.indices = indices;
        this.numVertices = positions.length/4;
    }

    public void transformVertices(Vector4f position, float[] rotation) {
        for (int i = 0; i < positions.length/4; i++) {
            Vector4f vert = new Vector4f(positions[i*4], positions[i*4+1], positions[i*4+2], positions[i*4+3]);
            Vector4f vertCopy = new Vector4f(vert.x(), vert.y(), vert.z(), vert.w());
            double cosXY = Math.cos(rotation[0]);
            double sinXY = Math.sin(rotation[0]);
            vert.x = (float) (cosXY * vertCopy.x - sinXY * vertCopy.y);
            vert.y = (float) (sinXY * vertCopy.x + cosXY * vertCopy.y);
            vertCopy = new Vector4f(vert.x(), vert.y(), vert.z(), vert.w());
            double cosXZ = Math.cos(rotation[1]);
            double sinXZ = Math.sin(rotation[1]);
            vert.z = (float) (cosXZ * vertCopy.z - sinXZ * vertCopy.x);
            vert.x = (float) (sinXZ * vertCopy.z + cosXZ * vertCopy.x);
            vertCopy = new Vector4f(vert.x(), vert.y(), vert.z(), vert.w());
            double cosXW = Math.cos(rotation[2]);
            double sinXW = Math.sin(rotation[2]);
            vert.x = (float) (cosXW * vertCopy.x - sinXW * vertCopy.w);
            vert.w = (float) (sinXW * vertCopy.x + cosXW * vertCopy.w);
            vertCopy = new Vector4f(vert.x(), vert.y(), vert.z(), vert.w());
            double cosYZ = Math.cos(rotation[3]);
            double sinYZ = Math.sin(rotation[3]);
            vert.y = (float) (cosYZ * vertCopy.y - sinYZ * vertCopy.z);
            vert.z = (float) (sinYZ * vertCopy.y + cosYZ * vertCopy.z);
            vertCopy = new Vector4f(vert.x(), vert.y(), vert.z(), vert.w());
            double cosYW = Math.cos(rotation[4]);
            double sinYW = Math.sin(rotation[4]);
            vert.w = (float) (cosYW * vertCopy.w - sinYW * vertCopy.y);
            vert.y = (float) (sinYW * vertCopy.w + cosYW * vertCopy.y);
            vertCopy = new Vector4f(vert.x(), vert.y(), vert.z(), vert.w());
            double cosZW = Math.cos(rotation[5]);
            double sinZW = Math.sin(rotation[5]);
            vert.w = (float) (cosZW * vertCopy.w - sinZW * vertCopy.z);
            vert.z = (float) (sinZW * vertCopy.w + cosZW * vertCopy.z);
            transPos[i*4] = vert.x()+position.x();
            transPos[i*4+1] = vert.y()+position.y();
            transPos[i*4+2] = vert.z()+position.z();
            transPos[i*4+3] = vert.w()+position.w();
        }
    }

    public Mesh slice3D(float planeW) {
        Random rand = new Random(1);
        ArrayList<Float> positionsList = new ArrayList<>();
        ArrayList<Float> coloursList = new ArrayList<>();
        ArrayList<Integer> indicesList = new ArrayList<>();
        int indicesCounter = 0;
        for (int[][] index : indices) {
            for (int[] ints : index) {
                int intersectionCounter = 0;
                for (int[] edge : edges) {
                    Vector4f vA = new Vector4f(transPos[ints[edge[0]] * 4],
                            transPos[ints[edge[0]] * 4 + 1],
                            transPos[ints[edge[0]] * 4 + 2],
                            transPos[ints[edge[0]] * 4 + 3]);
                    Vector4f cA = new Vector4f(colours[ints[edge[0]] * 4],
                            colours[ints[edge[0]] * 4 + 1],
                            colours[ints[edge[0]] * 4 + 2],
                            colours[ints[edge[0]] * 4 + 3]);
                    Vector4f vB = new Vector4f(transPos[ints[edge[1]] * 4],
                            transPos[ints[edge[1]] * 4 + 1],
                            transPos[ints[edge[1]] * 4 + 2],
                            transPos[ints[edge[1]] * 4 + 3]);
                    Vector4f cB = new Vector4f(colours[ints[edge[1]] * 4],
                            colours[ints[edge[1]] * 4 + 1],
                            colours[ints[edge[1]] * 4 + 2],
                            colours[ints[edge[1]] * 4 + 3]);
                    if (Math.min(Math.min(vA.w(), vB.w()), planeW) == planeW ||
                            Math.max(Math.max(vA.w(), vB.w()), planeW) == planeW) {
                        continue;
                    }
                    float t = (planeW - vA.w()) / (vB.w() - vA.w());
                    positionsList.add((1 - t) * vA.x() + t * vB.x());
                    positionsList.add((1 - t) * vA.y() + t * vB.y());
                    positionsList.add((1 - t) * vA.z() + t * vB.z());
                    coloursList.add((1 - t) * cA.x() + t * cB.x());
                    coloursList.add((1 - t) * cA.y() + t * cB.y());
                    coloursList.add((1 - t) * cA.z() + t * cB.z());
                    intersectionCounter++;
                }
                if (intersectionCounter == 3) {
                    indicesList.add(indicesCounter);
                    indicesList.add(indicesCounter + 1);
                    indicesList.add(indicesCounter + 2);

                    indicesCounter += 3;
                } else if (intersectionCounter == 4) {
                    indicesList.add(indicesCounter);
                    indicesList.add(indicesCounter + 1);
                    indicesList.add(indicesCounter + 2);
                    indicesList.add(indicesCounter + 2);
                    indicesList.add(indicesCounter + 3);
                    indicesList.add(indicesCounter + 1);
                    indicesList.add(indicesCounter + 2);
                    indicesList.add(indicesCounter + 3);
                    indicesList.add(indicesCounter);

                    indicesCounter += 4;
                }
            }

        }
        float[] positions = new float[positionsList.size()];
        float[] colours = new float[coloursList.size()];
        for (int i = 0; i < positionsList.size(); i++) {
            positions[i] = positionsList.get(i);
            colours[i] = coloursList.get(i);
        }
        int[] indices = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            indices[i] = indicesList.get(i);
        }
        return new Mesh(positions, colours, indices, null);
    }

}
