package Bricker.src.bricker.main;

import Bricker.src.bricker.brick_strategies.CollisionStrategy;
import Bricker.src.bricker.brick_strategies.CollisionStrategyFactory;
import Bricker.src.bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends GameManager {
    private static final float BALL_SPEED = 300;
    private static final int BRICK_DIST = 5;
    private static final int LIVES_START_COUNT = 3;
    public static final int MAX_NUM_LIFE = LIVES_START_COUNT + 1;

    private static final float LIVES_POSITION_X = 0;
    private static final float POSITION_DIST_Y = 30;
    public static final int PADDLE_WIDTH = 100;
    private static final int LIFE_SIZE = 30;
    /**
     * The number of collisions for the camera to get back to the regular
     * state
     */
    private static final int BALLS_HIT_FOR_CAMERA = 4;
    private static final int BRICK_HEIGHT = 15;
    private static final int WALL_WIDTH = 15;
    private static final String WIN_MESSAGE = "You win!";
    private static final String LOSE_MESSAGE = "You lose!";
    private static final String PLAY_AGAIN_PROMPT = " Play again?";
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String COLLISION_SOUND_PATH = "assets/blop.wav";
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String LIVES_IMAGE_PATH = "assets/heart.png";
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final int NUM_OF_STRATEGIES = 10;
    private static final String ORIGINAL_BALL_TAG = "ORIGINAL_BALL";
    public static final String ORIGINAL_PADDLE_TAG = "ORIGINAL_PADDLE";
    private Counter brickCounter;
    private Counter livesCounter;
    private int rows = 8;
    private int cols = 7;
    private Vector2 windowDimension;
    private ImageReader imageReader;
    private Ball ball;
    private UserInputListener inputListener;
    private WindowController windowController;
    private SoundReader soundReader;
    private Vector2 topLeftCorner;
    private Random random;
    private GameObjectCollection gameObjectCollection;
    private MessageHandler messageHandler;
    private Counter cameraStartedCounter;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);

    }

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        this(windowTitle, windowDimensions);
        this.cols = cols;
        this.rows = rows;
    }

    @Override
    public void update(float deltaTime) {
        int cameraDif = ball.getCollisionCounter() - cameraStartedCounter.value();
        if (cameraDif >= BALLS_HIT_FOR_CAMERA){
            this.setCamera(null);
        }
        super.update(deltaTime);
        checkForGameEnd();
    }


    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.inputListener = inputListener;
        this.windowController = windowController;
        this.messageHandler = new MessageHandler(windowController);
        this.gameObjectCollection = new GameObjectCollection(messageHandler);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.livesCounter = new Counter(LIVES_START_COUNT);
        this.windowDimension = windowController.getWindowDimensions();
        this.topLeftCorner = new Vector2(LIVES_POSITION_X,
                windowDimension.y() - POSITION_DIST_Y);
        this.brickCounter = new Counter(rows * cols);
        this.random = new Random();
        cameraStartedCounter = new Counter();
        Vector2 lifeDimensions = new Vector2(LIFE_SIZE, LIFE_SIZE);

        createBall();
        createPrimeryPaddle();
        createWalls();
        createBackground();
        createBricks();
        Renderable livesImage = imageReader.readImage(LIVES_IMAGE_PATH, true);
        livesCounter = new Counter(LIVES_START_COUNT);
        createLives(livesImage,
                lifeDimensions,
                livesCounter,
                gameObjects());

    }

    /**
     * Retrieves the brick counter.
     *
     * @return The brick counter.
     */
    public Counter getBrickCounter() {
        return brickCounter;
    }

    public boolean removeGameObjectHandler(GameObject gameObjectToRemove) {
        // TODO maybe add layer?
        return gameObjects().removeGameObject(gameObjectToRemove);
    }

    public void startCamera(String ballTag) {
        ballTag = ORIGINAL_BALL_TAG; // todo  remove this line
        if (this.camera() == null && (ballTag.equals(ORIGINAL_BALL_TAG))) {
           cameraStartedCounter.increaseBy(ball.getCollisionCounter() - cameraStartedCounter.value());
            setCamera(new Camera(
                                ball, // Object to follow
                                Vector2.ZERO, // Follow the center of the object
                                windowController.getWindowDimensions().mult(1.2f), // Widen the frame a bit
                                windowController.getWindowDimensions() // Share the window dimensions
                                )
            );
        }
    }

    public void addHeart(Vector2 topLeftCorner) {
        Renderable heartFig = imageReader.readImage(LIVES_IMAGE_PATH, true);
        Heart heart = new Heart(topLeftCorner,
                                windowDimension.mult(0.05f),
                                heartFig,
                this,
                ORIGINAL_PADDLE_TAG,
                windowDimension.y());
        heart.setVelocity(new Vector2(0, 100));
        gameObjects().addGameObject(heart);
    }

    public void increaseLifeCounter() {
        if (livesCounter.value() < MAX_NUM_LIFE) {
            livesCounter.increment();
        }
    }

    private void createLives(Renderable renderable,
                             Vector2 dimension,
                             Counter livesCounter,
                             GameObjectCollection gameObjectCollection) {

        GameObject numericLives = new NumericLifeCounter(livesCounter, new Vector2(topLeftCorner.x() +
                dimension.x() * MAX_NUM_LIFE, topLeftCorner.y()), dimension);
        gameObjects().addGameObject(numericLives, Layer.FOREGROUND);

        GameObject graphicLives = new GraphicLifeCounter(topLeftCorner,
                dimension,
                livesCounter,
                renderable,
                gameObjectCollection,
                LIVES_START_COUNT,
                renderable,
                MAX_NUM_LIFE);
        gameObjects().addGameObject(graphicLives, Layer.FOREGROUND);
    }

    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();

        if (rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if (rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        this.windowDimension = windowController.getWindowDimensions();
        ball.setCenter(windowDimension.mult(0.5f));
        this.gameObjects().addGameObject(ball);
    }

    private void createPrimeryPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15),
                paddleImage, inputListener, WALL_WIDTH, windowDimension);
        paddle.setCenter(new Vector2(windowDimension.x() / 2, windowDimension.y() - 25));
        paddle.setTag(ORIGINAL_PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimension, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBricks() {
        CollisionStrategyFactory collisionStrategyFactory =
                new CollisionStrategyFactory(this   );
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);

        int brick_size =
                (int) (windowDimension.x() - (BRICK_DIST * (cols) + 2 * WALL_WIDTH)) / (rows);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
//                int nextInt = random.nextInt(NUM_OF_STRATEGIES);
                int nextInt = 4; // TODO remove
                CollisionStrategy currentCollisionStrategy = collisionStrategyFactory.getCollisionStrategy(null, nextInt);
                Vector2 placement = new Vector2(BRICK_DIST * (row) + row * brick_size + WALL_WIDTH,
                        BRICK_DIST * (col) + col * BRICK_HEIGHT + WALL_WIDTH);
                GameObject brick = new Brick(placement,
                        new Vector2(brick_size, BRICK_HEIGHT), brickImage, currentCollisionStrategy);
                gameObjects().addGameObject(brick);
            }
        }
    }

    private void createWalls() {

        Vector2 leftWallTopLeftCorner = Vector2.ZERO;
        Vector2 leftWallDimensions = new Vector2(WALL_WIDTH, windowDimension.y());
        GameObject leftWall = new GameObject(leftWallTopLeftCorner, leftWallDimensions, null);
        gameObjects().addGameObject(leftWall);

        Vector2 rightWallTopLeftCorner =
                new Vector2(windowDimension.x() - WALL_WIDTH, 0);
        Vector2 rightWallDimensions =
                new Vector2(WALL_WIDTH, windowDimension.y());
        GameObject rightWall = new GameObject(rightWallTopLeftCorner, rightWallDimensions, null);
        gameObjects().addGameObject(rightWall);

        Vector2 topWallTopLeftCorner = Vector2.ZERO;
        Vector2 topWallDimensions =
                new Vector2(windowDimension.x() - WALL_WIDTH, WALL_WIDTH);
        GameObject topWall = new GameObject(topWallTopLeftCorner,
                topWallDimensions, null);
        gameObjects().addGameObject(topWall);
    }

    private void checkForGameEnd() {
        // TODO fix the win case
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (brickCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = WIN_MESSAGE;
        }
        if (ballHeight > windowDimension.y()) {
            if (livesCounter.value() > 1) {
                livesCounter.decrement();
                ball.setCenter(windowDimension.mult(0.5F));
            } else {
                prompt = LOSE_MESSAGE;
            }
        }
        if (!prompt.isEmpty()) {
            prompt += PLAY_AGAIN_PROMPT;
            if (windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            new BrickerGameManager("Bricker", new Vector2(700, 500), Integer.parseInt(args[0]), Integer.parseInt(args[1])).run();
        } else {
            new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
        }
    }

}

