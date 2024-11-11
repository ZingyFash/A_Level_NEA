package renderEngine.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Random;

public class Mesh4D {
    private int numVertices;
    private float[] positions;
    private float[] transformPositions;
    private float[] colours;
    private int[][] indices;

    public Mesh4D (float[] positions, float[] colours, int[][] indices) {
        this.positions = positions;
        this.transformPositions = new float[positions.length];
        this.colours = colours;
        this.indices = indices;
        this.numVertices = positions.length/4;
    }

    public void transformVertices(Vector4f position) {
        for (int i = 0; i < positions.length/4; i++) {
            Vector4f vert = new Vector4f(positions[i*4], positions[i*4+1], positions[i*4+2], positions[i*4+3]);
            transformPositions[i*4] = vert.x()+position.x();
            transformPositions[i*4+1] = vert.y()+position.y();
            transformPositions[i*4+2] = vert.z()+position.z();
            transformPositions[i*4+3] = vert.w()+position.w();
        }
    }

    public Mesh slice3D(float planeW) {
        ArrayList<Float> positionsList = new ArrayList<>();
        ArrayList<Float> coloursList = new ArrayList<>();
        for (int i = 0; i < indices.length; i++) {
            for (int j = 0; j < indices[i].length; j++) {
                Vector4f vertexA = new Vector4f(transformPositions[i*4], transformPositions[i*4+1], transformPositions[i*4+2], transformPositions[i*4+3]);
                Vector4f colourA = new Vector4f(colours[i*4], colours[i*4+1], colours[i*4+2], colours[i*4+3]);
                Vector4f vertexB = new Vector4f(transformPositions[indices[i][j]*4], transformPositions[indices[i][j]*4+1], transformPositions[indices[i][j]*4+2], transformPositions[indices[i][j]*4+3]);
                Vector4f colourB = new Vector4f(colours[indices[i][j]*4], colours[indices[i][j]*4+1], colours[indices[i][j]*4+2], colours[indices[i][j]*4+3]);
                if (Math.min(Math.min(vertexA.w(), vertexB.w()), planeW) == planeW ||
                        Math.max(Math.max(vertexA.w(), vertexB.w()), planeW) == planeW) {
                    continue;
                }
                float t = (planeW-vertexA.w())/(vertexB.w()-vertexA.w());
                positionsList.add((1-t) * vertexA.x() + t * vertexB.x());
                positionsList.add((1-t) * vertexA.y() + t * vertexB.y());
                positionsList.add((1-t) * vertexA.z() + t * vertexB.z());
                coloursList.add((1-t) * colourA.x() + t * colourB.x());
                coloursList.add((1-t) * colourA.y() + t * colourB.y());
                coloursList.add((1-t) * colourA.z() + t * colourB.z());
            }

        }
        float[] positions = new float[positionsList.size()];
        float[] colours = new float[coloursList.size()];
        Vector3f[] vertices = new Vector3f[positionsList.size()/3];
        for (int i = 0; i < positionsList.size(); i++) {
            positions[i] = positionsList.get(i);
            colours[i] = coloursList.get(i);
        }
        for (int i = 0; i < positionsList.size() / 3; i++) {
            vertices[i] = new Vector3f(positionsList.get(i*3), positionsList.get(i*3+1), positionsList.get(i*3+2));
        }

        Tetrahedron superTet = new Tetrahedron(new Vector3f(0, 1000, 0), new Vector3f(0, -1000, 1000), new Vector3f(-1000, -1000, -1000), new Vector3f(1000, -1000, -1000));
        ArrayList<Tetrahedron> tets = new ArrayList<>();
        tets.add(superTet);

        ArrayList<Tetrahedron> badTets = new ArrayList<>();
        ArrayList<Triangle> badTris = new ArrayList<>();

        for (Vector3f v : vertices) {
            badTets.clear();
            badTris.clear();
            for (Tetrahedron t : tets) {
                if (t.inCircumsphere(v)) {
                    badTets.add(t);
                    int index = indexOfFace(badTris, t.t0);
                    if (index < 0) {
                        badTris.add(t.t0);
                    } else {
                        badTris.remove(index);
                    }
                    index = indexOfFace(badTris, t.t1);
                    if (index < 0) {
                        badTris.add(t.t1);
                    } else {
                        badTris.remove(index);
                    }
                    index = indexOfFace(badTris, t.t2);
                    if (index < 0) {
                        badTris.add(t.t2);
                    } else {
                        badTris.remove(index);
                    }
                    index = indexOfFace(badTris, t.t3);
                    if (index < 0) {
                        badTris.add(t.t3);
                    } else {
                        badTris.remove(index);
                    }
                }
            }
            for (Tetrahedron t : badTets) {
                tets.remove(t);
            }
            for (Triangle t : badTris) {
                Tetrahedron tet = new Tetrahedron(v, t.v0, t.v1, t.v2);
                tets.add(tet);
            }
        }

        tets.removeIf(t->
            t.v0.equals(superTet.v0) || t.v0.equals(superTet.v1) || t.v0.equals(superTet.v2) || t.v0.equals(superTet.v3) ||
            t.v1.equals(superTet.v0) || t.v1.equals(superTet.v1) || t.v1.equals(superTet.v2) || t.v1.equals(superTet.v3) ||
            t.v2.equals(superTet.v0) || t.v2.equals(superTet.v1) || t.v2.equals(superTet.v2) || t.v2.equals(superTet.v3) ||
            t.v3.equals(superTet.v0) || t.v3.equals(superTet.v1) || t.v3.equals(superTet.v2) || t.v3.equals(superTet.v3)
        );

        positions = new float[tets.size()*4*3];
        colours = new float[tets.size()*4*3];
        Random rand = new Random(0);
        int[] indices = new int[tets.size()*4*3];
        for (int i = 0; i < tets.size(); i++) {
            positions[i*12] = tets.get(i).v0.x;
            positions[i*12+1] = tets.get(i).v0.y;
            positions[i*12+2] = tets.get(i).v0.z;
            positions[i*12+3] = tets.get(i).v1.x;
            positions[i*12+4] = tets.get(i).v1.y;
            positions[i*12+5] = tets.get(i).v1.z;
            positions[i*12+6] = tets.get(i).v2.x;
            positions[i*12+7] = tets.get(i).v2.y;
            positions[i*12+8] = tets.get(i).v2.z;
            positions[i*12+9] = tets.get(i).v3.x;
            positions[i*12+10] = tets.get(i).v3.y;
            positions[i*12+11] = tets.get(i).v3.z;

            colours[i*12] = rand.nextFloat();
            colours[i*12+1] = rand.nextFloat();
            colours[i*12+2] = rand.nextFloat();
            colours[i*12+3] = rand.nextFloat();
            colours[i*12+4] = rand.nextFloat();
            colours[i*12+5] = rand.nextFloat();
            colours[i*12+6] = rand.nextFloat();
            colours[i*12+7] = rand.nextFloat();
            colours[i*12+8] = rand.nextFloat();
            colours[i*12+9] = rand.nextFloat();
            colours[i*12+10] = rand.nextFloat();
            colours[i*12+11] = rand.nextFloat();

            indices[i*12] = i*4;
            indices[i*12+1] = i*4+1;
            indices[i*12+2] = i*4+2;
            indices[i*12+3] = i*4;
            indices[i*12+4] = i*4+2;
            indices[i*12+5] = i*4+3;
            indices[i*12+6] = i*4;
            indices[i*12+7] = i*4+3;
            indices[i*12+8] = i*4+1;
            indices[i*12+9] = i*4+1;
            indices[i*12+10] = i*4+2;
            indices[i*12+11] = i*4+3;
        }

        return new Mesh(positions, colours, indices, null);
    }

