package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;
import pepse.world.trees.Trunk;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Avatar class representing a character with walking, jumping, and flying capabilities.
 */
public class Avatar extends GameObject {
    /**
     * Default height of the Avatar.
     */
    public static final int DEFAULT_HEIGHT = 50;

    /**
     * Default width of the Avatar.
     */
    public static final int DEFAULT_WIDTH = 75;

    /**
     * Default dimensions (width and height) of the Avatar as a Vector2 object.
     */
    public static final Vector2 DEFAULT_DIMENSIONS = new Vector2(DEFAULT_WIDTH, DEFAULT_HEIGHT);

    /**
     * Tag used to identify Avatar objects.
     */
    public static final String TAG = "avatar";


    private static final int MAX_ENERGY = 100;
    private static final int MIN_ENERGY = 0;
    private static final String ENERGY_STRING = "Energy: %d";
    private static final Vector2 ENERGY_COUNTER_POS = new Vector2(30, 30);
    private static final Vector2 ENERGY_COUNTER_DIMENSIONS = new Vector2(100, 30);
    private static final Vector2 ENERGY_BAR_POS = new Vector2(30, 90);
    private static final Vector2 ENERGY_BAR_DIMENSIONS = new Vector2(350, 30);
    private static final Color ENERGY_BAR_COLOR = new Color(0, 255, 0);
    private static final Color ENERGY_BAR_WARNING_COLOR = new Color(255, 255, 0);
    private static final Color ENERGY_BAR_DANGER_COLOR = new Color(255, 0, 0);

    private static final double WALK_ANIMATION_FRAME_DURATION = 0.1;
    private static final double IDLE_ANIMATION_FRAME_DURATION = 0.3;
    private static final float GRAVITY = 600;
    private static final float WALKING_VELOCITY = 300;
    private static final float JUMPING_VELOCITY = 600;
    private static final String UI_FONT = "Courier New";
    private static final float FLYING_ANGLE = -30;
    private static final float ENERGY_CONSUMPTION_RATE = 0.5f;
    private static final float ENERGY_BAR_DANGER_LEVEL = 30;
    private static final float ENERGY_BAR_WARNING_LEVEL = 60;

    private static Renderable walkRenderable = null;
    private static Renderable idleRenderable = null;
    private static Renderable jumpRenderable = null;
    private static TextRenderable energyRenderable = null;
    private static Renderable energyBarRenderable = null;
    private static GameObject energyCounter = null;
    private static GameObject energyBar = null;

    private static final String[] idlePaths = {"assets/idle_0.png",
            "assets/idle_1.png", "assets/idle_2.png", "assets/idle_3.png"};
    private static final String[] walkPaths = {"assets/run_0.png",
            "assets/run_1.png", "assets/run_2.png", "assets/run_3.png",
            "assets/run_4.png", "assets/run_5.png"};
    private static final String[] jumpPaths = {"assets/jump_0.png",
            "assets/jump_1.png", "assets/jump_2.png", "assets/jump_3.png"};

    private float energy = MAX_ENERGY;
    private final float ENERGY_WALK_UNIT = .5f;
    private final float ENERGY_JUMP_UNIT = 10;
    private AvatarState state = AvatarState.REST;
    private UserInputListener inputListener;
    private ArrayList<Runnable> didJustJumpObserver = new ArrayList<>();
    private ArrayList<String> shouldCollide = new
            ArrayList<String>(Arrays.asList(Trunk.TAG, Terrain.TAG));

