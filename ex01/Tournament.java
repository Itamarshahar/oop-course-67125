/**
 * The Tournament class is responsible for managing and running a tournament
 * of the game.
 * It takes in the necessary parameters to configure the tournament, such as the
 * number of rounds, the renderer to use, and the two players. It then runs the
 * tournament, keeping track of the scores and printing the final results.
 */
class Tournament {
    private final static String MSG_RESULTS = "######### Results #########\n" +
            "Player 1, %s won: %d  rounds\n" +
            "Player 2, %s won: %d  rounds\n" +
            "Ties: %d ";
    private final static String MSG_TYPO_CLI = "Usage: Please run the game " +
            "again: java Tournament [round count] [size] [win_streak] " +
            "[render target: console/none] " +
            "[first player: human/whatever/clever/genius] " +
            "[second player: human/whatever/clever/genius]";
    private int rounds;
    private Renderer renderer;
    private Player player1, player2;
    private int currentRound = 1;

    /**
     * Constructor that receives 4 arguments:
     *
     * @param rounds   the number of rounds to play
     * @param renderer the renderer to use for printing the game stages
     * @param player1  the first player
     * @param player2  the second player
     */
    Tournament(int rounds, Renderer renderer, Player player1, Player player2) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Runs the tournament by playing the specified number of rounds.
     *
     * @param size        the size of the game board
     * @param winStreak   the required win streak to win the game
     * @param playerName1 the name of the first player
     * @param playerName2 the name of the second player
     */
    void playTournament(int size, int winStreak, String playerName1,
                        String playerName2) {
        Game game;
        int[] scoreTracker = {0, 0, 0}; // scoreTracker = {wins_player1, wins_player2, ties}
        for (; currentRound <= rounds; currentRound++) {
            if (currentRound % 2 == 1) {
                game = new Game(player1, player2, size, winStreak, renderer);
            } else {
                game = new Game(player2, player1, size, winStreak, renderer);
            }
            Mark winnerMark = game.run();
            updateScoreTracker(winnerMark, scoreTracker, currentRound % 2);
        }
        System.out.println(String.format(MSG_RESULTS, playerName1,
                scoreTracker[0], playerName2, scoreTracker[1],
                scoreTracker[2]));
//        System.out.println(String.format("Smarter rate: %d",
//                scoreTracker[0]/100 ));
    }

    /**
     * Updates the score tracker based on the winner of a round.
     *
     * @param mark         the winning mark (X, O, or BLANK for a tie)
     * @param scoreTracker the array to track the scores
     * @param currentRound the current round index (0 or 1)
     */
    private static void updateScoreTracker(Mark mark, int[] scoreTracker,
                                           int currentRound) {
        switch (mark) {
            case O:
                scoreTracker[currentRound]++;
                break;
            case X:
                scoreTracker[1 - currentRound]++;
                break;
            case BLANK:
                scoreTracker[2]++;
                break;
        }
    }

    /**
     * The main entry point for the Tournament class.
     * It parses the command-line arguments, creates the necessary players and
     * renderer, and then runs the tournament.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String player1Name = args[4].toLowerCase();
        String player2Name = args[5].toLowerCase();

        PlayerFactory playerFactory = new PlayerFactory();
        Player player1 = playerFactory.buildPlayer(player1Name);
        Player player2 = playerFactory.buildPlayer(player2Name);

        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[3],
                Integer.parseInt(args[1]));
        // TODO maybe divide to 2 different errors?
        if (player1 == null || player2 == null || renderer == null) {
            System.out.println(MSG_TYPO_CLI);
            return;
        }
        Tournament tournament = new Tournament(Integer.parseInt(args[0]),
                renderer, player1, player2);
        tournament.playTournament(Integer.parseInt(args[1]),
                Integer.parseInt(args[2]), player1Name, player2Name);
    } // TODO Documentation
}