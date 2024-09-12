package renderEngine;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    private Entity[] entities = new Entity[15];
    private Vector4f velocity = new Vector4f();
    private float rotation;

    public static void main(String[] args) {
        Main main = new Main();
        Engine engine = new Engine("4D", new Window.WindowOptions(), main);
        engine.start();
    }

    public void init(Window window, Scene scene, Renderer render) {
        // define the positions of the vertices of the cube
        float[] positions = new float[]{
                // VO
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };
        float[] colors = new float[24];

        // randomly generate the colours of the vertices
        for (int i = 0; i < 24; i++) {
            colors[i] = Math.abs(new Random().nextFloat());
        }

        // define the order in which to draw the vertices
        int[] indices = new int[]{
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

        // create mesh, model and entity
        Mesh mesh = new Mesh(positions, colors, indices);
        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, mesh);
        scene.addModel(model);

        for (int i = 0; i < entities.length; i++) {
            entities[i] = new Entity(cubeModelId);
            entities[i].setPosition((i%5-2)*1.5f, (i/5 - 1) * 1.5f, -5);
            scene.addEntity(entities[i]);
        }

    }

    public void update(Window window, Scene scene, long diffTimeMillis) {
        rotation += .06f + 0.05f * Math.sin(rotation);
        if (rotation > 2*Math.PI) {
            rotation = 0;
        }
        for (int i = 0; i < entities.length; i++) {
            entities[i].setRotation(Math.sin(rotation), Math.cos(rotation), 0, rotation);
            entities[i].setScale(Math.cos(rotation));
            entities[i].updateModelMatrix();
        }
    }

    public void cleanup() {
    }

    public void input(Window window, Scene scene, long diffTimeMillis) {
        velocity.zero();
        velocity.x = (window.isKeyPressed(GLFW_KEY_A)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_D)) ? 1 : 0;
        velocity.y = (window.isKeyPressed(GLFW_KEY_S)) ? -1 :
                (window.isKeyPressed(GLFW_KEY_W)) ? 1 : 0;

        velocity.mul(diffTimeMillis / 1000.0f);

        for (Entity entity : entities) {
            Vector3f entityPos = entity.getPosition();
            entity.setPosition(velocity.x + entityPos.x, velocity.y + entityPos.y, velocity.z + entityPos.z);
            entity.updateModelMatrix();
        }
    }
}