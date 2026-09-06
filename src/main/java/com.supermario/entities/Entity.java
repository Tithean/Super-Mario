package com.supermario.entities;

import java.awt.Rectangle; // used only as a lightweight math helper for hitboxes, not for drawing

/**
 * Base class for every object that lives in the game world
 * (player, enemies, items, etc). Provides shared physics state
 * so subclasses only need to implement behavior.
 */
public abstract class Entity {

    protected double x;          // position X
    protected double y;          // position Y
    protected double velocityX;  // horizontal speed
    protected double velocityY;  // vertical speed
    protected int width;
    protected int height;
    protected boolean alive = true;

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** Rebuilds the hitbox from current position each time it's requested. */
    public Rectangle getHitbox() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public abstract void update(double delta);

    /**
     * Draws this entity using the currently bound OpenGL context.
     * No Graphics2D here — GL calls act on global state, they aren't
     * passed a context object like AWT/Swing.
     */
    public abstract void render();

    // --- getters / setters ---
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}