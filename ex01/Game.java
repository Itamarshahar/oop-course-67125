public class Game {
    private final static int DEFAULT_SIZE = 4;
    private final static int DEFAULT_WIN_STREAK = 3;
    private final int boardSize, winStreak;
    private final Player playerX;
    private final Player playerO;
    private final Renderer renderer;
    private final Board board;
    private boolean endGame;

    /**
     * default constructor
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this(playerX, playerO, DEFAULT_SIZE, DEFAULT_WIN_STREAK, renderer);
    }

    /**
     * constructor
     *
     * @param playerX   the player that plays with X this game
     * @param playerO   the player that plays with O this game
     * @param size      the size of the board
     * @param winStreak the win streak necessary to win
     */
    public Game(Player playerX, Player playerO, int size, int winStreak,
                Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.boardSize = size;
        this.winStreak = winStreak;
        this.renderer = renderer;
        this.board = new Board(size);
    }

    /**
     * getter
     *
     * @return the winStreak
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * getter
     *
     * @return the BoardSize
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * run single round of game
     *
     * @return the mark of the winner
     */
    public Mark run() {
        Mark winner = Mark.BLANK;
        Mark currentMark = Mark.X;
        while (!isBoardFull()) {
            runTurn(currentMark);
            if (someoneWon(currentMark)) {
                return currentMark;
            }
            if (currentMark == Mark.X) {
                currentMark = Mark.O;
            } else {
                currentMark = Mark.X;
            }
        }
        return Mark.BLANK;
    }

    /**
     * Runs a single turn of the game.
     *
     * @param mark The mark of the player whose turn it is (either X or O).
     */
    private void runTurn(Mark mark) {
        if (mark == Mark.X) {
            playerX.playTurn(board, mark);
        } else {
            playerO.playTurn(board, mark);
        }
        renderer.renderBoard(board);
    }

    /**
     * Checks if the board is full, meaning there are no more blank spaces.
     *
     * @return true if the board is full, false otherwise.
     */
    private boolean isBoardFull() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if ((Mark.BLANK == board.getMark(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if someone has won the game.
     *
     * @param mark The mark of the player whose win condition is being checked.
     * @return true if the player with the given mark has won, false otherwise.
     */
    private boolean someoneWon(Mark mark) {
        return checkRows(mark) || checkCols(mark) || checkDiagonal(mark);
    }

    /**
     * Checks if the player with the given mark has a winning row on the board.
     *
     * @param mark The mark of the player whose win condition is being checked.
     * @return true if the player has a winning row, false otherwise.
     */
    private boolean checkRows(Mark mark) {
        // Iterate through each row of the board
        for (int row = 0; row < boardSize; row++) {
            int currentSequenceLength = 0;

            // Iterate through each column in the current row
            for (int col = 0; col < boardSize; col++) {
                // If the current cell has the given mark, increment the sequence length
                if (board.getMark(row, col) == mark) {
                    currentSequenceLength++;

                    // If the sequence length has reached the win streak, return true (a win has been found)
                    if (currentSequenceLength == winStreak)
                        return true;
                }
                // Otherwise, reset the sequence length
                else {
                    currentSequenceLength = 0;
                }
            }
        }

        // No winning row was found
        return false;
    }

    /**
     * Checks if the player with the given mark has a winning column on the board.
     *
     * @param mark The mark of the player whose win condition is being checked.
     * @return true if the player has a winning column, false otherwise.
     */
    private boolean checkCols(Mark mark) {
        // Iterate through each column of the board
        for (int row = 0; row < boardSize; row++) {
            int currentSequenceLength = 0;

            // Iterate through each row in the current column
            for (int col = 0; col < boardSize; col++) {
                // If the current cell has the given mark, increment the sequence length
                if (board.getMark(col, row) == mark) {
                    currentSequenceLength++;

                    // If the sequence length has reached the win streak, return true (a win has been found)
                    if (currentSequenceLength == winStreak)
                        return true;
                }
                // Otherwise, reset the sequence length
                else {
                    currentSequenceLength = 0;
                }
            }
        }

        // No winning column was found
        return false;
    }

    /**
     * Checks if the player with the given mark has a winning diagonal on the board.
     *
     * @param mark The mark of the player whose win condition is being checked.
     * @return true if the player has a winning diagonal, false otherwise.
     */
    private boolean checkDiagonal(Mark mark) {
        // Check for a winning right diagonal
        if (checkRightDiagonal(mark)) {
            return true;
        }

        // Check for a winning left diagonal
        return checkLeftDiagonal(mark);
    }

    /**
     * Checks if the player with the given mark has a winning right diagonal on the board.
     *
     * @param mark The mark of the player whose win condition is being checked.
     * @return true if the player has a winning right diagonal, false otherwise.
     */
    private boolean checkRightDiagonal(Mark mark) {
        // Iterate through each row of the board, starting from the top-left
        for (int row = 0; row < boardSize - winStreak + 1; row++) {
            int currentRowSeq = 0;
            int currentRowCol = 0;

            // Iterate through the diagonal cells starting from the current row and column
            for (int col = 0; row + col < boardSize; col++) {
                // Check the cell in the right diagonal
                if (board.getMark(row + col, col) == mark) {
                    currentRowSeq++;
                    if (currentRowSeq == winStreak) {
                        return true;
                    }
                } else {
                    currentRowSeq = 0;
                }

                // Check the cell in the left diagonal
                if (board.getMark(col, row + col) == mark) {
                    currentRowCol++;
                    if (currentRowCol == winStreak) {
                        return true;
                    }
                } else {
                    currentRowCol = 0;
                }
            }
        }
        // No winning right diagonal was found
        return false;
    }

    /**
     * Checks if the player with the given mark has a winning left diagonal on the board.
     *
     * @param mark The mark of the player whose win condition is being checked.
     * @return true if the player has a winning left diagonal, false otherwise.
     */
    private boolean checkLeftDiagonal(Mark mark) {
        // Iterate through each row of the board, starting from the top-right
        for (int row = 0; row < boardSize - winStreak + 1; row++) {
            int currentRowSeq = 0;
            int currentRowCol = 0;

            // Iterate through the diagonal cells starting from the current row and column
            for (int col = 0; row + col < boardSize; col++) {
                // Check the cell in the left diagonal
                if (board.getMark(row + col, (boardSize - col - 1)) == mark) {
                    currentRowSeq++;
                    if (currentRowSeq == winStreak) {
                        return true;
                    }
                } else {
                    currentRowSeq = 0;
                }
                // Check the cell in the right diagonal
                if (board.getMark(col, (boardSize - row - col - 1)) == mark) {
                    currentRowCol++;
                    if (currentRowCol == winStreak) {
                        return true;
                    }
                } else {
                    currentRowCol = 0;
                }
            }
        }
        // No winning left diagonal was found
        return false;
    }
}

