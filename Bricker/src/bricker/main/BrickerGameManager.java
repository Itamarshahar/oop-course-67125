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
    /**
     * The speed of the ball.
     */
    private static final float BALL_SPEED = 200;

    /**
     * The distance between bricks.
     */
    private static final int BRICK_DIST = 5;

    /**
     * The initial number of lives the player starts with.
     */
    private static final int LIVES_START_COUNT = 3;

    /**
     * The maximum number of lives the player can have.
     */
    public static final int MAX_NUM_LIFE = LIVES_START_COUNT + 1;

    /**
     * The x-coordinate of the top-left corner of the lives display.
     */
    private static final float LIVES_POSITION_X = 0;

    /**
     * The distance between the top of the window and the lives display.
     */
    private static final float POSITION_DIST_Y = 30;

    /**
     * The width of the paddle.
     */
    public static final int PADDLE_WIDTH = 100;

    /**
     * The size of the life icon.
     */
    private static final int LIFE_SIZE = 30;

    /**
     * The radius of the original ball.
     */
    private static final int ORIGINAL_BALL_RADIUS = 20;

    /**
     * The number of collisions for the camera to get back to the regular state.
     */
    private static final int BALLS_HIT_FOR_CAMERA = 4;

    /**
     * The height of the bricks.
     */
    private static final int BRICK_HEIGHT = 15;

    /**
     * The width of the walls.
     */
    private static final int WALL_WIDTH = 15;

    /**
     * The message to display when the player wins.
     */
    private static final String WIN_MESSAGE = "You win!";

    /**
     * The message to display when the player loses.
     */
    private static final String LOSE_MESSAGE = "You lose!";

    /**
     * The prompt to ask the player if they want to play again.
     */
    private static final String PLAY_AGAIN_PROMPT = " Play again?";

    /**
     * The path to the ball image.
     */
    private static final String BALL_IMAGE_PATH = "assets/ball.png";

    /**
     * The path to the collision sound.
     */
    private static final String COLLISION_SOUND_PATH = "assets/blop.wav";

    /**
     * The path to the paddle image.
     */
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";

    /**
     * The path to the background image.
     */
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    /**
     * The path to the lives (heart) image.
     */
    private static final String LIVES_IMAGE_PATH = "assets/heart.png";

    /**
     * The path to the brick image.
     */
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";

    /**
     * The path to the puck (small ball) image.
     */
    private static final String PUCK_IMAGE_PATH = "assets/mockBall.png";

    /**
     * The number of collision strategies available.
     */
    private static final int NUM_OF_STRATEGIES = 10;

    /**
     * The tag for the original ball.
     */
    private static final String ORIGINAL_BALL_TAG = "ORIGINAL_BALL";

    /**
     * The tag for the original paddle.
     */
    public static final String ORIGINAL_PADDLE_TAG = "ORIGINAL_PADDLE";

    /**
     * The size factor for the puck (small ball) relative to the original ball.
     */
    private static final float PUCK_SIZE_FACTOR = 0.75F;

    /**
     * The counter for the number of remaining bricks.
     */
    private Counter brickCounter;

    /**
     * The counter for the number of remaining lives.
     */
    private Counter livesCounter;

    /**
     * The number of rows of bricks.
     */
    private int rows = 8;

    /**
     * The number of columns of bricks.
     */
    private int cols = 7;

    /**
     * The dimensions of the game window.
     */
    private Vector2 windowDimension;

    /**
     * The ImageReader object for loading images.
     */
    private ImageReader imageReader;

    /**
     * The Ball object representing the main ball in the game.
     */
    private Ball ball;

    /**
     * The UserInputListener object for handling user input.
     */
    private UserInputListener inputListener;

    /**
     * The WindowController object for managing the game window.
     */
    private WindowController windowController;

    /**
     * The SoundReader object for loading sounds.
     */
    private SoundReader soundReader;

    /**
     * The top-left corner position for the lives display.
     */
    private Vector2 topLeftCorner;

    /**
     * The Random object for generating random values.
     */
    private Random random;

    /**
     * The GameObjectCollection for managing game objects.
     */
    private GameObjectCollection gameObjectCollection;

    /**
     * The MessageHandler object for handling game messages.
     */
    private MessageHandler messageHandler;

    /**
     * The counter for the number of collisions when the camera starts following the ball.
     */
    private Counter cameraStartedCounter;

    /**
     * A flag indicating if an extra paddle exists in the game.
     */
    private boolean extraPaddleExist = false;

    /**
     * Constructs a new BrickerGameManager with the specified window title and dimensions.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);

    }

    /**
     * Constructs a new BrickerGameManager with the specified window title, dimensions,
     * and the number of rows and columns for the bricks.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param cols             The number of columns for bricks.
     * @param rows             The number of rows for bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions, int cols, int rows) {
        this(windowTitle, windowDimensions);
        this.cols = cols;
        this.rows = rows;
    }

    /**
     * Overrides the update method to check for game end conditions and update the camera position.
     *
     * @param deltaTime The time elapsed since the last frame update.
     */
    @Override
    public void update(float deltaTime) {
        int cameraDif = ball.getCollisionCounter() - cameraStartedCounter.value();
        if (cameraDif >= BALLS_HIT_FOR_CAMERA) {
            this.setCamera(null);
        }
        super.update(deltaTime);
        checkForGameEnd();
    }


    /**
     * Overrides the initializeGame method to initialize the game components.
     *
     * @param imageReader      The ImageReader object for loading images.
     * @param soundReader      The SoundReader object for loading sounds.
     * @param inputListener    The UserInputListener object for handling user input.
     * @param windowController The WindowController object for managing the game window.
     */
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
        createPrimaryPaddle();
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

    /**
     * Removes a game object from the collection, handling special cases like the extra paddle.
     *
     * @param gameObjectToRemove The game object to be removed.
     * @return True if the game object was successfully removed, false otherwise.
     */
    public boolean removeGameObjectHandler(GameObject gameObjectToRemove) {
        // TODO maybe add layer?
        if (gameObjectToRemove instanceof ExtraPaddle) {
            extraPaddleExist = false;
        }
        return gameObjects().removeGameObject(gameObjectToRemove);
    }

    /**
     * Starts the camera mode, following the ball's movement.
     *
     * @param ballTag The tag of the ball object.
     */
    public void startCamera(String ballTag) {
//        ballTag = ORIGINAL_BALL_TAG; // todo  remove this line
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

    /**
     * Adds a heart (extra life) object to the game.
     *
     * @param topLeftCorner The top-left corner position of the heart object.
     */
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

    /**
     * Increases the life counter up to the maximum number of lives.
     */
    public void increaseLifeCounter() {
        if (livesCounter.value() < MAX_NUM_LIFE) {
            livesCounter.increment();
        }
    }

    /**
     * Creates and initializes the numeric and graphical life counters.
     *
     * @param renderable           The renderable object for the graphical life counter.
     * @param dimension            The dimensions of the life counter icons.
     * @param livesCounter         The counter for the number of remaining lives.
     * @param gameObjectCollection The collection of game objects to add the life counters to.
     */
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

    /**
     * Creates and initializes the main ball object.
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH, true);
        Sound collisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        Vector2 ballDimensions = new Vector2(ORIGINAL_BALL_RADIUS,
                ORIGINAL_BALL_RADIUS);
        ball = new Ball(Vector2.ZERO, ballDimensions, ballImage, collisionSound);

        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;

        if (random.nextBoolean()) {
            ballVelX *= -1;
        }
        if (random.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        this.windowDimension = windowController.getWindowDimensions();
        ball.setCenter(windowDimension.mult(0.5f));
        ball.setTag(ORIGINAL_BALL_TAG);
        this.gameObjects().addGameObject(ball);
    }

    /**
     * Creates a puck (small ball) object with random velocity and direction.
     *
     * @param topLeftCorner The top-left corner position of the puck object.
     */
    public void createPuck(Vector2 topLeftCorner) {
        Vector2 puckDimensions =
                new Vector2(ORIGINAL_BALL_RADIUS * PUCK_SIZE_FACTOR,
                        ORIGINAL_BALL_RADIUS * PUCK_SIZE_FACTOR);
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH, true);
        Sound puchCollisionSound = soundReader.readSound(COLLISION_SOUND_PATH);
        float minHeight = windowDimension.y() + 2 * BRICK_HEIGHT;

        double angle = random.nextDouble() * Math.PI;
        Puck puck = new Puck(topLeftCorner, puckDimensions, puckImage, puchCollisionSound, minHeight, this);

        float velocityX = (float) Math.cos(angle) * BALL_SPEED;
        float velocityY = (float) Math.sin(angle) * BALL_SPEED;
        Vector2 velocity = new Vector2(velocityX, velocityY);
        puck.setVelocity(velocity);
        gameObjects().addGameObject(puck);

    }

    /**
     * Creates and initializes the primary paddle object.
     */
    private void createPrimaryPaddle() {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);

        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15),
                paddleImage, inputListener, WALL_WIDTH, windowDimension);
        paddle.setCenter(new Vector2(windowDimension.x() / 2, windowDimension.y() - 25));
        paddle.setTag(ORIGINAL_PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    /**
     * Adds an extra paddle to the game at the center of the window.
     */
    public void addExtraPaddle() {
        if (!extraPaddleExist) {
            extraPaddleExist = true;
            Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH, false);
            GameObject extraPaddle = new ExtraPaddle(Vector2.ZERO, new Vector2(100, 15),
                    paddleImage, inputListener, WALL_WIDTH, windowDimension,
                    this);
            extraPaddle.setCenter(new Vector2(windowDimension.x() / 2, windowDimension.y() / 2));
            extraPaddle.setTag(ORIGINAL_PADDLE_TAG);
            gameObjects().addGameObject(extraPaddle);

        }
    }

    /**
     * Creates and initializes the background object.
     */
    private void createBackground() {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH, false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimension, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * Creates and initializes the brick objects with different collision strategies.
     */
    private void createBricks() {
        CollisionStrategyFactory collisionStrategyFactory =
                new CollisionStrategyFactory(this);
        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH, false);

        int brick_size =
                (int) (windowDimension.x() - (BRICK_DIST * (cols) + 2 * WALL_WIDTH)) / (rows);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
//                int nextInt = random.nextInt(NUM_OF_STRATEGIES);
                int nextInt = 2; // TODO remove
//                int nextInt = random.nextInt(NUM_OF_STRATEGIES);
                CollisionStrategy currentCollisionStrategy = collisionStrategyFactory.getCollisionStrategy(null, nextInt);
                Vector2 placement = new Vector2(BRICK_DIST * (row) + row * brick_size + WALL_WIDTH,
                        BRICK_DIST * (col) + col * BRICK_HEIGHT + WALL_WIDTH);
                GameObject brick = new Brick(placement,
                        new Vector2(brick_size, BRICK_HEIGHT), brickImage, currentCollisionStrategy);
                gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * Creates and initializes the wall objects.
     */
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

    /**
     * Checks for game end conditions (winning or losing) and prompts the player to play again.
     */
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

    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            new BrickerGameManager("Bricker", new Vector2(700, 500), Integer.parseInt(args[0]), Integer.parseInt(args[1])).run();
        } else {
            new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
        }
    }

}

