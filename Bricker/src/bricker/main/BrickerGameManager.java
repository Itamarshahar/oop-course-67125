package Bricker.src.bricker.main;

import Bricker.src.bricker.brick_strategies.BasicCollisionStrategy;
import Bricker.src.bricker.brick_strategies.CollisionStrategy;
import Bricker.src.bricker.gameobjects.Ball;
import Bricker.src.bricker.gameobjects.Brick;
import Bricker.src.bricker.gameobjects.Paddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


public class BrickerGameManager extends GameManager {
    private static final float BALL_SPEED = 300;
    private static final int BRICK_DIST = 5;
    private static final int LIVES_START_COUNT = 10000;
    private int rows = 8;
    private int cols = 7;
    private static final int BRICK_HEIGHT = 15;
    private Vector2 windowDimensions;
    private static final int WALL_WIDTH = 10;
    private ImageReader imageReader;


    private Counter livesCounter;
    private Counter brickCounter;
    private Ball ball;
    private UserInputListener inputListener;
    private WindowController windowController;


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
        super.update(deltaTime);
        checkForGameEnd();

    }

    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";
        if (brickCounter.value() == 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            prompt = "You win!";
        }
        if (ballHeight > windowDimensions.y()) {
            if (livesCounter.value() > 1) {
                livesCounter.decrement();
                ball.setCenter(windowDimensions.mult(0.5F));
            } else {
                prompt = "You lose!";
            }
        }
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }

    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.imageReader = imageReader;

        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        //create ball
//        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Renderable ballImage = imageReader.readImage("/Users/itamar_shahar/IdeaProjects/oop-course-67125/Bricker/assets/ball.png", true);
//        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Sound collisionSound = soundReader.readSound("/Users/itamar_shahar/IdeaProjects/oop-course-67125/Bricker/assets/blop.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(20, 20), ballImage, collisionSound);
        //choosing random diagonal for the ball
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
        this.windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        this.gameObjects().addGameObject(ball);
        createUserPaddle(imageReader);

        //create walls
        createWalls();
        createBackground(imageReader);

        //create background
//        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
//        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);


        //create bricks
        createBricks(imageReader);

        //create lives
//        Renderable livesImage = imageReader.readImage("assets/heart.png", true);
        Renderable livesImage = imageReader.readImage("/Users/itamar_shahar/IdeaProjects/oop-course-67125/Bricker/assets/heart.png", true);
        livesCounter = new Counter(LIVES_START_COUNT);

    }

    private void createUserPaddle(ImageReader imageReader) {
        Renderable paddleImage = imageReader.readImage("/Users/itamar_shahar/IdeaProjects/oop-course-67125/Bricker/assets/paddle.png",
                false);
        //create  user paddle
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(100, 15),
                paddleImage, inputListener, WALL_WIDTH, windowDimensions);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, windowDimensions.y() - 25));
        gameObjects().addGameObject(paddle);

    }

    private void createBackground(ImageReader imageReader ) {
      Renderable backgroundImage = imageReader.readImage("  /Users/itamar_shahar/IdeaProjects/oop-course-67125/Bricker/assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createBricks(ImageReader imageReader) {
        brickCounter = new Counter(rows * cols);
//        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Renderable brickImage = imageReader.readImage("/Users/itamar_shahar/IdeaProjects/oop-course-67125/Bricker/assets/brick.png", false);
        CollisionStrategy basicStrategy = new BasicCollisionStrategy(gameObjects());
        int brick_size =
                (int) (windowDimensions.x() - (BRICK_DIST * (cols) + 2 * WALL_WIDTH)) / (rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Vector2 placement = new Vector2(BRICK_DIST * (i) + i * brick_size + WALL_WIDTH,
                        BRICK_DIST * (j) + j * BRICK_HEIGHT + WALL_WIDTH);
                GameObject brick = new Brick(placement,
                        new Vector2(brick_size, BRICK_HEIGHT), brickImage, basicStrategy);
                gameObjects().addGameObject(brick);
            }
        }
    }

    private void createWalls() {
        Renderable rectangle = new RectangleRenderable(Color.RED);
        GameObject leftWall = new GameObject(Vector2.ZERO, new Vector2(WALL_WIDTH, windowDimensions.y()), rectangle);
        gameObjects().addGameObject(leftWall);

        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0), new Vector2(10,
                windowDimensions.y()), rectangle);
        gameObjects().addGameObject(rightWall);

    }

    public static void main(String[] args) {
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();


    }


}
