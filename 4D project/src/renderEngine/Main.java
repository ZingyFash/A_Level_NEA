package renderEngine;

import org.joml.Math;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class Main {

    private Entity cubeEntity1;
    private Entity cubeEntity2;
    private Entity[] entities = new Entity[5];
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
        List<Mesh> meshList = new ArrayList<>();
        Mesh mesh = new Mesh(positions, colors, indices);
        meshList.add(mesh);
        String cubeModelId = "cube-model";
        Model model = new Model(cubeModelId, meshList);
        scene.addModel(model);

        for (int i = 0; i < entities.length; i++) {
            entities[i] = new Entity(cubeModelId);
            entities[i].setPosition((i-2)*1.5f, 0, -5);
            scene.addEntity(entities[i]);
        }

//        List<Mesh> meshList1 = new ArrayList<>();
//        for (int i = 0; i < positions.length / 3; i++) {
//            positions[i*3] *= -1;
//        }
//        Mesh mesh1 = new Mesh(positions, colors, indices);
//        meshList1.add(mesh1);
//        String cubeModelId1 = "cube-model1";
//        Model model1 = new Model(cubeModelId1, meshList1);
//        scene.addModel(model1);
//
//        cubeEntity1 = new Entity(cubeModelId);
//        cubeEntity1.setPosition(-1, 0, -3);
//        scene.addEntity(cubeEntity1);
//        cubeEntity2 = new Entity(cubeModelId1);
//        cubeEntity2.setPosition(1, 0, -3);
//        scene.addEntity(cubeEntity2);
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
//        cubeEntity1.setRotation(Math.sin(rotation), Math.cos(rotation), 0, rotation);
//        cubeEntity1.setScale(Math.cos(rotation));
//        cubeEntity1.updateModelMatrix();
//        cubeEntity2.setRotation(-Math.sin(rotation), Math.cos(rotation), 0, -rotation);
//        cubeEntity2.setScale(Math.cos(rotation));
//        cubeEntity2.updateModelMatrix();
    }

    public void cleanup() {
    }

    public void input(Window window, Scene scene, long diffTimeMillis) {
        velocity.zero();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            velocity.y += 1;
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            velocity.y -= 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            velocity.x -= 1;
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            velocity.x += 1;
        }

        velocity.mul(diffTimeMillis / 1000.0f);
//        Vector3f entityPos = cubeEntity1.getPosition();
//        cubeEntity1.setPosition(velocity.x + entityPos.x, velocity.y + entityPos.y, velocity.z + entityPos.z);
//        cubeEntity1.setScale(cubeEntity1.getScale() + velocity.w);
//        cubeEntity1.updateModelMatrix();
//        entityPos = cubeEntity2.getPosition();
//        cubeEntity2.setPosition(velocity.x + entityPos.x, velocity.y + entityPos.y, velocity.z + entityPos.z);
//        cubeEntity2.setScale(cubeEntity2.getScale() + velocity.w);
//        cubeEntity2.updateModelMatrix();
    }
}