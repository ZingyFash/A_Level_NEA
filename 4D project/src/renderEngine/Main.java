package renderEngine;

import org.joml.Vector3f;
import org.joml.Vector4f;
import renderEngine.engine.Engine;
import renderEngine.engine.Scene;
import renderEngine.engine.Window;
import renderEngine.entity.*;

import static org.lwjgl.glfw.GLFW.*;
import static renderEngine.Meshes.MeshData.*;

/**
 * The class that outlines the logic for the whole application
 */
public class Main {

    public static float planeZ;
    public static float planeW;
    private Entity entity;
    private Entity4D entity4D;
    private Entity plane;
    private final Vector4f velocity = new Vector4f();
    private float rotation;
    private boolean paused = false;

    /**
     * The entry point of the application, sets up the window and the engine.
     *
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
     *
     * @param window The window in which the scene is rendered
     * @param scene  The scene, that holds all the entities
     */
    public void init(Window window, Scene scene) {
        float[] quadPositions = {-1, -1, 0, -1, 1, 0, 1, 1, 0, 1, -1, 0};
        float[] quadColours = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};
        int[] quadIndices = {0, 1, 2, 2, 0, 3};


        // create mesh, model and entity
        Mesh mesh = new Mesh(Cube.positions, Cube.colours, Cube.indices, Cube.adjacencyMatrix);
        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, mesh);
        scene.addModel(model);
        Mesh quadMesh = new Mesh(quadPositions, quadColours, quadIndices, new boolean[][]{{false, true, false, true}, {true, false, true, false}, {false, true, false, true}, {true, false, true, false}});
        String quadModelId = "quad";
        Model quad = new Model(quadModelId, quadMesh);
        scene.addModel(quad);

        entity = new Entity(cubeModelId);
        entity.setPosition(0, 0, 0);
        entity.updateModelMatrix();
        scene.addEntity(entity);
        plane = new Entity(quadModelId);
        plane.setPosition(0, 0, 0);
        plane.updateModelMatrix();
        scene.addEntity(plane);


        Mesh4D mesh4D = new Mesh4D(Tesseract.positions, Tesseract.colours, Tesseract.indices);
        String modelID = "Tesseract";
        Model4D model4D = new Model4D(modelID, mesh4D);
        scene.addModel(model4D);
        entity4D = new Entity4D(modelID);
        entity4D.setPosition(0, 0, -5, 0);
        scene.addEntity(entity4D);
    }

    /**
     * This function is called periodically based on the UPS, used for backend physics updates
     *
     * @param window         The window that holds the scene
     * @param scene          The scene that holds the entities
     * @param diffTimeMillis The difference in time since the last update
     */
    public void update(Window window, Scene scene, long diffTimeMillis) {
        if (!paused) rotation += .05f;
        if (rotation > 2 * Math.PI) {
            rotation = 0;
        }
        entity.setRotation(1,0,0, rotation);
        entity.updateModelMatrix();
        //entity4D.setRotation(new float[]{rotation, rotation, rotation, rotation, rotation, rotation});
    }

    /**
     * Called once every frame. Handles user input
     *
     * @param window the window that holds the scene
     * @param scene the scene that holds the entities
     * @param diffTimeMillis The time since the last update
     */
    private boolean lastFrameSpacePressed = false;

    public void input(Window window, Scene scene, long diffTimeMillis) {
        float[] rotation = {0, 0, 0, 0, 0, 0};
        velocity.zero();
        velocity.x = (window.isKeyPressed(GLFW_KEY_A)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_D)) ? 1 : 0;
        velocity.y = (window.isKeyPressed(GLFW_KEY_S)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_W)) ? 1 : 0;
        velocity.z = (window.isKeyPressed(GLFW_KEY_Q)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_E)) ? 1 : 0;
        velocity.w = (window.isKeyPressed(GLFW_KEY_Z)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_X)) ? 1 : 0;

        rotation[0] = (window.isKeyPressed(GLFW_KEY_T)) ? 0.05f :
                (window.isKeyPressed(GLFW_KEY_Y)) ? -0.05f : 0;
        rotation[1] = (window.isKeyPressed(GLFW_KEY_U)) ? 0.05f :
                (window.isKeyPressed(GLFW_KEY_I)) ? -0.05f : 0;
        rotation[2] = (window.isKeyPressed(GLFW_KEY_O)) ? 0.05f :
                (window.isKeyPressed(GLFW_KEY_P)) ? -0.05f : 0;
        rotation[3] = (window.isKeyPressed(GLFW_KEY_H)) ? 0.05f :
                (window.isKeyPressed(GLFW_KEY_J)) ? -0.05f : 0;
        rotation[4] = (window.isKeyPressed(GLFW_KEY_K)) ? 0.05f :
                (window.isKeyPressed(GLFW_KEY_L)) ? -0.05f : 0;
        rotation[5] = (window.isKeyPressed(GLFW_KEY_N)) ? 0.05f :
                (window.isKeyPressed(GLFW_KEY_M)) ? -0.05f : 0;
        if (window.isKeyPressed(GLFW_KEY_SPACE) != lastFrameSpacePressed && window.isKeyPressed(GLFW_KEY_SPACE)) {
            paused = !paused;
        }
        if (window.isKeyPressed(GLFW_KEY_ENTER)) {
            entity4D.setRotation(new float[]{0, 0, 0, 0, 0, 0});
            plane.setPosition(new Vector3f(0,0,0));
            planeW = 0;
            planeZ = 0;
        }
        lastFrameSpacePressed = window.isKeyPressed(GLFW_KEY_SPACE);

        velocity.mul(diffTimeMillis / 1000.0f * 5);

        Vector4f entity4DPosition = entity4D.getPosition();
        entity4D.setPosition(velocity.x() + entity4DPosition.x(), velocity.y() + entity4DPosition.y(), velocity.z() + entity4DPosition.z(), entity4DPosition.w());
        planeW += velocity.w;
        float[] currentRotation = entity4D.getRotation();
        entity4D.setRotation(new float[]{
                currentRotation[0] + rotation[0], currentRotation[1] + rotation[1], currentRotation[2] + rotation[2], currentRotation[3] + rotation[3], currentRotation[4] + rotation[4], currentRotation[5] + rotation[5]
        });
        Vector3f entityPos = entity.getPosition();
        entity.setPosition(velocity.x() + entityPos.x(), velocity.y() + entityPos.y(), entityPos.z());
        planeZ += velocity.z;
        plane.setPosition(plane.getPosition().add(new Vector3f(0, 0, velocity.z)));
        entity.updateModelMatrix();
        plane.updateModelMatrix();
    }

    public void cleanup() {
    }
}