package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

public class PuckStrategy implements CollisionStrategy {
    private final CollisionStrategy baseStrategy;
    private final BrickerGameManager objectCollection;

    public PuckStrategy(CollisionStrategy baseStrategy, BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
        this.baseStrategy = baseStrategy;
    }

    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        baseStrategy.onCollision(obj1, obj2);
        objectCollection.createPuck(obj1.getCenter());
        objectCollection.createPuck(obj1.getCenter());
    }
}
