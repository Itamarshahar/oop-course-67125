package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Avatar;

import java.awt.*;
import java.util.function.Consumer;


public class Fruit extends GameObject implements ResponsiveToJump {
    public static final String TAG = "fruit";
    private final Color initailFruitColor = new Color(255, 0, 0);
    private final float BONUS_ENERGY = 10f;
    private final String EATEN_FRUIT_TAG = "eaten";
    private final Color[] fruitColors;
    private final OvalRenderable[] fruitRanderables;
    private final Consumer<Float> onEaten;
    private int fruitIndex;


    public Fruit(Vector2 topLeftCorner, Consumer<Float> onEaten, float fruitSize, Color[] fruitColors) {
        super(topLeftCorner, Vector2.of(fruitSize, fruitSize), new OvalRenderable(fruitColors[0]));
        this.fruitColors = fruitColors;
        this.fruitRanderables = new OvalRenderable[fruitColors.length];
        for (int i = 0; i < fruitColors.length; i++) {
            this.fruitRanderables[i] = new OvalRenderable(fruitColors[i]);
        }
        this.fruitIndex = 0;
        this.onEaten = onEaten;
        this.setTag(TAG);
    }

    @Override
    public void onJump() {
        this.fruitIndex++;
        renderer().setRenderable(fruitRanderables[fruitIndex % fruitColors.length]);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other.getTag().equals(Avatar.TAG) && this.getTag().equals(TAG)) {
            super.onCollisionEnter(other, collision);
            onEaten.accept(BONUS_ENERGY);
            this.setTag(EATEN_FRUIT_TAG);
            new Transition<Float>(this,
                    this.renderer()::fadeOut,
                    0f,
                    0f,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    PepseGameManager.DAYTIME_CYCLE_DURATION,
                    Transition.TransitionType.TRANSITION_ONCE,
                    this::fadeIn);
   }}
   private void fadeIn(){
        renderer().fadeIn(0f);
        setTag(TAG); // TODO why this is needed?
    }

    }