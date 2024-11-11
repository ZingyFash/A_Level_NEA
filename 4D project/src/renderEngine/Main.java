package renderEngine;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderEngine.engine.Engine;
import renderEngine.engine.Scene;
import renderEngine.engine.Window;
import renderEngine.entity.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The class that outlines the logic for the whole application
 */
public class Main {

    public static float planeZ;
    //private Entity entity;
    private Entity4D entity;
    private Entity plane;
    private final Vector4f velocity = new Vector4f();
    private float rotation;
    private boolean paused = false;

    /**
     * The entry point of the application, sets up the window and the engine.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Main main = new Main();
        Window.WindowOptions windowOptions = new Window.WindowOptions();
        windowOptions.fps = 60;
        // initialise and run the graphics engine
        Engine engine = new Engine("4D", windowOptions, main);
        engine.start();
    }

    /**
     * Sets up the models and entities in the scene
     * @param window The window in which the scene is rendered
     * @param scene The scene, that holds all the entities
     */
    public void init(Window window, Scene scene) {
//        // define the positions of the vertices of the cube
//        float[] positions = new float[]{
//                -0.5f, 0.5f, 0.5f, // 0
//                -0.5f, -0.5f, 0.5f, // 1
//                0.5f, -0.5f, 0.5f, // 2
//                0.5f, 0.5f, 0.5f, // 3
//                -0.5f, 0.5f, -0.5f, // 4
//                0.5f, 0.5f, -0.5f, // 7
//                -0.5f, -0.5f, -0.5f, // 5
//                0.5f, -0.5f, -0.5f, // 6
//        };
//        boolean[][] adjacencyMatrix = {
//                {false, true, false, true, true, false, false, false}, // 0
//                {true, false, true, false, false, false, true, false}, // 1
//                {false, true, false, true, false, false, false, true}, // 2
//                {true, false, true, false, false, true, false, false}, // 3
//                {true, false, false, false, false, true, true, false}, // 4
//                {false, false, false, true, true, false, false, true}, // 5
//                {false, true, false, false, true, false, false, true}, // 6
//                {false, false, true, false, false, true, true, false}, // 7
//        };
//
//        float[] colours = {
//                0.0f, 1.0f, 1.0f,
//                0.0f, 1.0f, 1.0f,
//                1.0f, 0.0f, 1.0f,
//                1.0f, 1.0f, 1.0f,
//                0.0f, 1.0f, 0.0f,
//                1.0f, 1.0f, 0.0f,
//                0.0f, 0.0f, 0.0f,
//                1.0f, 0.0f, 0.0f
//        };
//
//        // define the order in which to draw the vertices
//        int[] indices = new int[]{
//                // Front face
//                0, 1, 3, 3, 1, 2,
//                // Top Face
//                4, 0, 3, 5, 4, 3,
//                // Right face
//                3, 2, 7, 5, 3, 7,
//                // Left face
//                6, 1, 0, 6, 0, 4,
//                // Bottom face
//                2, 1, 6, 2, 6, 7,
//                // Back face
//                7, 6, 4, 7, 4, 5,
//        };
//
//        float[] quadPositions = {-1,-1,0,-1,1,0,1,1,0,1,-1,0};
//        float[] quadColours = {0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f, 0.5f, 0.5f, 0.5f};
//        int[] quadIndices = {0,1,2,2,0,3};
//
//
//        // create mesh, model and entity
//        Mesh mesh = new Mesh(positions, colours, indices, adjacencyMatrix);
//        String cubeModelId = "cube-model";
//        Model model = new Model(cubeModelId, mesh);
//        scene.addModel(model);
//        Mesh quadMesh = new Mesh(quadPositions, quadColours, quadIndices, new boolean[][]{{false,true,false,true},{true,false,true,false},{false,true,false,true},{true,false,true,false}});
//        String quadModelId = "quad";
//        Model quad = new Model(quadModelId, quadMesh);
//        scene.addModel(quad);
//
//        entity = new Entity(cubeModelId);
//        entity.setPosition(0,0,-5);
//        entity.updateModelMatrix();
//        scene.addEntity(entity);
//        plane = new Entity(quadModelId);
//        plane.setPosition(0,0,0);
//        plane.updateModelMatrix();
//        scene.addEntity(plane);

        float[] positions = {
                -.5f,.5f,.5f,-.5f,      // 0
                -.5f,-.5f,.5f,-.5f,     // 1
                .5f,-.5f,.5f,-.5f,      // 2
                .5f,.5f,.5f,-.5f,       // 3
                -.5f,.5f,-.5f,-.5f,     // 4
                -.5f,-.5f,-.5f,-.5f,    // 5
                .5f,-.5f,-.5f,-.5f,     // 6
                .5f,.5f,-.5f,-.5f,      // 7
                -.5f,.5f,.5f,.5f,       // 8
                -.5f,-.5f,.5f,.5f,      // 9
                .5f,-.5f,.5f,.5f,       // 10
                .5f,.5f,.5f,.5f,        // 11
                -.5f,.5f,-.5f,.5f,      // 12
                -.5f,-.5f,-.5f,.5f,     // 13
                .5f,-.5f,-.5f,.5f,      // 14
                .5f,.5f,-.5f,.5f        // 15
        };
        float[] colours = {
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
        int[][] indices = {
                {1,3,4,8},
                {2,5,9},
                {3,6,10},
                {7,11},
                {5,7,12},
                {6,13},
                {7,14},
                {15},
                {9,11,12},
                {10,13},
                {11,14},
                {15},
                {13,15},
                {14},
                {15},
                {}
        };
        Mesh4D mesh4D = new Mesh4D(positions, colours, indices);
        String modelID = "Tesseract";
        Model4D model = new Model4D(modelID, mesh4D);
        scene.addModel(model);
        entity = new Entity4D(modelID);
        entity.setPosition(2,2,-5,0);
        scene.addEntity(entity);
    }

    /**
     * This function is called periodically based on the UPS, used for backend physics updates
     * @param window The window that holds the scene
     * @param scene The scene that holds the entities
     * @param diffTimeMillis The difference in time since the last update
     */
    public void update(Window window, Scene scene, long diffTimeMillis) {
//        if(!paused) rotation += .01f;
//        if (rotation > 2*Math.PI) {
//            rotation = 0;
//        }
////        for (int i = 0; i < entities.length; i++) {
////            entities[i].setRotation(Math.sin(rotation), Math.cos(rotation), 0, rotation);
////            entities[i].setPosition((i%10-5)*1.5f+Math.sin(rotation), (i/10 - 5) * 1.5f+Math.cos(rotation), -5+Math.sin(rotation)+Math.cos(rotation));
////            entities[i].setScale(Math.cos(rotation));
////            entities[i].updateModelMatrix();
////        }
//        entity.setRotation(Math.sin(rotation),Math.cos(rotation),0, rotation);
//        //entity.setScale(Math.cos(rotation));
//        entity.updateModelMatrix();
    }

    /**
     * Called once every frame. Handles user input
     * @param window the window that holds the scene
     * @param scene the scene that holds the entities
     * @param diffTimeMillis The time since the last update
     */
    private boolean lastFrameSpacePressed = false;

    public void input(Window window, Scene scene, long diffTimeMillis) {
        velocity.zero();
        velocity.x = (window.isKeyPressed(GLFW_KEY_A)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_D)) ? 1 : 0;
        velocity.y = (window.isKeyPressed(GLFW_KEY_S)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_W)) ? 1 : 0;
        velocity.z = (window.isKeyPressed(GLFW_KEY_Q)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_E)) ? 1 : 0;
        if (window.isKeyPressed(GLFW_KEY_SPACE) != lastFrameSpacePressed && window.isKeyPressed(GLFW_KEY_SPACE)) {
            paused = !paused;
        }
        lastFrameSpacePressed = window.isKeyPressed(GLFW_KEY_SPACE);

        velocity.mul(diffTimeMillis / 1000.0f * 10);

        Vector4f entityPos = entity.getPosition();
        entity.setPosition(velocity.x() + entityPos.x(), velocity.y() + entityPos.y(), velocity.z()+entityPos.z(), entityPos.w());
        //planeZ+=velocity.z;
        //plane.setPosition(plane.getPosition().add(new Vector3f(0,0,velocity.z)));
        //entity.updateModelMatrix();
        //plane.updateModelMatrix();
    }

    public void cleanup() {
    }
}