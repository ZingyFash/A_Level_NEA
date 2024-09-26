package renderEngine;

import org.joml.Matrix4f;
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
    private UniformMap uniformsMap1;
    ShaderProgram shader;
    ShaderProgram shaderProgram;

    public Renderer2DSlice() {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);

        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\2D.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\2D.frag", GL_FRAGMENT_SHADER));
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList1 = new ArrayList<>();
        shaderModuleDataList1.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\Pure3D.vert", GL_VERTEX_SHADER));
        shaderModuleDataList1.add(new ShaderProgram.ShaderModuleData("4D project\\resources\\shaders\\Pure3D.frag", GL_FRAGMENT_SHADER));

        shader = new ShaderProgram(shaderModuleDataList);
        shaderProgram = new ShaderProgram(shaderModuleDataList1);
        uniformsMap = new UniformMap(shader.getProgramId());
        uniformsMap1 = new UniformMap(shaderProgram.getProgramId());
        createUniforms();
    }

    public void cleanup() {
        shader.cleanup();
    }

    public void render(Window window, Scene scene) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0,0,window.getWidth(), window.getHeight());

        shader.bind();

        uniformsMap.setUniform1f("aspectRatio", ((float) window.getWidth()) /window.getHeight());

        Entity entity = scene.getEntities().get(0);
        scene.getModelMap().get(entity.getModelId()).getMesh().transformVertices(entity.getModelMatrix());
        Mesh mesh2D = scene.getModelMap().get(entity.getModelId()).getMesh().slice2D(Main.planeZ);
        scene.getModelMap().get(entity.getModelId()).getMesh().transformVertices(entity.getModelMatrix().invert());
        glBindVertexArray(mesh2D.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh2D.getNumVertices(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.unbind();

        shaderProgram.bind();

        uniformsMap1.setUniform4x4f("projectionMatrix", scene.getProjection().getProjectionMatrix());

        entity = scene.getEntities().get(1);
        glBindVertexArray(scene.getModelMap().get(entity.getModelId()).getMesh().getVaoId());
        glDrawElements(GL_TRIANGLES, scene.getModelMap().get(entity.getModelId()).getMesh().getNumVertices(), GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
        Entity e = scene.getEntities().get(0);

        uniformsMap1.setUniform4x4f("modelMatrix", e.getModelMatrix());
        Model model = scene.getModelMap().get(e.getModelId());
        Mesh mesh = model.getMesh();
        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getNumVertices(), GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        Matrix4f I = new Matrix4f(1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1);
        uniformsMap1.setUniform4x4f("modelMatrix", I);

        shaderProgram.unbind();
    }

    private void createUniforms() {
        uniformsMap = new UniformMap(shader.getProgramId());
        uniformsMap.createUniform("aspectRatio");
        uniformsMap1 = new UniformMap(shaderProgram.getProgramId());
        uniformsMap1.createUniform("projectionMatrix");
        uniformsMap1.createUniform("modelMatrix");
    }
}
