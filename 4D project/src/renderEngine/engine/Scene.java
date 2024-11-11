package renderEngine.engine;

import renderEngine.entity.Entity;
import renderEngine.entity.Entity4D;
import renderEngine.entity.Model;
import renderEngine.entity.Model4D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scene {

    private List<Entity> entities;
    private List<Entity4D> entities4D;
    private Map<String, Model> modelMap;
    private Map<String, Model4D> modelMap4D;
    private Projection projection;

    public Scene(int width, int height) {
        this.entities = new ArrayList<>();
        this.entities4D = new ArrayList<>();
        this.modelMap = new HashMap<>();
        this.modelMap4D = new HashMap<>();
        projection = new Projection(width, height);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }
    public void addEntity(Entity4D entity) {
        entities4D.add(entity);
    }

    public void addModel(Model model) {
        this.modelMap.put(model.getId(), model);
    }
    public void addModel(Model4D model) {
        this.modelMap4D.put(model.getId(), model);
    }

    public List<Entity> getEntities() {
        return entities;
    }
    public List<Entity4D> getEntities4D() {
        return entities4D;
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }
    public Map<String, Model4D> getModelMap4D() {
        return modelMap4D;
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
