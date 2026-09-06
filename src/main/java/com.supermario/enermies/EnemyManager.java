package com.supermario.enermies;

import com.supermario.world.TileCollision;
import com.supermario.world.TileMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Owns every active Enemy. The game loop only talks to this class instead
 * of individual enemy instances — matches the design doc's spawning and
 * "remove dead objects" steps (stages 3 and 4).
 */
public class EnemyManager {

    private final List<Enemy> enemies = new ArrayList<>();

    public void spawn(Enemy enemy) {
        enemies.add(enemy);
    }

    /**
     * Updates every enemy: AI movement, gravity + ground collision, and a
     * temporary screen-edge bounce standing in for real wall detection.
     * Dead enemies are removed at the end of the frame.
     */
    public void update(double delta, TileMap tileMap, int screenWidth) {
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();

            enemy.update(delta);
            TileCollision.resolveVertical(enemy, tileMap, delta);

            // Temporary: bounce off screen edges. Replace with real wall/ledge
            // tile checks once the map has more than a flat floor.
            if (enemy.getX() <= 0 || enemy.getX() + enemy.getWidth() >= screenWidth) {
                enemy.reverseDirection();
            }

            if (!enemy.isAlive()) {
                it.remove();
            }
        }
    }

    public void render() {
        for (Enemy enemy : enemies) {
            enemy.render();
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}