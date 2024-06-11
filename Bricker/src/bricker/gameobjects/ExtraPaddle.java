package Bricker.src.bricker.gameobjects;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

public class ExtraPaddle extends Paddle{
    private final GameObjectCollection gameObjectCollection;
    private final Counter collisionCounter;
    private int paddleLives = 4;

    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable, UserInputListener inputListener, int wallWidth, Vector2 windowDimension
                        ,GameObjectCollection gameObjectCollection){
        super(topLeftCorner,dimensions,renderable,inputListener,wallWidth,windowDimension);
        this.collisionCounter = new Counter();
        this.gameObjectCollection = gameObjectCollection;


    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other instanceof Ball){
            collisionCounter.increment();
        }
        if(collisionCounter.value() == paddleLives){
            gameObjectCollection.removeGameObject(this);
        }

    }


}