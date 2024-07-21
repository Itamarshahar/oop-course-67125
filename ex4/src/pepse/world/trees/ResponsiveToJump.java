package pepse.world.trees;


/**
 * This interface should be implemented by classes that need to respond to a jump action.
 * The implementing class will define the behavior that occurs when the jump action is triggered.
 */
public interface ResponsiveToJump {

    /**
     * This method will be called when a jump action occurs.
     * Implementing classes should define the specific behavior that should happen when a jump is performed.
     */
    void onJump();
}