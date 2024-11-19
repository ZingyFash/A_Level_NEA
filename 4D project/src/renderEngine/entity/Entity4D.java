package renderEngine.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Entity4D {
    private final String modelId;
    private Matrix4f modelMatrix;
    private Vector4f position;
    private float[] rotation;
    private float scale;

    public Entity4D(String modelId) {
        this.modelId = modelId;
        position = new Vector4f();
        rotation = new float[6];
        scale = 1;
    }

    public String getModelId() {
        return modelId;
    }
    public final void setPosition(float x, float y, float z, float w) {
        position.x = x;
        position.y = y;
        position.z = z;
        position.w = w;
    }

    public Vector4f getPosition() {
        return position;
    }

    public float[] getRotation() {
        return rotation;
    }

    public void setRotation(float[] rotation) {
        if (rotation.length != 6) return;
        this.rotation = rotation;
    }
}
