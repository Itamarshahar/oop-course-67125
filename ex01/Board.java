public class Board {
    private static final int DEFAULT_BOARD_SIZE = 4;
    private final int boardSize; //
    Mark[][] board;

    /**
     * default constructor
     */
    public Board() {
        this(DEFAULT_BOARD_SIZE);
    }

    /**
     * constructor
     *
     * @param size the size of the board
     */
    public Board(int size) {
        this.boardSize = size;
        this.board = new Mark[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = Mark.BLANK;
            }
        }
    }

    /**
     * getter for the size of the board
     *
     * @return the size of the board
     */
    public int getSize() {
        return boardSize;
    }

    /**
     * mark square at Mark
     *
     * @param mark the mark to assign
     * @param row  the row
     * @param col  the col
     * @return true if placed the mark otherwise false
     */
    public boolean putMark(Mark mark,
                           int row, int col) {
//        boolean coordIsEmpty = coordIsEmpty(row, col);
        if ((coordInsideTheBoard(row)) && (coordInsideTheBoard(col) &&
                coordIsEmpty(row, col))) {
            this.board[row][col] = mark;
            return true;
        }
        return false;
    }

    /**
     * check if the given index within the board
     *
     * @param row the row
     * @param col the col
     * @return true if so, otherwise false
     */
    private boolean coordIsEmpty(int row, int col) {
        return this.board[row][col] == Mark.BLANK;
    }

    private boolean coordInsideTheBoard(int coord) {
        return ((0 <= coord) && (coord < boardSize));
    }

    /**
     * getter for the mark of square
     *
     * @param row the row
     * @param col the col
     * @return the mark in board[row][col]
     */
    public Mark getMark(int row, int col) {
        if ((0 <= row) && (row < boardSize) && (0 <= col)
                && (col < boardSize)) {
            return this.board[row][col];
        }
        return Mark.BLANK;
    }
}
