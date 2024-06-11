package Bricker.src.bricker.brick_strategies;

import Bricker.src.bricker.gameobjects.Puck;
import Bricker.src.bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * This class is a factory for creating collision strategies in the game.
 */
public class CollisionStrategyFactory {
    private final BrickerGameManager objectCollection;

    /**
     * Constructor to initialize the CollisionStrategyFactory.
     *
     * @param objectCollection The BrickerGameManager instance that manages the game objects.
     */
    public CollisionStrategyFactory(BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
    }

    /**
     * Creates a collision strategy based on the provided base strategy and strategy number.
     *
     * @param baseStrategy The base CollisionStrategy instance to be wrapped or extended.
     * @param strategy     The strategy number that determines the type of collision strategy to create.
     * @return The created CollisionStrategy instance.
     */
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
                return doubleBehaviorHandler(baseStrategy);
            default:
                return baseStrategy;
        }
    }

    /**
     * Creates a collision strategy that combines two or more collision strategies.
     *
     * @param baseStrategy The base CollisionStrategy instance to be wrapped or extended.
     * @return The created CollisionStrategy instance that combines multiple strategies.
     */
    private CollisionStrategy doubleBehaviorHandler(CollisionStrategy baseStrategy) {
        Random rand = new Random();
        int first = rand.nextInt(4);
        int second = rand.nextInt(3);

        if (first == 4) { // triple case
            int third = rand.nextInt(3);
            int fourth = rand.nextInt(3);
            CollisionStrategy tmp = getCollisionStrategy(baseStrategy, second);
            CollisionStrategy tmp1 = getCollisionStrategy(tmp, third);
            return getCollisionStrategy(tmp1, fourth);
        } else {
            CollisionStrategy tmp = getCollisionStrategy(baseStrategy, first);
            return getCollisionStrategy(tmp, second);
        }
    }
}
