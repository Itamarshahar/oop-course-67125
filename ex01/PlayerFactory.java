public class PlayerFactory{
    public PlayerFactory(){}
    /**
     * build Player
     * @param type string type of player
     * @return Player
     */
    public Player buildPlayer(String type){
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