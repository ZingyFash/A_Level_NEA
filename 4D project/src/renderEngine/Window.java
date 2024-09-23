package renderEngine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;
import org.tinylog.Logger;

import java.util.concurrent.Callable;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final long handle;
    private int width;
    private int height;
    private final Callable<Void> resizeFunc;


    public Window(String title, WindowOptions opts, Callable<Void> resizeFunc) {
        this.resizeFunc = resizeFunc;
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        if (opts.compatibleProfile) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        }

        if (opts.width > 0 && opts.height > 3) {
            this.width = opts.width;
            this.height = opts.height;
        } else {
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            width = vidMode.width();
            height = vidMode.height();
        }

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetFramebufferSizeCallback(handle, (window, w, h) -> resize(w, h));

        glfwSetErrorCallback((int errCode, long msgPtr) ->
                Logger.error("Error code [{}], message [{}].", errCode, MemoryUtil.memUTF8(msgPtr))
        );

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            keyCallBack(key, action);
        });


        glfwMakeContextCurrent(handle);

        if (opts.fps > 0) {
            glfwSwapInterval(0);
        } else {
            glfwSwapInterval(1);
        }

        glfwShowWindow(handle);

        int[] arrWidth = new int[1];
        int[] arrHeight = new int[1];

        glfwGetFramebufferSize(handle, arrWidth, arrHeight);
        this.width = arrWidth[0];
        this.height = arrHeight[0];
    }

    private void resize(int w, int h) {
        this.width = w;
        this.height = h;
        try {
            resizeFunc.call();
        } catch (Exception e) {
            Logger.error("Error calling resize callback.", e);
        }
    }

    public void update() {
        glfwSwapBuffers(handle);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(handle);
    }

    private void keyCallBack(int key, int action) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            // Closes window if escape key is released
            glfwSetWindowShouldClose(handle, true); // We will detect this in the rendering loop
        }
    }

    public void cleanup() {
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null) {
            callback.free();
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public long getHandle() { return handle; }

    public boolean isKeyPressed(int key) { return (glfwGetKey(handle, key) == GLFW_PRESS); }

    public void pollEvents() { glfwPollEvents(); }

    public static class WindowOptions {
        public int fps;
        public int ups = Engine.TARGET_UPS;
        public int width;
        public int height;
        public boolean compatibleProfile;
    }
}
