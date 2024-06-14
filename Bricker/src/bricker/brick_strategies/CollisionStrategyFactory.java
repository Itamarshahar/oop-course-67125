package bricker.brick_strategies;

import bricker.main.BrickerGameManager;

import java.util.Random;

/**
 * This class is a factory for creating collision strategies in the game.
 */
public class CollisionStrategyFactory {
    private final BrickerGameManager objectCollection;


    /**
     * Constructor to initialize the CollisionStrategyFactory.
     *
     * @param objectCollection The BrickerGameManager instance that manages
     *                         the game objects.
     */
    public CollisionStrategyFactory(BrickerGameManager objectCollection) {
        this.objectCollection = objectCollection;
    }

    /**
     * Creates a collision strategy based on the provided base strategy and
     * strategy number.
     *
     * @param baseStrategy The base CollisionStrategy instance to be wrapped
     *                     or extended.
     * @param strategy     The strategy number that determines the type of
     *                     collision strategy to create.
     * @return The created CollisionStrategy instance.
     */
    public CollisionStrategy getCollisionStrategy(CollisionStrategy
                                                          baseStrategy,
                                                  int strategy) {
        if (baseStrategy == null) {
            baseStrategy = new BasicCollisionStrategy(this.objectCollection);
        }
        return switch (strategy) {
            case 0 -> new PuckStrategy(baseStrategy, objectCollection);
            case 1 -> new ExtraPaddleStrategy(baseStrategy, objectCollection);
            case 2 -> new CameraStrategy(baseStrategy, objectCollection);
            case 3 -> new ExtraLifeStrategy(baseStrategy, objectCollection);
            case 4 -> doubleBehaviorHandler(baseStrategy);
            default -> baseStrategy;
        };
    }

    /**
     * Creates a collision strategy that combines two or more collision
     * strategies.
     *
     * @param baseStrategy The base CollisionStrategy instance to be wrapped
     *                     or extended.
     *
     * @return The created CollisionStrategy instance that combines multiple
     * strategies.
     */
    private CollisionStrategy doubleBehaviorHandler(CollisionStrategy
                                                            baseStrategy) {
        Random rand = new Random();
        int first = rand.nextInt(5);
        int second = rand.nextInt(4);

        if (first == 4) { // triple case
            int third = rand.nextInt(4);
            int fourth = rand.nextInt(4);
            CollisionStrategy tmp = getCollisionStrategy(baseStrategy, second);
            CollisionStrategy tmp1 = getCollisionStrategy(tmp, third);
            return getCollisionStrategy(tmp1, fourth);
        } else {
            CollisionStrategy tmp = getCollisionStrategy(baseStrategy, first);
            return getCollisionStrategy(tmp, second);
        }
    }
}
