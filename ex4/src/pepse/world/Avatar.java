package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;

import pepse.util.AvatarMovement;

/**
 * This class is responsible for the avatar. It is the main character of the game.

 */
public class Avatar extends GameObject {

    private static final String ASSETS_IDLE_0_PNG = "./assets/idle_0.png";
    private static final String ASSETS_IDLE_1_PNG = "./assets/idle_1.png";
    private static final String ASSETS_IDLE_2_PNG = "./assets/idle_2.png";
    private static final String ASSETS_IDLE_3_PNG = "./assets/idle_3.png";
    private static final String ASSETS_JUMP_0_PNG = "./assets/jump_0.png";
    private static final String ASSETS_JUMP_1_PNG = "./assets/jump_1.png";
    private static final String ASSETS_JUMP_2_PNG = "./assets/jump_2.png";
    private static final String ASSETS_JUMP_3_PNG = "./assets/jump_3.png";
    private static final String ASSETS_RUN_0_PNG = "./assets/run_0.png";
    private static final String ASSETS_RUN_1_PNG = "./assets/run_1.png";
    private static final String ASSETS_RUN_2_PNG = "./assets/run_2.png";
    private static final String ASSETS_RUN_3_PNG = "./assets/run_3.png";
    private static final String ASSETS_RUN_4_PNG = "./assets/run_4.png";
    private static final String ASSETS_RUN_5_PNG = "./assets/run_5.png";
    private static final float GRAVITY = 600;
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float JUMP_ENERGY_COST = 10f;
    private static final float RUN_ENERGY_COST = 0.5f;
    private static final float IDLE_ENERGY_GAIN = 1f;
    private static final int MAX_SPEED = 400;
    public static final Vector2 AVATAR_DIMENSIONS = Vector2.of(50, 70);
    private final UserInputListener inputListener;
    private float energy = 100;
    private static final int maxEnergy = 100;
    private static final int minEnergy = 0;
    private AvatarMovement movmentENum = AvatarMovement.STATIC;
    private AnimationRenderable idleAnimation;
    private AnimationRenderable runAnimation;
    private AnimationRenderable jumpAnimation;
    /**
     * The tag of the avatar.
     */
    public static final String TAG = "Avatar";
    private ArrayList<Runnable> didJustJumpObserver = new ArrayList<>();


    /**
     * The constructor of the avatar.
     * @param pos the position of the avatar.
     * @param inputListener the input listener of the avatar.
     * @param imageReader the image reader of the avatar.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader){
        super(pos, AVATAR_DIMENSIONS, imageReader.readImage(ASSETS_IDLE_0_PNG, false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        setTag(TAG);

        idleAnimation = new AnimationRenderable(new String[]{ASSETS_IDLE_0_PNG, ASSETS_IDLE_1_PNG,
                ASSETS_IDLE_2_PNG, ASSETS_IDLE_3_PNG}, imageReader, false, (double)0.2f);
        runAnimation = new AnimationRenderable(new String[]{ASSETS_RUN_0_PNG, ASSETS_RUN_1_PNG,
                ASSETS_RUN_2_PNG, ASSETS_RUN_3_PNG, ASSETS_RUN_4_PNG, ASSETS_RUN_5_PNG},
                imageReader, false, (double)0.2f);
        jumpAnimation = new AnimationRenderable(new String[]{ASSETS_JUMP_0_PNG, ASSETS_JUMP_1_PNG,
                ASSETS_JUMP_2_PNG, ASSETS_JUMP_3_PNG}, imageReader, false, (double)0.2f);

    }

    @Override
    /**
     * This method updates the avatar. It changes the avatar's velocity and energy according to the input.
     * @param deltaTime the time passed since the last update.
     *                  It changes the avatar's renderable according to its velocity.
     *                  It also changes the avatar's energy according to its velocity.
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
         movmentENum = AvatarMovement.STATIC;
         if(getVelocity().x() == 0 && getVelocity().y() != 0){
             movmentENum = AvatarMovement.JUMPING;
             renderer().setRenderable(jumpAnimation);
         }
        float xVel = 0;
         if(!(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && inputListener.isKeyPressed(KeyEvent.VK_RIGHT))){
            if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy >= RUN_ENERGY_COST){
                xVel -= VELOCITY_X;
                movmentENum = AvatarMovement.RUNNING;
                renderer().setRenderable(runAnimation);
                renderer().setIsFlippedHorizontally(true);
            }
            if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy >= RUN_ENERGY_COST){
                xVel += VELOCITY_X;
                movmentENum = AvatarMovement.RUNNING;
                renderer().setRenderable(runAnimation);
                renderer().setIsFlippedHorizontally(false);
            }
            transform().setVelocityX(xVel);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 &&
                energy >= JUMP_ENERGY_COST){
            decreaseEnergy(JUMP_ENERGY_COST);
            transform().setVelocityY(VELOCITY_Y);
            movmentENum = AvatarMovement.JUMPING;
            renderer().setRenderable(jumpAnimation);
            for(Runnable observer : didJustJumpObserver){
                observer.run(); // upadte the listeners
            }
        }
        if (this.getVelocity().y() > MAX_SPEED) {
            this.setVelocity(new Vector2(this.getVelocity().x(), MAX_SPEED));
        }
        switch (movmentENum){
            case STATIC:
                if(getVelocity().y() == 0){
                    increaseEnergy(IDLE_ENERGY_GAIN);
                    renderer().setRenderable(idleAnimation);
                 }
                break;
            case RUNNING:
                decreaseEnergy(RUN_ENERGY_COST);
                break;
            case JUMPING:
                renderer().setRenderable(jumpAnimation);
//                 System.out.println("JUMP. energy: " + energy);
                break;
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
            increaseEnergy(delta);
        } else {
            decreaseEnergy(-delta);
        }
    }

    /**
     * This method returns the action to be performed when the avatar eats a fruit.
     * @return the action to be performed when the avatar eats a fruit.
     */
    public Consumer<Float> getOnEaten() {
        return this::changeEnergyBy;
    }

    private void increaseEnergy(float delta) {
        this.energy = min(energy + delta, maxEnergy);
    }

    private void decreaseEnergy(float delta) {
        this.energy = max(energy - delta, minEnergy);
    }


    /**
     * This method adds a observer to the didJustJumpListeners.
     * @param observer the observer to be added.
     */
    public void addDidJustJumpObserver(Runnable observer){
        didJustJumpObserver.add(observer);
    }
}



