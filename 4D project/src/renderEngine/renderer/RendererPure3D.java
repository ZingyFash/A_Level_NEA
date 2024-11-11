package renderEngine.renderer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import renderEngine.engine.Scene;
import renderEngine.engine.ShaderProgram;
import renderEngine.engine.UniformMap;
import renderEngine.engine.Window;
import renderEngine.entity.Entity;
import renderEngine.entity.Mesh;
import renderEngine.entity.Model;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class RendererPure3D implements Renderer {
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
        uniformsMap.setUniform3f("disp", new Vector3f(0,0,0));

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

    public void createUniforms() {
        uniformsMap = new UniformMap(shaderProgram.getProgramId());
        uniformsMap.createUniform("projectionMatrix");
        uniformsMap.createUniform("modelMatrix");
        uniformsMap.createUniform("disp");
    }

}
