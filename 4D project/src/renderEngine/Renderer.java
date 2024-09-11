package renderEngine;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Renderer {
    private ShaderProgram shaderProgram;
    private UniformMap uniformsMap;
    public Renderer() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project/resources/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project/resources/shaders/scene.frag", GL_FRAGMENT_SHADER));

        shaderProgram = new ShaderProgram(shaderModuleDataList);

        createUniforms();
    }

    public void cleanup() {
        shaderProgram.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0,0, window.getWidth(), window.getHeight());

        shaderProgram.bind();

        uniformsMap.setUniform4x4f("projectionMatrix", scene.getProjection().getProjectionMatrix());

        for (Entity e : scene.getEntities()) {
            uniformsMap.setUniform4x4f("modelMatrix", e.getModelMatrix());
            Model model = scene.getModelMap().get(e.getModelId());
            model.getMeshList().forEach(m -> {
                glBindVertexArray(m.getVaoId());
                glDrawElements(GL_TRIANGLES, m.getNumVertices(), GL_UNSIGNED_INT, 0);
            });
        }

        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    private void createUniforms() {
        uniformsMap = new UniformMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
    }

}
