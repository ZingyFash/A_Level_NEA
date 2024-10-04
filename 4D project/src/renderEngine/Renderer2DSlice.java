package renderEngine;

import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer2DSlice {
    private UniformMap uniformsMap2D;
    private UniformMap uniformsMap3D;
    ShaderProgram shaderProgram2D;
    ShaderProgram shaderProgram3D;

    public Renderer2DSlice() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\2D.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\2D.frag", GL_FRAGMENT_SHADER));
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList1 = new ArrayList<>();
        shaderModuleDataList1.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\Pure3D.vert", GL_VERTEX_SHADER));
        shaderModuleDataList1.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\Pure3D.frag", GL_FRAGMENT_SHADER));

        shaderProgram2D = new ShaderProgram(shaderModuleDataList);
        shaderProgram3D = new ShaderProgram(shaderModuleDataList1);
        uniformsMap2D = new UniformMap(shaderProgram2D.getProgramId());
        uniformsMap3D = new UniformMap(shaderProgram3D.getProgramId());
        createUniforms();
    }

    public void cleanup() {
        shaderProgram2D.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClearColor(1,1,1,1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0,0,window.getWidth(), window.getHeight());

        for (Entity entity : scene.getEntities()) {
            // The 2D cross-section
            shaderProgram2D.bind();
            uniformsMap2D.setUniform1f("aspectRatio", ((float) window.getWidth()) /window.getHeight());
            Mesh mesh = scene.getModelMap().get(entity.getModelId()).getMesh();
            mesh.transformVertices(entity.getModelMatrix());
            Mesh mesh2D = mesh.slice2D(Main.planeZ);
            mesh.transformVertices(entity.getModelMatrix().invert());
            glBindVertexArray(mesh2D.getVaoId());
            glDrawElements(GL_TRIANGLES, mesh2D.getNumVertices(), GL_UNSIGNED_INT, 0);
            shaderProgram2D.unbind();

            // The 3D entity
            shaderProgram3D.bind();
            uniformsMap3D.setUniform4x4f("projectionMatrix", scene.getProjection().getProjectionMatrix());
            uniformsMap3D.setUniform4x4f("modelMatrix", entity.getModelMatrix());
            glBindVertexArray(scene.getModelMap().get(entity.getModelId()).getMesh().getVaoId());
            glDrawElements(GL_TRIANGLES, scene.getModelMap().get(entity.getModelId()).getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
            glBindVertexArray(0);
            shaderProgram3D.unbind();
        }
    }

    private void createUniforms() {
        uniformsMap2D = new UniformMap(shaderProgram2D.getProgramId());
        uniformsMap2D.createUniform("aspectRatio");
        uniformsMap3D = new UniformMap(shaderProgram3D.getProgramId());
        uniformsMap3D.createUniform("projectionMatrix");
        uniformsMap3D.createUniform("modelMatrix");
    }
}
