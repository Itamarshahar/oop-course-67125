import java.util.*;

/**
 * Base class for the exercise 0.
 *
 *
 */
class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    static final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";
    static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";

    Random rand = new Random();
    String name; // Bot name
    String[] repliesToIllegalRequest; // Array of replies to illegal requests
    String[] repliesToLegalRequest; // Array of replies to legal requests

    /**
     * Constructor for the ChatterBot class.
     *
     * @param name                   The name of the bot.
     * @param repliesToLegalRequest  Array of optional replies to legal
     *                               requests.
     * @param repliesToIllegalRequest  Array of optional replies to illegal
     *                                 requests.
     */
    ChatterBot(String name, String[] repliesToLegalRequest,
               String[] repliesToIllegalRequest) {
        this.repliesToLegalRequest = Arrays.copyOf(repliesToLegalRequest,
                repliesToLegalRequest.length);
        this.repliesToIllegalRequest = Arrays.copyOf(repliesToIllegalRequest,
                repliesToIllegalRequest.length);
        this.name = name;
    }

    /**
     * Replies to a statement. If the statement starts with REQUEST_PREFIX,
     * the bot returns whatever is after this prefix. Otherwise, it returns
     * a randomly selected reply from its legal or illegal replies array.
     *
     * @param statement  The statement to which the bot is replying.
     * @return           The bot's reply to the statement.
     */
    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            // Remove the request prefix
            String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
            return replyToLegalRequest(phrase);
        }
        return replyToIllegalRequest(statement);
    }

    /**
     * Replies to an illegal request
     *
     * @param statement  The illegal request statement.
     * @return           The bot's reply to the illegal request.
     */
    String replyToIllegalRequest(String statement) {
        return replacePlaceholderInARandomPattern(repliesToIllegalRequest,
                PLACEHOLDER_FOR_ILLEGAL_REQUEST, statement);
    }

    /**
     * Replies to a legal request
     *
     * @param statement  The legal request statement.
     * @return           The bot's reply to the legal request.
     */
    String replyToLegalRequest(String statement) {
        return replacePlaceholderInARandomPattern(repliesToLegalRequest,
                PLACEHOLDER_FOR_REQUESTED_PHRASE, statement);
    }

    /**
     * Replaces a placeholder in a randomly selected pattern from an array
     * of patterns.
     *
     * @param patterns      Array of optional answers.
     * @param placeholder   Substring that needs to be replaced.
     * @param replacement   String that will replace the placeholder.
     * @return              The string with replaced placeholder.
     */
    String replacePlaceholderInARandomPattern(String[] patterns,
                                              String placeholder,
                                              String replacement) {
        int randomIndex = rand.nextInt(patterns.length);
        String reply = patterns[randomIndex];
        return reply.replaceAll(placeholder, replacement);
    }

    /**
     * Returns the name of the bot.
     *
     * @return  The name of the bot.
     */
    String getName() {
        return name;
    }
}
