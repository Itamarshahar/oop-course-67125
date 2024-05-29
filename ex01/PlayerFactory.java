/**
 * The PlayerFactory class is responsible for creating instances of various
 * Player implementations. It provides a centralized way to obtain Player
 * objects without needing to know the specific implementation details.
 *
 * @author itamarshahar2
 */
public class PlayerFactory {
    /**
     * Constructs a new PlayerFactory instance.
     */
    public PlayerFactory() {
    }

    /**
     * Builds a new Player instance based on the specified type.
     *
     * @param type the type of Player to create, which can be one of "human",
     *             "clever", "whatever", or "genius"
     * @return a new Player instance of the specified type
     */
    public Player buildPlayer(String type) {
        Player player = null;
        switch (type.toLowerCase()) {
            case "human":
                player = new HumanPlayer();
                break;
            case "clever":
                player = new CleverPlayer();
                break;
            case "whatever":
                player = new WhateverPlayer();
                break;
            case "genius":
                player = new GeniusPlayer();
                break;
        }
        return player;
    }
}