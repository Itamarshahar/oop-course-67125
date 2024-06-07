package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

public class BasicCollisionStrategy implements CollisionStrategy {

    protected GameObjectCollection objectCollection;

    public BasicCollisionStrategy(GameObjectCollection objectCollection) {
        this.objectCollection = objectCollection;

    }

    public  void onCollision(GameObject obj1, GameObject obj2){
        objectCollection.removeGameObject(obj1);
    }
}
