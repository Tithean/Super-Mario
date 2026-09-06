package com.supermario.enermies;

import com.supermario.entities.Entity;

/**
 * Common base for all enemy types (Goomba, Koopa, PiranhaPlant, ...).
 * Concrete subclasses only need to implement update()/render() and
 * react to state changes relevant to them.
 */
public abstract class Enemy extends Entity {

    public enum EnemyState {
        WALKING,
        STOMPED,
        SHELL,
        FLIPPED,
        DEAD
    }

    protected EnemyState state = EnemyState.WALKING;
    protected int direction = -1; // -1 = left, 1 = right

    public Enemy(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public EnemyState getState() { return state; }
    public void setState(EnemyState state) { this.state = state; }

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    /**
     * Generic wall-bounce: flips direction and horizontal velocity.
     * Works for any subclass without needing to know its speed constant.
     * Override if a subclass needs different behavior on hitting a wall.
     */
    public void reverseDirection() {
        direction *= -1;
        velocityX = -velocityX;
    }

    /** Called by collision logic when Mario stomps on this enemy. */
    public void onStomped() {
        state = EnemyState.STOMPED;
        velocityX = 0;
    }
}