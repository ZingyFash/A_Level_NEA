package renderEngine;

import java.util.List;

public class Model {

    private final String id;
    private List<Mesh> meshList;

    public Model(String id, List<Mesh> meshList) {
        this.id = id;
        this.meshList = meshList;
    }

    public void cleanup() {
        meshList.forEach(Mesh::cleanup);
    }

    public String getId() {
        return id;
    }

    public List<Mesh> getMeshList() {
        return meshList;
    }
}