    int indexOfFace (ArrayList<Triangle> tris, Triangle t) {
        int index = -1;
        for (int i = 0; i < tris.size(); i++) {
            if (t.isEqualTo(tris.get(i))) {
                index = i;
                break;
            }
        }
        return index;
    }

    static class Edge {
        Vector3f v0,v1;
        public Edge (Vector3f v0, Vector3f v1) {
            this.v0 = v0;
            this.v1 = v1;
        }

        public boolean isEqualTo (Edge o) {
            return (this.v0.equals(o.v0) && this.v1.equals(o.v1)) || (this.v0.equals(o.v1) && this.v1.equals(o.v0));
        }
    }
    static class Triangle {
        Edge e0,e1,e2;
        Vector3f v0,v1,v2;

        public Triangle (Vector3f v0, Vector3f v1, Vector3f v2) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
            this.e0 = new Edge(v0, v1);
            this.e1 = new Edge(v1, v2);
            this.e2 = new Edge(v2, v0);
        }

        public boolean isEqualTo (Triangle o) {
            return  (this.v0.equals(o.v0) && this.v1.equals(o.v1) && this.v2.equals(o.v2)) ||
                    (this.v0.equals(o.v0) && this.v1.equals(o.v2) && this.v2.equals(o.v1)) ||
                    (this.v0.equals(o.v1) && this.v1.equals(o.v0) && this.v2.equals(o.v2)) ||
                    (this.v0.equals(o.v1) && this.v1.equals(o.v2) && this.v2.equals(o.v0)) ||
                    (this.v0.equals(o.v2) && this.v1.equals(o.v0) && this.v2.equals(o.v1)) ||
                    (this.v0.equals(o.v2) && this.v1.equals(o.v1) && this.v2.equals(o.v0));
        }
    }
    static class Tetrahedron {
        Triangle t0,t1,t2,t3; // The faces of the tetrahedron
        Vector3f v0,v1,v2,v3; // The vertices of the tetrahedron

        public Tetrahedron (Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3) {
            this.v0 = v0;
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
            this.t0 = new Triangle(v0, v1, v2);
            this.t1 = new Triangle(v0, v2, v3);
            this.t2 = new Triangle(v0, v3, v1);
            this.t3 = new Triangle(v1, v2, v3);
        }

        public boolean inCircumsphere(Vector3f p) {
            Matrix4f m = new Matrix4f(
                    v0.x-p.x, v0.y-p.y, v0.z-p.z, v0.lengthSquared()-p.lengthSquared(),
                    v1.x-p.x, v1.y-p.y, v1.z-p.z, v1.lengthSquared()-p.lengthSquared(),
                    v2.x-p.x, v2.y-p.y, v2.z-p.z, v2.lengthSquared()-p.lengthSquared(),
                    v3.x-p.x, v3.y-p.y, v3.z-p.z, v3.lengthSquared()-p.lengthSquared()
            );
            return m.determinant()<0;
        }
    }

}
