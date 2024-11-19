package renderEngine.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import renderEngine.*;
import renderEngine.engine.Scene;
import renderEngine.engine.ShaderProgram;
import renderEngine.engine.UniformMap;
import renderEngine.engine.Window;
import renderEngine.entity.Entity4D;
import renderEngine.entity.Mesh;
import renderEngine.entity.Mesh4D;
import renderEngine.entity.Model4D;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer4D implements Renderer {
    private ShaderProgram shaderProgram;
    private UniformMap uniformsMap;
    public Renderer4D() {
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

        for (Entity4D e : scene.getEntities4D()) {
            uniformsMap.setUniform4x4f("modelMatrix", new Matrix4f());
            Model4D model = scene.getModelMap4D().get(e.getModelId());
            Mesh4D mesh = model.getMesh();
            mesh.transformVertices(e.getPosition(), e.getRotation());
            Mesh mesh3 = mesh.slice3D(Main.planeW);
            glBindVertexArray(mesh3.getVaoId());
            glDrawElements(GL_TRIANGLES, mesh3.getNumVertices(), GL_UNSIGNED_INT, 0);
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
