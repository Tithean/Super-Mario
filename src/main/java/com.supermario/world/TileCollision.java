package com.supermario.world;

import com.supermario.entities.Entity;

/**
 * Handles entity-vs-tile vertical collision.
 *
 * Responsibilities:
 *  - Gravity
 *  - Falling
 *  - Landing on solid tiles
 *  - Hitting ceilings
 *
 * Collision logic is independent from OpenGL rendering.
 */
public final class TileCollision {

    private TileCollision() {
        // Utility class
    }

    /**
     * Gravity in pixels per second squared.
     */
    public static final double GRAVITY = 900.0;

    /**
     * Maximum falling speed.
     */
    public static final double MAX_FALL_SPEED = 700.0;

    /**
     * Resolves vertical movement and collision.
     *
     * @return true when the entity is standing on solid ground.
     */
    public static boolean resolveVertical(
            Entity entity,
            TileMap map,
            double delta
    ) {

        // -----------------------------------------------------
        // 1. Apply gravity
        // -----------------------------------------------------

        double velocityY =
                entity.getVelocityY()
                        + GRAVITY * delta;

        // Prevent extremely fast falling
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }

        entity.setVelocityY(velocityY);

        // -----------------------------------------------------
        // 2. Move vertically
        // -----------------------------------------------------

        double oldY = entity.getY();

        double newY =
                oldY + velocityY * delta;

        entity.setY(newY);

        boolean grounded = false;

        // -----------------------------------------------------
        // 3. Falling
        // -----------------------------------------------------

        if (velocityY >= 0) {

            double leftX =
                    entity.getX() + 2;

            double rightX =
                    entity.getX()
                            + entity.getWidth()
                            - 2;

            double feetY =
                    entity.getY()
                            + entity.getHeight();

            /*
             * Check both left and right sides of the feet.
             *
             * This is safer than checking only the center.
             */
            if (
                    map.isSolidAtPixel(leftX, feetY)
                            || map.isSolidAtPixel(rightX, feetY)
            ) {

                int row =
                        (int) Math.floor(
                                feetY / TileMap.TILE_SIZE
                        );

                double groundY =
                        row * TileMap.TILE_SIZE;

                entity.setY(
                        groundY - entity.getHeight()
                );

                entity.setVelocityY(0);

                grounded = true;
            }
        }

        // -----------------------------------------------------
        // 4. Moving upward / hitting ceiling
        // -----------------------------------------------------

        else {

            double leftX =
                    entity.getX() + 2;

            double rightX =
                    entity.getX()
                            + entity.getWidth()
                            - 2;

            double headY =
                    entity.getY();

            if (
                    map.isSolidAtPixel(leftX, headY)
                            || map.isSolidAtPixel(rightX, headY)
            ) {

                int row =
                        (int) Math.floor(
                                headY / TileMap.TILE_SIZE
                        );

                double ceilingY =
                        (row + 1)
                                * TileMap.TILE_SIZE;

                entity.setY(ceilingY);

                entity.setVelocityY(0);
            }
        }

        return grounded;
    }
}