package com.supermario.world;

import static org.lwjgl.opengl.GL11.*;

public class TileMap {

    public static final int TILE_SIZE = 32;

    // Tile IDs
    public static final int AIR      = 0;
    public static final int GROUND   = 1;
    public static final int GRASS    = 2;
    public static final int BRICK    = 3;
    public static final int QUESTION = 4;
    public static final int PIPE     = 5;
    public static final int LADDER   = 6;
    public static final int COIN     = 7;

    private final int[][] tiles;
    private final int rows;
    private final int cols;

    public TileMap(int[][] tiles) {
        this.tiles = tiles;
        this.rows = tiles.length;
        this.cols = tiles[0].length;
    }

    // =========================================================
    // WORLD 1-1 MAP
    // =========================================================

    public static TileMap createWorld1_1(int cols, int rows) {

        int[][] data = new int[rows][cols];

        int groundRow = rows - 4;

        // -----------------------------------------------------
        // Ground
        // -----------------------------------------------------

        for (int r = groundRow; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                data[r][c] = GROUND;
            }
        }

        // Top grass
        for (int c = 0; c < cols; c++) {
            data[groundRow][c] = GRASS;
        }

        // -----------------------------------------------------
        // Question blocks
        // -----------------------------------------------------

        put(data, 10, groundRow - 4, QUESTION);

        put(data, 17, groundRow - 8, QUESTION);

        put(data, 5, groundRow - 4, QUESTION);

        // -----------------------------------------------------
        // Brick platform
        // -----------------------------------------------------

        put(data, 30, groundRow - 4, BRICK);
        put(data, 31, groundRow - 4, QUESTION);
        put(data, 32, groundRow - 4, BRICK);
        put(data, 33, groundRow - 4, BRICK);
        put(data, 34, groundRow - 4, BRICK);

        // -----------------------------------------------------
        // Coins
        // -----------------------------------------------------

        put(data, 26, groundRow - 5, COIN);
        put(data, 28, groundRow - 5, COIN);
        put(data, 30, groundRow - 5, COIN);

        // -----------------------------------------------------
        // Small brick platform
        // -----------------------------------------------------

        put(data, 20, groundRow - 2, BRICK);
        put(data, 21, groundRow - 2, BRICK);
        put(data, 22, groundRow - 2, BRICK);

        // -----------------------------------------------------
        // Pipe
        // -----------------------------------------------------

        put(data, 39, groundRow - 2, PIPE);
        put(data, 39, groundRow - 1, PIPE);

        // -----------------------------------------------------
        // Ladder
        // -----------------------------------------------------

        put(data, 15, groundRow - 2, LADDER);
        put(data, 16, groundRow - 2, LADDER);

