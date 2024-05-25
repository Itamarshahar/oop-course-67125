public class Game {
    private final static int DEFAULT_SIZE = 4;
    private final static int DEFAULT_WIN_STREAK = 3;
    private final int boardSize, winStreak;
    private Player playerX;
    private Player playerO;
    private Renderer renderer;
    private Board board;
    private boolean endGame;

    Game(Player playerX, Player playerO, Renderer renderer) {
        this(playerX, playerO, DEFAULT_SIZE, DEFAULT_WIN_STREAK, renderer);
    }

    Game(Player playerX, Player playerO, int size, int winStreak,
         Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.boardSize = size;
        this.winStreak = winStreak;
        this.renderer = renderer;
        this.board = new Board(size);
    }

    int getWinStreak() {
        return winStreak;
    }

    int getBoardSize() {
        return boardSize;
    }

    Mark run() {
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

    private void runTurn(Mark mark) {
        if (mark == Mark.X) {
            playerX.playTurn(board, mark);
        } else {
            playerO.playTurn(board, mark);
        }
        renderer.renderBoard(board);
    }

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

    private boolean someoneWon(Mark mark) {
        return checkRows(mark) || checkCols(mark) || checkDiagonal(mark);
    }

    /**
     * check if there are win sequance at row
     *
     * @param mark mark of the player
     * @return true if the player is winner and false otherwise
     */
    private boolean checkRows(Mark mark) {

        for (int row = 0; row < boardSize; row++) {
            int currentSequenceLength = 0;
            for (int col = 0; col < boardSize; col++) {
                if (board.getMark(row, col) == mark) {
                    currentSequenceLength++;
                    if (currentSequenceLength == winStreak)
                        return true;
                } else currentSequenceLength = 0;
            }
        }
        return false;
    }

    private boolean checkCols(Mark mark) {

        for (int row = 0; row < boardSize; row++) {
            int currentSequenceLength = 0;
            for (int col = 0; col < boardSize; col++) {
                if (board.getMark(col, row) == mark) {
                    currentSequenceLength++;
                    if (currentSequenceLength == winStreak)
                        return true;
                } else currentSequenceLength = 0;
            }
        }

        return false;
    }

    private boolean checkDiagonal(Mark mark) {
        return (checkRightDiagonal(mark) || checkLeftDiagonal(mark));
    }

    private boolean checkRightDiagonal(Mark mark) {
        for (int row = 0; row < boardSize - winStreak + 1; row++) {
            int currentRowSeq = 0;
            int currentRowCol = 0;
            for (int col = 0; row + col < boardSize; col++) {
                if (board.getMark(row + col, col) == mark) {
                    currentRowSeq++;
                    if (currentRowSeq == winStreak) {
                        return true;
                    }
                } else currentRowSeq = 0;
                if (board.getMark(col, row + col) == mark) {
                    currentRowCol++;
                    if (currentRowCol == winStreak) {
                        return true;
                    }
                } else currentRowCol = 0;
            }
        }
        return false;
    }

    private boolean checkLeftDiagonal(Mark mark) {
        for (int row = 0; row < boardSize - winStreak + 1; row++) {
            int currentRowSeq = 0;
            int currentRowCol = 0;
            for (int col = 0; row + col < boardSize; col++) {
                if (board.getMark(row + col, (boardSize - col - 1)) == mark) {
                    currentRowSeq++;
                    if (currentRowSeq == winStreak) {
                        return true;
                    }
                } else currentRowSeq = 0;
                if (board.getMark(col, (boardSize - row - col - 1)) == mark) {
                    currentRowCol++;
                    if (currentRowCol == winStreak) {
                        return true;
                    }
                } else currentRowCol = 0;
            }
        }
        return false;
    }
}

