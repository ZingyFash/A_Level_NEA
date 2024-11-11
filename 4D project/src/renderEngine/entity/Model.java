package renderEngine.entity;

public class Model {

    private final String id;
    private Mesh mesh;

    public Model(String id, Mesh mesh) {
        this.id = id;
        this.mesh = mesh;
    }

    public void cleanup() {
        mesh.cleanup();
    }

    public String getId() {
        return id;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
