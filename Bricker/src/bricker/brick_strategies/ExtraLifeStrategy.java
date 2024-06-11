package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraLifeStrategy implements CollisionStrategy {



    private final CollisionStrategy wrapped;
    private final BrickerGameManager brickerGameManager;

    public ExtraLifeStrategy(CollisionStrategy wrapped,
                             BrickerGameManager brickerGameManager) {
        this.wrapped = wrapped;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {
        wrapped.onCollision(obj1,obj2);
        brickerGameManager.addHeart(obj1.getCenter());

    }
}
