package renderEngine;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer2DSlice {
    private ComputeShader shader;
    private UniformMap uniformsMap;
    public Renderer2DSlice() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        shader = new ComputeShader("4D project/resources/shaders/2DSlice.comp");

        createUniforms();
    }

    public void cleanup() {
        //shader.cleanup();
    }

    public void render(Window window, Scene scene) {

    }

    private void createUniforms() {
        uniformsMap = null;
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
    }
}
