package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.Transition;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Block;
import pepse.world.Night;
import pepse.world.Sky;
import pepse.world.Terrain;

import java.util.List;
import java.util.Random;

import static danogl.components.Transition.TransitionType.TRANSITION_BACK_AND_FORTH;

public class PepseGameManager extends GameManager {
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    // TODO doc
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    // TODO doc
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        ///////////////////////////////////////////////////////////////////////
        // Sky Part
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);

        ///////////////////////////////////////////////////////////////////////
        // Terrain Part
        Random random = new Random();
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), random.nextInt());
//        Terrain terrain = new Terrain(windowController.getWindowDimensions(),
//                0 );
        List<Block> blocks = terrain.createInRange(-5, (int) windowController.getWindowDimensions().x() + 5); //
        // TODO - itamar - verify why (-5) ?
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        ///////////////////////////////////////////////////////////////////////
        // Light and Dark Part
        float cycleLength = 30;
        GameObject night = Night.create(windowController.getWindowDimensions(), cycleLength);
        gameObjects().addGameObject(night, Layer.STATIC_OBJECTS); // TODO
    }
}


