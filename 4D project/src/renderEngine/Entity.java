package renderEngine;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Each object in the scene is referenced as an Entity
 */
public class Entity {

    private final String modelId;
    private Matrix4f modelMatrix;
    private Vector3f position;
    private Quaternionf rotation;
    private float scale;

    public Entity(String modelId) {
        this.modelId = modelId;
        modelMatrix = new Matrix4f();
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = 1;
    }

    /**
     * @return String - modelId - The reference to the model in the scenes modelMap
     */
    public String getModelId() {
        return modelId;
    }

    /**
     * @return Matrix4f - modelMatrix - The matrix encoding the position, rotation, and scale of the object.
     * This describes how to transform the vertices of the model
     */
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
    public Vector3f getPosition() {
        return position;
    }
    public Quaternionf getRotation() {
        return rotation;
    }
    public float getScale() {
        return scale;
    }

    public final void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    public final void setPosition(Vector3f v) {
        position.x = v.x();
        position.y = v.y();
        position.z = v.z();
    }
    public void setRotation(float x, float y, float z, float angle) {
        rotation.fromAxisAngleRad(x,y,z,angle);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void updateModelMatrix() {
        modelMatrix.translationRotateScale(position, rotation, scale);
    }
}
