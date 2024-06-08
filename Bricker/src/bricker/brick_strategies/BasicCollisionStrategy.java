package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class BasicCollisionStrategy implements CollisionStrategy {

    protected Counter bricksCounter;
    protected BrickerGameManager objectCollection;

    public BasicCollisionStrategy(BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
        this.bricksCounter = objectCollection.getBrickCounter();
    }

    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        if (objectCollection.removeGameObjectHandler(obj1)) {
            bricksCounter.decrement();
        }
    }
}
