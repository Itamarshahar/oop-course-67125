package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraPaddleStrategy implements CollisionStrategy {
    private final CollisionStrategy wrappedCollision;
    private final BrickerGameManager brickerGameManager;

    ExtraPaddleStrategy(CollisionStrategy wrappedCollision,
                        BrickerGameManager objectCollection) {
        this.wrappedCollision = wrappedCollision;
        this.brickerGameManager = objectCollection;
    }

    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        wrappedCollision.onCollision(obj1, obj2);
        brickerGameManager.addExtraPaddle();
    }
}
