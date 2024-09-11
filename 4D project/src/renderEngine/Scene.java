package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private List<Entity> entities;
    private Map<String, Model> modelMap;
    private Projection projection;

    public Scene(int width, int height) {
        this.entities = new ArrayList<>();
        this.modelMap = new HashMap<>();
        projection = new Projection(width, height);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void addModel(Model model) {
        this.modelMap.put(model.getId(), model);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }

    public Projection getProjection() {
        return projection;
    }

    public void resize(int width, int height) {
        projection.updateProjectionMatrix(width, height);
    }

    public void cleanup() {
        modelMap.values().forEach(Model::cleanup);
    }
}
