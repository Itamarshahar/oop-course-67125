import java.util.Random;
/**
 * the class represents a clever player with a built in algorithm
 */
class CleverPlayer implements Player {
    private int coord = 0;
    private final Random  random = new Random();
    private boolean chooseRandomIndex = false;
    /**
     * default constructor
     */
    public CleverPlayer() {
    }

    /**
     * run a single turn of the Clever player
     *
     * @param board the board to play
     * @param mark  the type to mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        if (chooseRandomIndex) {
            int randomIndex = random.nextInt((board.getSize()) *
                    (board.getSize()));
            if (board.putMark(mark, randomIndex / board.getSize(),
                    randomIndex % board.getSize())) {
                chooseRandomIndex = false;
                return;
            }
        }
        // seeking for the next available position
        while (!board.putMark(mark, coord % board.getSize(),
                coord / board.getSize())) {
            coord++;
            if (coord > (board.getSize() * (board.getSize()) - 1)) {
                coord = 0;
            }
        }
        chooseRandomIndex = true;
    }
}