        return new TileMap(data);
    }

    private static void put(int[][] data, int col, int row, int tile) {

        if (row >= 0 &&
                row < data.length &&
                col >= 0 &&
                col < data[0].length) {

            data[row][col] = tile;
        }
    }

    // =========================================================
    // COLLISION
    // =========================================================

    public boolean isSolid(int col, int row) {

        if (row < 0 ||
                row >= rows ||
                col < 0 ||
                col >= cols) {

            return false;
        }

        int tile = tiles[row][col];

        return tile == GROUND
                || tile == GRASS
                || tile == BRICK
                || tile == QUESTION
                || tile == PIPE;
    }

    public boolean isSolidAtPixel(double px, double py) {

        int col = (int) Math.floor(px / TILE_SIZE);
        int row = (int) Math.floor(py / TILE_SIZE);

        return isSolid(col, row);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getPixelWidth() {
        return cols * TILE_SIZE;
    }

    public int getPixelHeight() {
        return rows * TILE_SIZE;
    }

    // =========================================================
    // RENDER
    // =========================================================

    public void render() {

        // -----------------------------
        // Sky
        // -----------------------------

        glColor3f(0.25f, 0.55f, 1.0f);

        glBegin(GL_QUADS);

        glVertex2d(0, 0);
        glVertex2d(getPixelWidth(), 0);
        glVertex2d(getPixelWidth(), getPixelHeight());
        glVertex2d(0, getPixelHeight());

        glEnd();

        // -----------------------------
        // Background hills
        // -----------------------------

        renderHill(5, rows - 5, 6);
        renderHill(32, rows - 5, 4);
        renderHill(50, rows - 5, 5);

        // -----------------------------
        // Bushes
        // -----------------------------

        renderBush(12, rows - 5);
        renderBush(27, rows - 5);
        renderBush(44, rows - 5);

        // -----------------------------
        // Clouds
        // -----------------------------

        renderCloud(7, 5);
        renderCloud(24, 4);
        renderCloud(43, 6);

        // -----------------------------
        // Tiles
        // -----------------------------

        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < cols; c++) {

                int tile = tiles[r][c];

                if (tile == AIR) {
                    continue;
                }

                double x = c * TILE_SIZE;
                double y = r * TILE_SIZE;

                switch (tile) {

                    case GROUND:
                        renderGround(x, y);
                        break;

                    case GRASS:
                        renderGrass(x, y);
                        break;

                    case BRICK:
                        renderBrick(x, y);
                        break;

                    case QUESTION:
                        renderQuestion(x, y);
                        break;

                    case PIPE:
                        renderPipe(x, y);
                        break;

                    case LADDER:
                        renderLadder(x, y);
                        break;

                    case COIN:
                        renderCoin(x, y);
                        break;
                }
            }
        }

        glColor3f(1, 1, 1);
    }

    // =========================================================
    // GROUND
    // =========================================================

    private void renderGround(double x, double y) {

        glColor3f(0.70f, 0.28f, 0.07f);

        quad(x, y, TILE_SIZE, TILE_SIZE);

        // Brick lines

        glColor3f(0.15f, 0.08f, 0.03f);

        glBegin(GL_LINES);

        glVertex2d(x, y + 16);
        glVertex2d(x + TILE_SIZE, y + 16);

        glVertex2d(x + 16, y);
        glVertex2d(x + 16, y + 16);

        glVertex2d(x + 8, y + 16);
        glVertex2d(x + 8, y + TILE_SIZE);

        glVertex2d(x + 24, y + 16);
        glVertex2d(x + 24, y + TILE_SIZE);

        glEnd();
    }

    // =========================================================
    // GRASS
    // =========================================================

    private void renderGrass(double x, double y) {

        glColor3f(0.65f, 0.30f, 0.08f);

        quad(x, y, TILE_SIZE, TILE_SIZE);

        // Green top

        glColor3f(0.10f, 0.75f, 0.10f);

        quad(x, y, TILE_SIZE, 7);

        // Grass line

        glColor3f(0.05f, 0.45f, 0.05f);

        quad(x, y + 7, TILE_SIZE, 3);
    }

    // =========================================================
    // BRICK
    // =========================================================

    private void renderBrick(double x, double y) {

        glColor3f(0.75f, 0.30f, 0.05f);

        quad(x, y, TILE_SIZE, TILE_SIZE);

        glColor3f(0.15f, 0.06f, 0.01f);

        glBegin(GL_LINES);

        glVertex2d(x, y + 10);
        glVertex2d(x + TILE_SIZE, y + 10);

        glVertex2d(x, y + 21);
        glVertex2d(x + TILE_SIZE, y + 21);

        glVertex2d(x + 16, y);
        glVertex2d(x + 16, y + 10);

        glVertex2d(x + 8, y + 10);
        glVertex2d(x + 8, y + 21);

        glVertex2d(x + 24, y + 10);
        glVertex2d(x + 24, y + 21);

        glVertex2d(x + 16, y + 21);
        glVertex2d(x + 16, y + TILE_SIZE);

        glEnd();
    }

    // =========================================================
    // QUESTION
    // =========================================================

    private void renderQuestion(double x, double y) {

        glColor3f(1.0f, 0.60f, 0.05f);

        quad(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);

        glColor3f(0.25f, 0.10f, 0.01f);

        glLineWidth(3);

        // Question mark

        glBegin(GL_LINE_STRIP);

        glVertex2d(x + 11, y + 10);
        glVertex2d(x + 15, y + 7);
        glVertex2d(x + 21, y + 8);
        glVertex2d(x + 23, y + 12);
        glVertex2d(x + 20, y + 16);
        glVertex2d(x + 16, y + 19);

        glEnd();

        quad(x + 15, y + 23, 4, 4);
    }

    // =========================================================
    // PIPE
    // =========================================================

    private void renderPipe(double x, double y) {

        glColor3f(0.10f, 0.75f, 0.05f);

        quad(x + 3, y, TILE_SIZE - 6, TILE_SIZE);

        // Pipe highlight

        glColor3f(0.35f, 1.0f, 0.10f);

        quad(x + 6, y, 5, TILE_SIZE);
    }

    // =========================================================
    // LADDER
    // =========================================================

    private void renderLadder(double x, double y) {

        glColor3f(0.65f, 0.32f, 0.12f);

        quad(x + 5, y, 5, TILE_SIZE);
        quad(x + 22, y, 5, TILE_SIZE);

        for (int i = 5; i < TILE_SIZE; i += 9) {
            quad(x + 5, y + i, 22, 4);
        }
    }

    // =========================================================
    // COIN
    // =========================================================

    private void renderCoin(double x, double y) {

        glColor3f(1.0f, 0.80f, 0.05f);

        glBegin(GL_QUADS);

        glVertex2d(x + 12, y + 4);
        glVertex2d(x + 20, y + 4);
        glVertex2d(x + 24, y + 16);
        glVertex2d(x + 20, y + 28);
        glVertex2d(x + 12, y + 28);
        glVertex2d(x + 8, y + 16);

        glEnd();

        glColor3f(1.0f, 1.0f, 0.4f);

        quad(x + 12, y + 8, 4, 16);
    }

    // =========================================================
    // HILL
    // =========================================================

    private void renderHill(int tileX, int groundTileY, int size) {

        double x = tileX * TILE_SIZE;
        double baseY = groundTileY * TILE_SIZE;
        double width = size * TILE_SIZE;
        double height = size * TILE_SIZE;

        glColor3f(0.10f, 0.70f, 0.08f);

        glBegin(GL_TRIANGLES);

        glVertex2d(x, baseY);
        glVertex2d(x + width / 2, baseY - height);
        glVertex2d(x + width, baseY);

        glEnd();

        // small dark grass marks

        glColor3f(0.02f, 0.25f, 0.02f);

        quad(x + width * 0.45, baseY - height * 0.45, 5, 10);
        quad(x + width * 0.60, baseY - height * 0.30, 5, 10);
    }

    // =========================================================
    // BUSH
    // =========================================================

    private void renderBush(int tileX, int groundTileY) {

        double x = tileX * TILE_SIZE;
        double y = groundTileY * TILE_SIZE;

        glColor3f(0.25f, 0.75f, 0.05f);

        glBegin(GL_TRIANGLES);

        glVertex2d(x, y);
        glVertex2d(x + 20, y - 25);
        glVertex2d(x + 40, y);

        glVertex2d(x + 25, y);
        glVertex2d(x + 50, y - 35);
        glVertex2d(x + 75, y);

        glEnd();

        glColor3f(0.15f, 0.55f, 0.03f);

        quad(x + 10, y - 10, 5, 7);
        quad(x + 48, y - 15, 5, 7);
    }

    // =========================================================
    // CLOUD
    // =========================================================

    private void renderCloud(int tileX, int tileY) {

        double x = tileX * TILE_SIZE;
        double y = tileY * TILE_SIZE;

        glColor3f(1.0f, 1.0f, 1.0f);

        circle(x + 15, y + 20, 15);
        circle(x + 32, y + 12, 20);
        circle(x + 52, y + 20, 16);
        circle(x + 35, y + 25, 20);
    }

    private void circle(double cx, double cy, double radius) {

        glBegin(GL_TRIANGLE_FAN);

        glVertex2d(cx, cy);

        for (int i = 0; i <= 20; i++) {

            double angle = Math.PI * 2 * i / 20;

            glVertex2d(
                    cx + Math.cos(angle) * radius,
                    cy + Math.sin(angle) * radius
            );
        }

        glEnd();
    }

    // =========================================================
    // QUAD
    // =========================================================

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