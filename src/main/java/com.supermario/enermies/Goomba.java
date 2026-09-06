package com.supermario.enermies;

import static org.lwjgl.opengl.GL11.*;

public class Goomba extends Enemy {

    private static final double SPEED = 60.0;

    public Goomba(double x, double y) {
        super(x, y, 32, 32);

        // Goomba starts moving left
        this.velocityX = SPEED * direction;
    }

    @Override
    public void update(double delta) {

        if (!alive) {
            return;
        }

        // Move left / right
        x += velocityX * delta;
    }

    @Override
    public void render() {

        if (!alive) {
            return;
        }

        /*
         * ==========================================
         * GOOMBA PIXEL ART
         * ==========================================
         *
         * 32 x 32 pixels
         *
         *          ████████████
         *       ████████████████
         *      ███  ████████  ███
         *      ██  ●  ████  ●  ██
         *      ██    ████    ██
         *       ███████████████
         *        ██████████████
         *       ████  ████  ████
         *      ████          ████
         *      ██              ██
         *       ███          ███
         *
         * ==========================================
         */

        double px = x;
        double py = y;

        // ------------------------------------------------
        // 1. Body / Head
        // ------------------------------------------------

        glColor3f(
                0.70f,
                0.30f,
                0.08f
        );

        // Main head
        quad(
                px + 5,
                py + 5,
                22,
                17
        );

        // Top part
        quad(
                px + 9,
                py + 2,
                14,
                5
        );

        quad(
                px + 6,
                py + 4,
                20,
                5
        );

        // Left side
        quad(
                px + 3,
                py + 9,
                5,
                10
        );

        // Right side
        quad(
                px + 24,
                py + 9,
                5,
                10
        );

        // ------------------------------------------------
        // 2. Dark brown outline
        // ------------------------------------------------

        glColor3f(
                0.20f,
                0.07f,
                0.02f
        );

        // Top outline
        quad(
                px + 9,
                py + 1,
                14,
                2
        );

        quad(
                px + 6,
                py + 3,
                20,
                2
        );

        // Left outline
        quad(
                px + 3,
                py + 8,
                3,
                11
        );

        // Right outline
        quad(
                px + 26,
                py + 8,
                3,
                11
        );

        // Bottom head outline
        quad(
                px + 7,
                py + 20,
                18,
                3
        );

        // ------------------------------------------------
        // 3. Eyes
        // ------------------------------------------------

        glColor3f(
                1.0f,
                1.0f,
                1.0f
        );

        // Left eye
        quad(
                px + 8,
                py + 9,
                6,
                6
        );

        // Right eye
        quad(
                px + 18,
                py + 9,
                6,
                6
        );

        // ------------------------------------------------
        // 4. Pupils
        // ------------------------------------------------

        glColor3f(
                0.02f,
                0.02f,
                0.02f
        );

        quad(
                px + 10,
                py + 10,
                3,
                5
        );

        quad(
                px + 19,
                py + 10,
                3,
                5
        );

        // ------------------------------------------------
        // 5. Eyebrows
        // ------------------------------------------------

        quad(
                px + 8,
                py + 7,
                7,
                2
        );

        quad(
                px + 17,
                py + 7,
                7,
                2
        );

        // ------------------------------------------------
        // 6. Mouth
        // ------------------------------------------------

        glColor3f(
                0.05f,
                0.02f,
                0.01f
        );

        quad(
                px + 10,
                py + 17,
                12,
                3
        );

        // ------------------------------------------------
        // 7. Feet
        // ------------------------------------------------

        glColor3f(
                0.20f,
                0.07f,
                0.02f
        );

        // Left foot
        quad(
                px + 2,
                py + 23,
                12,
                7
        );

        // Right foot
        quad(
                px + 18,
                py + 23,
                12,
                7
        );

        // ------------------------------------------------
        // 8. Foot highlights
        // ------------------------------------------------

        glColor3f(
                0.45f,
                0.16f,
                0.04f
        );

        quad(
                px + 4,
                py + 23,
                8,
                3
        );

        quad(
                px + 20,
                py + 23,
                8,
                3
        );

        // Reset OpenGL color
        glColor3f(
                1.0f,
                1.0f,
                1.0f
        );
    }

    // =====================================================
    // DRAW PIXEL RECTANGLE
    // =====================================================

    private void quad(
            double x,
            double y,
            double width,
            double height
    ) {

        glBegin(GL_QUADS);

        glVertex2d(x, y);
        glVertex2d(x + width, y);
        glVertex2d(x + width, y + height);
        glVertex2d(x, y + height);

        glEnd();
    }
}