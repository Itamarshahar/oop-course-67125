public class GeniusPlayer implements Player {
    private int coord = 0;
    private int diagonal = 0;

    public GeniusPlayer() {
    }

    /**
     * run a single turn of the Genius player
     *
     * @param board the board to play
     * @param mark  the type to mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        // seeking for the "smart moves"
        for (int row = 0; row < board.getSize(); row++) {
            // trying the diagoal first
            if (board.putMark(mark, diagonal, diagonal)) {
                diagonal++;
                return;
            }
            // trying as close as possible to the diagonal
            else if (board.putMark(mark, board.getSize() - diagonal, board.getSize() - diagonal)) {
                diagonal++;
                return;
            }
        }
        // seeking for the next available position
        while (!board.putMark(mark, coord % board.getSize(),
                coord / board.getSize())) {
            coord++;
            if (coord > board.getSize() * (board.getSize()) - 1) {
                coord = 0;
            }

        }
    }
}