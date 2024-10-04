package renderEngine;

/**
 * The class that defines the logic for rendering a scene and handling updates and inputs
 */
public class Engine {

    public static final int TARGET_UPS = 30;
    private final Main main;
    private final Window window;
    private Renderer2DSlice render;
    private boolean running;
    private Scene scene;
    private int targetFps;
    private int targetUps;

    public Engine(String windowTitle, Window.WindowOptions opts, Main main) {
        window = new Window(windowTitle, opts, () -> {
            resize();
            return null;
        });
        scene = new Scene(window.getWidth(), window.getHeight());
        targetFps = opts.fps;
        targetUps = opts.ups;
        this.main = main;
        render = new Renderer2DSlice();
        main.init(window, scene, render);
        running = true;
    }

    public void start() {
        running = true;
        run();
    }

    public void stop() {
        running = false;
    }

    /**
     * Periodically calls the update function in Main and render function in the desired renderer class
     */
    private void run() {
        long initialTime = System.currentTimeMillis();
        float timeU = 1000.0f / targetUps;
        float timeR = (targetFps > 0) ? 1000.0f / targetFps : 0;
        float deltaUpdate = 0;
        float deltaR = 0;

        long updateTime = initialTime;
        while (running && !window.windowShouldClose()) {
            window.pollEvents();

            long now = System.currentTimeMillis();
            deltaUpdate += (now - initialTime) / timeU;
            deltaR += (now - initialTime) / timeR;

            if (targetFps <= 0 || deltaR >= 1) {
                main.input(window, scene, now - initialTime);
            }

            if (deltaUpdate >= 1) {
                long diffTimeMillis = now - updateTime;
                main.update(window, scene, diffTimeMillis);
                updateTime = now;
                deltaUpdate--;
            }

            if (targetFps <= 0 || deltaR >= 1) {
                render.render(window, scene);
                deltaR--;
                window.update();
            }
            initialTime = now;
        }

        cleanup();
    }


    private void cleanup() {
        main.cleanup();
        render.cleanup();
        scene.cleanup();
        window.cleanup();
    }

    private void resize() {
        scene.resize(window.getWidth(), window.getHeight());
    }
}
