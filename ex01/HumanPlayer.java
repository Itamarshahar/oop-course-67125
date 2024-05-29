public class HumanPlayer implements Player {
    private final static String MSG_ASK_USER_FOR_COORD = "Player %s, type " +
            "coordinates: "; //TODO doc
     public final static String UNKNOWN_PLAYER_NAME = "Choose a player, " +
            "and start again.\nThe players: [human, clever, whatever, " +
             "genius]"; //TODO doc

    public final static String UNKNOWN_RENDERER_NAME = "Choose a renderer, " +
            "and start again. \nPlease choose one of the following [console, none]";//TODO doc

    public final static String INVALID_COORDINATE = "Invalid mark position," +
            " please choose a different position.\n" +
            "Invalid coordinates, type again: ";//TODO doc

    public final static String OCCUPIED_COORDINATE = "Mark position is already " +
            "occupied.\n" +
            "Invalid coordinates, type again: ";//TODO doc
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
                System.out.println(INVALID_COORDINATE);
            } else {
                System.out.println(OCCUPIED_COORDINATE);
            }
        }

    }
}