    /**
     * Constructs a new Avatar object.
     *
     * @param pos The initial position of the Avatar.
     * @param inputListener The listener for user input.
     * @param imageReader The image reader to load the Avatar's images.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, DEFAULT_DIMENSIONS, imageReader.readImage("assets/idle_0.png", false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        setTag(TAG);
        walkRenderable = new AnimationRenderable(walkPaths, imageReader, true,
                WALK_ANIMATION_FRAME_DURATION);
        idleRenderable = new AnimationRenderable(idlePaths, imageReader, true,
                IDLE_ANIMATION_FRAME_DURATION);
        jumpRenderable = new AnimationRenderable(jumpPaths, imageReader, true,
                WALK_ANIMATION_FRAME_DURATION);
        energyRenderable = new TextRenderable(String.format(ENERGY_STRING, MAX_ENERGY),
                UI_FONT, false, true);
        energyBarRenderable = new RectangleRenderable(ENERGY_BAR_COLOR);
        energyBar = new GameObject(ENERGY_COUNTER_POS, ENERGY_COUNTER_DIMENSIONS, energyBarRenderable);
    }

    /**
     * Updates the Avatar's state each frame.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEnergyDisplay();
        handleWalking();
        handleJumping();
        handleFlying();
        handleRest();
    }


    private void handleWalking() {
        float velocity = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (updateRight()) {
                velocity += WALKING_VELOCITY;
            }
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if (updateLeft()) {
                velocity -= WALKING_VELOCITY;
            }
        }
        transform().setVelocityX(velocity);
    }

    private void handleJumping() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && transform().getVelocity().y() == 0) {
            updateJump();
            for(Runnable observer : didJustJumpObserver) {
                observer.run();
            }
        }
    }

    private void handleFlying() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energy > 0) {
            transform().setVelocityY(-JUMPING_VELOCITY);
            renderer().setRenderableAngle(FLYING_ANGLE);
            energy -= ENERGY_CONSUMPTION_RATE;
            state = AvatarState.FLY;
        } else {
            if (transform().getVelocity().y() > 0) {
                renderer().setRenderableAngle(-FLYING_ANGLE);
                state = AvatarState.FLY;
            } else {
                renderer().setRenderableAngle(0);
            }
        }
        if (energy < MAX_ENERGY && transform().getVelocity().y() == 0) {
            energy += ENERGY_CONSUMPTION_RATE;
        }
    }

    private boolean updateLeft() {
        if (this.energy < ENERGY_WALK_UNIT && this.energy == 0) return false;
        if (this.getVelocity().y() != 0) {
            this.renderer().setRenderable(jumpRenderable);
        } else {
            this.renderer().setRenderable(walkRenderable);
        }
        this.renderer().setIsFlippedHorizontally(true);
        this.state = AvatarState.LEFT;
        this.energy -= ENERGY_WALK_UNIT;
        return true;
    }

    private boolean updateRight() {
        if (this.energy < ENERGY_WALK_UNIT && this.energy == 0) return false;
        if (this.getVelocity().y() != 0) {
            this.renderer().setRenderable(jumpRenderable);
        } else {
            this.renderer().setRenderable(walkRenderable);
        }
        this.renderer().setIsFlippedHorizontally(false);
        this.state = AvatarState.RIGHT;
        this.energy -= ENERGY_WALK_UNIT;
        return true;
    }

    private boolean updateJump() {
        if (this.energy < ENERGY_JUMP_UNIT || this.state == AvatarState.FLY) return false;
        this.transform().setVelocityY(-JUMPING_VELOCITY);
        this.energy -= ENERGY_JUMP_UNIT;
        this.state = AvatarState.JUMP;
        return true;
    }
    private void handleRest() {
        if (!inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && !inputListener.isKeyPressed(KeyEvent.VK_LEFT)
                && transform().getVelocity().y() == 0 || energy == 0) {
            renderer().setRenderable(idleRenderable);
            state = AvatarState.REST;
        }
    }

    private void updateEnergyDisplay() {
        energyRenderable.setString(String.format(ENERGY_STRING, (int) energy));
        energyBar.transform().setDimensions(new Vector2(energy * ENERGY_BAR_DIMENSIONS.x()
                / MAX_ENERGY, ENERGY_BAR_DIMENSIONS.y()));
        energyBar.transform().setTopLeftCorner(ENERGY_BAR_POS);
        if (energy < ENERGY_BAR_WARNING_LEVEL && energy > ENERGY_BAR_DANGER_LEVEL) {
            energyBar.renderer().setRenderable(new RectangleRenderable(ENERGY_BAR_WARNING_COLOR));
        } else if (energy < ENERGY_BAR_DANGER_LEVEL) {
            energyBar.renderer().setRenderable(new RectangleRenderable(ENERGY_BAR_DANGER_COLOR));
        } else {
            energyBar.renderer().setRenderable(new RectangleRenderable(ENERGY_BAR_COLOR));
        }
    }


    /**
     * Displays the energy bar and counter on the screen.
     *
     * @param gameObjects The collection of game objects to which the energy bar and counter will be added.
     */
    public static void displayEnergy(GameObjectCollection gameObjects) {
        energyCounter = new GameObject(ENERGY_COUNTER_POS, ENERGY_COUNTER_DIMENSIONS, energyRenderable);
        energyCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(energyCounter, Layer.UI);
        energyBar = new GameObject(ENERGY_BAR_POS, ENERGY_BAR_DIMENSIONS, energyBarRenderable);
        energyBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(energyBar, Layer.UI);
    }

    /**
     * Handles the event when the Avatar collides with another object.
     *
     * @param other The other game object involved in the collision.
     * @param collision The details of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (shouldCollide.contains(other.getTag())) {
            transform().setVelocityY(0);
            state = AvatarState.REST;
        }
    }

    /**
     * This method returns the energy of the avatar.
     * @return the energy of the avatar.
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * This method changes the energy of the avatar.
     * @param delta the change in energy.
     */
    public void changeEnergyBy(float delta) {
        if (delta > 0) {
            increeseEnergy(delta);
        } else {
            decreeseEnergy(-delta);
        }
    }

    /**
     * This method returns the action to be performed when the avatar eats a fruit.
     * @return the action to be performed when the avatar eats a fruit.
     */
    public Consumer<Float> getOnEaten() {
        return this::changeEnergyBy;
    }

    private void increeseEnergy(float delta) {
        this.energy = min(energy + delta, MAX_ENERGY);
    }

    private void decreeseEnergy(float delta) {
        this.energy = max(energy - delta, MIN_ENERGY);
    }
    /**
     * Adds an observer to be notified when the Avatar has just jumped.
     *
     * @param observer The observer to be added. It should implement the Runnable interface.
     */
    public void addDidJustJumpObserver(Runnable observer) {
        didJustJumpObserver.add(observer);
    }



}
