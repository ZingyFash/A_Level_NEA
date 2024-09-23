package renderEngine;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public class Renderer2DSlice {
    private UniformMap uniformsMap;
    ShaderProgram shader;

    public Renderer2DSlice() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\2D.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\2D.frag", GL_FRAGMENT_SHADER));

        shader = new ShaderProgram(shaderModuleDataList);
        uniformsMap = new UniformMap(shader.getProgramId());
        createUniforms();
    }

    public void cleanup() {
        shader.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0,0,window.getWidth(), window.getHeight());

        shader.bind();

        for (Entity entity : scene.getEntities()) {
            scene.getModelMap().get(entity.getModelId()).getMesh().transformVertices(entity.getModelMatrix());
            Mesh mesh2D = scene.getModelMap().get(entity.getModelId()).getMesh().slice2D(1);
            glBindVertexArray(mesh2D.getVaoId());
            glDrawElements(GL_TRIANGLES, mesh2D.getNumVertices(), GL_UNSIGNED_INT, 0);
        }

        shader.bind();

    }

    private void createUniforms() {

    }
}
