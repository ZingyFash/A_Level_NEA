package renderEngine;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

public class ComputeShader {
    private int id;
    private int outTex;
    ComputeShader(String path) {

    }

    public void use() {
        glUseProgram(id);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE0, outTex);
    }

    public void dispatch() {
        glDispatchCompute(100,100 ,1);
    }

}
