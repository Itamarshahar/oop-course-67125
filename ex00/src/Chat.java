import java.util.Scanner;

/**
 * Class representing a conversation between two chatter bots.
 */
class Chat {
    /**
     * Main method to start the conversation.
     *
     * @param none
     */
    public static void main(String[] args) {
        // Replies to illegal requests for bot 1
        String[] repliesToIllegalRequestBot1 = new String[]{"what is "
                + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + "?",
                "say I should say " +
                        ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST};

        // Replies to illegal requests for bot 2
        String[] repliesToIllegalRequestBot2 = {"do you mean? " +
                ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST + " for what?",
                "say say " + ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST};

        // Replies to legal requests for bot 1
        String[] repliesToLegalRequestBot1 = new String[]{"You want me to say "
                + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + "? then:"
                + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE,
                "Do you want me to say " +
                        ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + "? " +
                        "alright: "
                        + ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};

        // Replies to legal requests for bot 2
        String[] repliesToLegalRequestBot2 = new String[]{"say " +
                ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE + "? okay: " +
                ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE};

        // Names of the bots
        String nameBot1 = "Sammy";
        String nameBot2 = "Ruthy";

        // Create chatter bots
        ChatterBot[] bots = {
                new ChatterBot(nameBot1, repliesToLegalRequestBot1,
                        repliesToIllegalRequestBot1),
                new ChatterBot(nameBot2, repliesToLegalRequestBot2,
                        repliesToIllegalRequestBot2)
        };

        // Starting statement
        String statement = "say Hi";
        Scanner scanner = new Scanner(System.in);

        // Conversation loop
        for (int i = 0; ; i++) {
            ChatterBot currentBot = bots[i % bots.length];
            statement = currentBot.replyTo(statement);
            System.out.print(currentBot.getName() + ":" + " " + statement);
            scanner.nextLine();
        }
    }
}
