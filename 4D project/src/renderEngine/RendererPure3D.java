package renderEngine;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public class RendererPure3D {
    private ShaderProgram shaderProgram;
    private UniformMap uniformsMap;
    public RendererPure3D() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project/resources/shaders/Pure3D.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project/resources/shaders/Pure3D.frag", GL_FRAGMENT_SHADER));

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
            Mesh mesh = model.getMesh();
            glBindVertexArray(mesh.getVaoId());
            glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);
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