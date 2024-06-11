package Bricker.src.bricker.brick_strategies;
import Bricker.src.bricker.main.BrickerGameManager;
import danogl.GameObject;

public class ExtraPaddleStrategy implements CollisionStrategy {
    private final CollisionStrategy wrappedCollision;
    private final BrickerGameManager objectCollection;

    ExtraPaddleStrategy(CollisionStrategy wrappedCollision,
                        BrickerGameManager objectCollection){
        this.wrappedCollision = wrappedCollision;
        this.objectCollection = objectCollection;
    }
    @Override
    public void onCollision(GameObject obj1, GameObject obj2) {

    }
}
