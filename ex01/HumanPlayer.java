public class HumanPlayer implements Player {
    private static final String MSG_ASK_USER_FOR_COORD = "Player %s, type " +
            "coordinates: "; // TODO replace

    HumanPlayer() {
    } // TODO typing check

    /**
     * run a single turn of the Human player
     *
     * @param board the board to play
     * @param mark  the type to mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        // ask for cords
        System.out.println(String.format(MSG_ASK_USER_FOR_COORD,
                mark.toString()));
        // check if cords are valid
        while (true) {
            int coordsFromUser = KeyboardInput.readInt();
            int row = coordsFromUser / 10;
            int col = coordsFromUser % 10;
            if (board.putMark(mark, row, col)) {
                break;
            }
            if ((0 > row) || (row >= board.getSize()) || (0 > col) ||
                    (col >= board.getSize())) {
                System.out.println(Constants.INVALID_COORDINATE);
            } else {
                System.out.println(Constants.OCCUPIED_COORDINATE);
            }
        }

    }
}