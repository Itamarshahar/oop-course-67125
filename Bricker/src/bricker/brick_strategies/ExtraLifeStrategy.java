package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraLifeStrategy implements CollisionStrategy {
    private final CollisionStrategy wrappedCollision;
    private final BrickerGameManager brickerGameManager;

    public ExtraLifeStrategy(CollisionStrategy wrappedCollision,
                             BrickerGameManager brickerGameManager) {
        this.wrappedCollision = wrappedCollision;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        wrappedCollision.onCollision(obj1,obj2);
        brickerGameManager.addHeart(obj1.getCenter());
    }
}
