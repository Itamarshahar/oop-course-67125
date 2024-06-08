package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.gameobjects.Puck;
import Bricker.src.bricker.main.BrickerGameManager;

public class CollisionStrategyFactory {
    private final BrickerGameManager objectCollection;

    public CollisionStrategyFactory(BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
    }


    public CollisionStrategy getCollisionStrategy(CollisionStrategy baseStrategy,
                                                  int strategy) {

        if (baseStrategy == null) {
            baseStrategy = new BasicCollisionStrategy(objectCollection);
        }
        switch (strategy) {
            case 0:
                return new PuckStrategy(baseStrategy, objectCollection);

            default:
                return baseStrategy;
        }
    }
}
