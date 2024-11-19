package renderEngine.Meshes;

public class MeshData {
    public static class Cube {
        public static final float[] positions = new float[]{
                -0.5f, 0.5f, 0.5f, // 0
                -0.5f, -0.5f, 0.5f, // 1
                0.5f, -0.5f, 0.5f, // 2
                0.5f, 0.5f, 0.5f, // 3
                -0.5f, 0.5f, -0.5f, // 4
                0.5f, 0.5f, -0.5f, // 7
                -0.5f, -0.5f, -0.5f, // 5
                0.5f, -0.5f, -0.5f, // 6
        };
        public static final boolean[][] adjacencyMatrix = {
                {false, true, false, true, true, false, false, false}, // 0
                {true, false, true, false, false, false, true, false}, // 1
                {false, true, false, true, false, false, false, true}, // 2
                {true, false, true, false, false, true, false, false}, // 3
                {true, false, false, false, false, true, true, false}, // 4
                {false, false, false, true, true, false, false, true}, // 5
                {false, true, false, false, true, false, false, true}, // 6
                {false, false, true, false, false, true, true, false}, // 7
        };

        public static final float[] colours = {
                0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f,
                1.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f
        };

        // define the order in which to draw the vertices
        public static final int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };
    }

    public static class Tesseract {
        public static final float[] positions = {
                -.5f,.5f,.5f,-.5f,      // 0
                -.5f,-.5f,.5f,-.5f,     // 1
                .5f,.5f,.5f,-.5f,       // 3
                .5f,-.5f,.5f,-.5f,      // 2
                -.5f,.5f,-.5f,-.5f,     // 4
                -.5f,-.5f,-.5f,-.5f,    // 5
                .5f,.5f,-.5f,-.5f,      // 7
                .5f,-.5f,-.5f,-.5f,     // 6
                -.5f,.5f,.5f,.5f,       // 8
                -.5f,-.5f,.5f,.5f,      // 9
                .5f,.5f,.5f,.5f,        // 11
                .5f,-.5f,.5f,.5f,       // 10
                -.5f,.5f,-.5f,.5f,      // 12
                -.5f,-.5f,-.5f,.5f,     // 13
                .5f,.5f,-.5f,.5f,        // 15
                .5f,-.5f,-.5f,.5f,      // 14
        };
        public static final float[] colours = {
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f
        };
        public static final int[][][] indices = {
                // 1,2,4 (a)
                {
                        /* 1,2,4 */     {0, 1, 3, 7},
                        /* 1,4,2 */     {0, 1, 5, 7},
                        /* 2,1,4 */     {0, 2, 3, 7},
                        /* 2,4,1 */     {0, 2, 6, 7},
                        /* 4,1,2 */     {0, 4, 5, 7},
                        /* 4,2,1 */     {0, 4, 6, 7}
                },
                // 1,2,4 (b)
                {
                        {8, 9, 11, 15},
                        {8, 9, 13, 15},
                        {8, 10, 11, 15},
                        {8, 10, 14, 15},
                        {8, 12, 13, 15},
                        {8, 12, 14, 15}
                },
                // 1,2,8 (a)
                {
                        /* 1,2,8 */     {0, 1, 3, 11},
                        /* 1,8,2 */     {0, 1, 9, 11},
                        /* 2,1,8 */     {0, 2, 3, 11},
                        /* 2,8,1 */     {0, 2, 10, 11},
                        /* 8,1,2 */     {0, 8, 9, 11},
                        /* 8,2,1 */     {0, 8, 10, 11}
                },
                // 1,2,8 (b)
                {
                        {4, 5, 7, 15},
                        {4, 5, 13, 15},
                        {4, 6, 7, 15},
                        {4, 6, 14, 15},
                        {4, 12, 13, 15},
                        {4, 12, 14, 15}
                },
                // 1,4,8 (a)
                {
                        /* 1,4,8 */     {0, 1, 5, 13},
                        /* 1,8,4 */     {0, 1, 9, 13},
                        /* 4,1,8 */     {0, 4, 5, 13},
                        /* 4,8,1 */     {0, 4, 12, 13},
                        /* 8,1,4 */     {0, 8, 9, 13},
                        /* 8,4,1 */     {0, 8, 12, 13}
                },
                // 1,4,8 (b)
                {
                        {2, 3, 7, 15},
                        {2, 3, 11, 15},
                        {2, 6, 7, 15},
                        {2, 6, 14, 15},
                        {2, 10, 11, 15},
                        {2, 10, 14, 15}
                },
                // 2,4,8 (a)
                {
                        /* 2,4,8 */     {0, 2, 6, 14},
                        /* 2,8,4 */     {0, 2, 10, 14},
                        /* 4,2,8 */     {0, 4, 6, 14},
                        /* 4,8,2 */     {0, 4, 12, 14},
                        /* 8,2,4 */     {0, 8, 10, 14},
                        /* 8,4,2 */     {0, 8, 12, 14}
                },
                // 2,4,8 (b)
                {
                        {1, 3, 7, 15},
                        {1, 3, 11, 15},
                        {1, 5, 7, 15},
                        {1, 5, 13, 15},
                        {1, 9, 11, 15},
                        {1, 9, 13, 15}
                }
        };
    }

    public static class FiveCell {
        public static final float[] positions = {
                0.5f, -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f, -0.5f,
                0, 0.5f, 0, -0.5f,
                0, -0.5f, 0.5f, -0.5f,
                0, -0.5f, -0.5f, 0.5f
        };
        public static final float[] colours = {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                1, 1, 0, 0,
                1, 0, 1, 0
        };
        public static final int[][][] indices = {
                {
                        {0, 1, 2, 3},
                        {0, 1, 2, 4},
                        {0, 1, 3, 4},
                        {0, 2, 3, 4},
                        {1, 2, 3, 4}
                }
        };
    }

    public static class Tetrahedra {
        public static int[][] edges = {{0,1},{0,2},{0,3},{1,2},{2,3},{1,3}};
    }
}
