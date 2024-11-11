package renderEngine.entity;

public class Model4D {

    private final String id;
    private Mesh4D mesh4D;

    public Model4D (String id, Mesh4D mesh4D) {
        this.id = id;
        this.mesh4D = mesh4D;
    }

    public String getId() {
        return id;
    }

    public Mesh4D getMesh() {
        return mesh4D;
    }

}
