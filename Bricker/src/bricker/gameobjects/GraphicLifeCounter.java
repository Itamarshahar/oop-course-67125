package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
/**
 * GraphicLifeCounter.java
 *
 * This class represents a graphical life counter in the game. It displays a
 * set of renderable objects (e.g., hearts) based on the current life count.
 */
public class GraphicLifeCounter extends GameObject {
    private Counter livesCounter;
    private int numOfLives;

    private final GameObject[] livesArray;
    private GameObjectCollection gameObjectCollection;

/**
     * Construct a new GraphicLifeCounter instance.
     *
     * @param topLeftCorner       Position of the life counter, in window
 *                            coordinates (pixels).
     *                            Note that (0,0) is the top-left corner of
 *                            the window.
     * @param dimensions          Width and height of each life icon in
 *                           window coordinates.
     * @param livesCounter        The counter for tracking the number
 *                           of lives.
     * @param renderable          The renderable representing each life icon.
     * @param gameObjectCollection The collection of game objects to
 *                             add the life icons to.
     * @param numOfLives          The initial number of lives.
     */
    public GraphicLifeCounter(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Counter livesCounter,
            Renderable renderable,
            GameObjectCollection gameObjectCollection,
            int numOfLives,
            int minY,
            BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, null);
        this.numOfLives = numOfLives;
        this.gameObjectCollection = gameObjectCollection;
        this.livesCounter = livesCounter;
        Heart heart1 = new Heart(topLeftCorner, dimensions, renderable, brickerGameManager,minY);
        Heart heart2 = new Heart(topLeftCorner.add(Vector2.RIGHT.mult(dimensions.x())),
                        dimensions, renderable, brickerGameManager,minY);
        Heart heart3 = new Heart(topLeftCorner.add(Vector2.RIGHT.mult(dimensions.x()*2)),
                        dimensions, renderable, brickerGameManager,minY);
        Heart heart4 =
                new Heart(topLeftCorner.add(Vector2.RIGHT.mult(dimensions.x()*3)),
                        dimensions, renderable, brickerGameManager,minY);

        this.livesArray = new GameObject[]{heart1,heart2, heart3, heart4};
        for (int i = 0; i < numOfLives; i++){
            gameObjectCollection.addGameObject(livesArray[i], Layer.UI);
        }

    }

    /**
     * Updates the graphical representation of the life counter based on the current
     * life count. Adds or removes life icons as necessary.
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives){
            gameObjectCollection.removeGameObject(livesArray[livesCounter.value()], Layer.UI);
            numOfLives--;
        }
        if (livesCounter.value() > numOfLives){
            gameObjectCollection.addGameObject(livesArray[numOfLives], Layer.UI);
            numOfLives++;
        }
    }
}

