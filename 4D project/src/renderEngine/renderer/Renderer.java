package renderEngine.renderer;

import renderEngine.engine.Scene;
import renderEngine.engine.Window;

public interface Renderer {
    void cleanup();
    void render(Window window, Scene scene);
    void createUniforms();
}
