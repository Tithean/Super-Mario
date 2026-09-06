package com.supermario.GUI;

import com.supermario.enermies.EnemyManager;
import com.supermario.world.TileMap;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width;
    private int height;
    private String title;

    private static Window window = null;

    private long glfwWindow;

    private EnemyManager enemyManager;
    private TileMap tileMap;

    private double lastTime;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    private Window() {

        this.width = 1920;
        this.height = 1080;

        this.title = "Super Mario";
    }

    // =========================================================
    // SINGLETON
    // =========================================================

    public static Window get() {

        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    // =========================================================
    // ENEMY MANAGER
    // =========================================================

    public void setEnemyManager(EnemyManager enemyManager) {

        this.enemyManager = enemyManager;
    }

    // =========================================================
    // RUN
    // =========================================================

    public void run() {

        System.out.println(
                "LWJGL " + Version.getVersion()
        );

        init();

        loop();

        destroy();
    }

    // =========================================================
    // INIT
    // =========================================================

    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {

            throw new IllegalStateException(
                    "Unable to initialize GLFW"
            );
        }

        // -----------------------------------------------------
        // GLFW Window Settings
        // -----------------------------------------------------

        glfwDefaultWindowHints();

        glfwWindowHint(
                GLFW_VISIBLE,
                GLFW_FALSE
        );

        glfwWindowHint(
                GLFW_RESIZABLE,
                GLFW_TRUE
        );

        glfwWindowHint(
                GLFW_MAXIMIZED,
                GLFW_TRUE
        );

        // -----------------------------------------------------
        // Create Window
        // -----------------------------------------------------

        glfwWindow = glfwCreateWindow(
                width,
                height,
                title,
                NULL,
                NULL
        );

        if (glfwWindow == NULL) {

            throw new IllegalStateException(
                    "Unable to create GLFW window"
            );
        }

        // -----------------------------------------------------
        // OpenGL Context
        // -----------------------------------------------------

        glfwMakeContextCurrent(glfwWindow);

        // V-Sync
        glfwSwapInterval(1);

        // Show Window
        glfwShowWindow(glfwWindow);

        // Create OpenGL capabilities
        GL.createCapabilities();

        // -----------------------------------------------------
        // Projection
        // -----------------------------------------------------

        glMatrixMode(GL_PROJECTION);

        glLoadIdentity();

        glOrtho(
                0,
                width,
                height,
                0,
                -1,
                1
        );

        glMatrixMode(GL_MODELVIEW);

        glLoadIdentity();

        // -----------------------------------------------------
        // Enable Transparency
        // -----------------------------------------------------

        glEnable(GL_BLEND);

        glBlendFunc(
                GL_SRC_ALPHA,
                GL_ONE_MINUS_SRC_ALPHA
        );

        // -----------------------------------------------------
        // Create World 1-1
        // -----------------------------------------------------

        /*
         * 80 columns
         * 34 rows
         *
         * Tile size = 32
         *
         * Map width  = 80 * 32 = 2560 px
         * Map height = 34 * 32 = 1088 px
         */

        tileMap = TileMap.createWorld1_1(
                80,
                34
        );

        // -----------------------------------------------------
        // Start Timer
        // -----------------------------------------------------

        lastTime = glfwGetTime();
    }

    // =========================================================
    // GAME LOOP
    // =========================================================

    public void loop() {

        while (!glfwWindowShouldClose(glfwWindow)) {

            // -------------------------------------------------
            // Delta Time
            // -------------------------------------------------

            double currentTime = glfwGetTime();

            double delta =
                    currentTime - lastTime;

            lastTime = currentTime;

            /*
             * Prevent a huge physics jump if the game
             * freezes for a moment.
             */
            if (delta > 0.1) {
                delta = 0.1;
            }

            // -------------------------------------------------
            // Events
            // -------------------------------------------------

            glfwPollEvents();

            // -------------------------------------------------
            // Clear Screen
            // -------------------------------------------------

            glClearColor(
                    0.25f,
                    0.55f,
                    1.0f,
                    1.0f
            );

            glClear(
                    GL_COLOR_BUFFER_BIT
            );

            // -------------------------------------------------
            // Update
            // -------------------------------------------------

            update(delta);

            // -------------------------------------------------
            // Render
            // -------------------------------------------------

            render();

            // -------------------------------------------------
            // Display
            // -------------------------------------------------

            glfwSwapBuffers(glfwWindow);
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private void update(double delta) {

        if (enemyManager != null) {

            enemyManager.update(
                    delta,
                    tileMap,
                    width
            );
        }
    }

    // =========================================================
    // RENDER
    // =========================================================

    private void render() {

        /*
         * Render background + map
         */
        tileMap.render();

        /*
         * Render enemies
         */
        if (enemyManager != null) {

            enemyManager.render();
        }
    }

    // =========================================================
    // DESTROY
    // =========================================================

    private void destroy() {

        glfwFreeCallbacks(
                glfwWindow
        );

        glfwDestroyWindow(
                glfwWindow
        );

        glfwTerminate();

        glfwSetErrorCallback(null);

    }
}