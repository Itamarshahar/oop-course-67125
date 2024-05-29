import java.util.Random;

class WhateverPlayer implements Player {
    Random random = new Random();

    WhateverPlayer() {
    }

    /**
     * run a single turn of the Whatever Player
     *
     * @param board the board to play
     * @param mark  the type to mark
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int randomIndex = random.nextInt((board.getSize())
                * (board.getSize()));
        while (!board.putMark(mark, randomIndex / board.getSize(),
                randomIndex % board.getSize())) {
            randomIndex = random.nextInt((board.getSize())
                    * (board.getSize()));
        }
    }
}