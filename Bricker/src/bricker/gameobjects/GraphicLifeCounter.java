package Bricker.src.bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

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
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */

    public GraphicLifeCounter(Vector2 topLeftCorner,
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
        this.gameObjectsCollection = gameObjectsCollection;
        this.numOfLives = numOfLives;
        this.livesImage = livesImage;
        this.livesArray = new GameObject[maxNumLives];
        this.maxNumLives = maxNumLives;

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
//        int curX = (int)widgetTopLeftCorner.x();
//
//        for (int i = 0; i < numOfLives; i++) {
//            GameObject heart = new GameObject(new Vector2(curX, widgetTopLeftCorner.y()),
//                    widgetDimensions, widgetRenderable);
//            curX += (int) (widgetDimensions.x() + 3);
//            livesArray[i] = heart;
//            gameObjectsCollection.addGameObject(livesArray[i], Layer.BACKGROUND);
//        }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            gameObjectCollection.removeGameObject(livesArray[numOfLives - 1],
                    Layer.FOREGROUND);
            numOfLives--;
        } else if (livesCounter.value() > numOfLives) {
            int i = livesCounter.value() - 1;
            livesArray[i] =
                    new GameObject(new Vector2(topLeftCorner.x() + (i * dimensions.x()),
                            topLeftCorner.y())
                            , dimensions, livesImage);
            gameObjectCollection.addGameObject(livesArray[i],
                    Layer.FOREGROUND);
            numOfLives++;
        }
    }
}

//    @Override
//    public void update(float deltaTime){
//        super.update(deltaTime);
//
//    }

