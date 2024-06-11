package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.gameobjects.Puck;
import Bricker.src.bricker.main.BrickerGameManager;

import java.util.Random;

public class CollisionStrategyFactory {
    private final BrickerGameManager objectCollection;

    public CollisionStrategyFactory(BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
    }
    public CollisionStrategy getCollisionStrategy(CollisionStrategy baseStrategy,
                                                  int strategy) {
        if (baseStrategy == null) {
            baseStrategy = new BasicCollisionStrategy(this.objectCollection);
        }
        switch (strategy) {
            case 0:
                return new PuckStrategy(baseStrategy, objectCollection);
            case 1:
                return new ExtraPaddleStrategy(baseStrategy, objectCollection);
            case 2:
                return new CameraStrategy(baseStrategy, objectCollection);
            case 3:
                return new ExtraLifeStrategy(baseStrategy, objectCollection);
            case 4:
                return doubleBehaviorHandler();
//            case 5:
            default:
                return baseStrategy;
        }
    }
    private CollisionStrategy doubleBehaviorHandler(){
         Random rand = new Random();
         int first = rand.nextInt(4);
         int second = rand.nextInt(3);

         if (first == 4) { // triple
             int third = rand.nextInt(3);
             int fourth = rand.nextInt(3);
             CollisionStrategy tmp = getCollisionStrategy(null, second);
             CollisionStrategy tmp1 = getCollisionStrategy(tmp, third);
             return getCollisionStrategy(tmp1, fourth);
         }
         else {
             CollisionStrategy tmp = getCollisionStrategy(null, first);
             return getCollisionStrategy(tmp, second);
         }
//        warraped1 = getCollisionStrategy(baseStrategy, 0-4);
//            warraped2 = getCollisionStrategy(warraped1, 0-3);
    }
}


// brick = double(camera, double(heart , paddle))