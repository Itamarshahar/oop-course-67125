package Bricker.src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
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
    private final Renderable livesImage;
    private final int maxNumLives;
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private Counter livesCounter;
    private GameObjectCollection gameObjectsCollection;
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
     * @param livesImage          The renderable representing each life
 *                            icon when displayed.
     * @param maxNumLives         The maximum number of lives allowed.
     */
    public GraphicLifeCounter(
            Vector2 topLeftCorner,
            Vector2 dimensions,
            Counter livesCounter,
            Renderable renderable,
            GameObjectCollection gameObjectCollection,
            int numOfLives,
            Renderable livesImage,
            int maxNumLives) {
        super(topLeftCorner, dimensions, null);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.numOfLives = numOfLives;
        this.gameObjectCollection = gameObjectCollection;
        this.livesImage = livesImage;
        this.maxNumLives = maxNumLives;
        this.livesArray = new GameObject[maxNumLives];
        this.livesCounter = livesCounter;

        for (int i = 0; i < numOfLives; i++) {
            if (i < livesCounter.value())
                livesArray[i] = new GameObject(new Vector2(topLeftCorner.x() + (i * dimensions.x()),
                        topLeftCorner.y())
                        , dimensions, renderable);
            else
                livesArray[i] = new GameObject(new Vector2(topLeftCorner.x() + (i * dimensions.x()),
                        topLeftCorner.y())
                        , dimensions, null);
            gameObjectCollection.addGameObject(livesArray[i], Layer.FOREGROUND);
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
        if (livesCounter.value() < numOfLives) {
            gameObjectCollection.removeGameObject(livesArray[numOfLives - 1],
                    Layer.FOREGROUND);
            numOfLives--;
        }
        else if (maxNumLives >= livesCounter.value() && livesCounter.value() >
                numOfLives) {
            int i = livesCounter.value() - 1;
            livesArray[i] = new GameObject(new Vector2(topLeftCorner.x() +
                    (i * dimensions.x()),
                                                        topLeftCorner.y())
                            , dimensions, livesImage);
            gameObjectCollection.addGameObject(livesArray[i],
                    Layer.FOREGROUND);
            numOfLives++;
        }
    }
}

