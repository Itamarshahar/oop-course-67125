package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import danogl.gui.rendering.AnimationRenderable;



public class Avatar  extends GameObject {
    private static final float GRAVITY = 600;
    private static final int FACTOR = 40;
    private final String[] idlePaths = {"assets/idle_0.png", "assets/idle_1.png",
            "assets/idle_2.png", "assets/idle_3.png"};
    private final String[] walkPaths = {"assets/run_0.png", "assets/run_1.png",
            "assets/run_2.png", "assets/run_3.png", "assets/run_4.png", "assets/run_5.png"};
    private final String[] jumpPaths = {"assets/jump_0.png", "assets/jump_1.png",
            "assets/jump_2.png", "assets/jump_3.png"};
    private final AnimationRenderable idleRender;
    private final AnimationRenderable walkRender;
    private final AnimationRenderable jumpRender;

    private pepse.world.AvatarState state = pepse.world.AvatarState.REST;
    private UserInputListener inputListener;

    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader) {
        super(pos, Vector2.ONES.mult(FACTOR), imageReader.readImage("assets/idle_0.png", false));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;

        this.idleRender = new AnimationRenderable(this.idlePaths, imageReader,true, 0.2);
        this.walkRender = new AnimationRenderable(this.walkPaths, imageReader, true, 0.2);
        this.jumpRender = new AnimationRenderable(this.jumpPaths, imageReader, true, 0.2);
    }

}
