import java.util.*;

/**
 * Base file for the ChatterBot exercise.
 * The bot's replyTo method receives a statement.
 * If it starts with the constant REQUEST_PREFIX, the bot returns
 * whatever is after this prefix. Otherwise, it returns one of
 * a few possible replies as supplied to it via its constructor.
 * In this case, it may also include the statement after
 * the selected reply (coin toss).
 *
 * @author Dan Nirel
 */
class ChatterBot {
    static final String REQUEST_PREFIX = "say ";
    static final String PLACEHOLDER_FOR_REQUESTED_PHRASE = "<phrase>";
    static final String PLACEHOLDER_FOR_ILLEGAL_REQUEST = "<request>";

    Random rand = new Random();
    String[] repliesToIllegalRequest;
    String[] repliesToLegalRequest;
    String name;

    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest ) {
        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
        for (int i = 0; i < repliesToLegalRequest.length; i = i + 1) {
            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
        }

        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
        for (int i = 0; i < repliesToIllegalRequest.length; i = i + 1) {
            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
        }

        this.name = name;
    }

    String replyTo(String statement) {
        if (statement.startsWith(REQUEST_PREFIX)) {
            //we don’t repeat the request prefix, so delete it from the reply
            statement =  statement.replaceFirst(REQUEST_PREFIX, "");
            return this.replyToLegalRequest(statement);
//            String phrase =  statement.replaceFirst(REQUEST_PREFIX, "");
//            String responsePattern = replyToLegalRequest(phrase);
//            String reply = responsePattern.replaceAll(PLACEHOLDER_FOR_REQUESTED_PHRASE, phrase);
//            return reply;
        }
        return replyToIllegalRequest(statement);
    }
//    String replyToIllegalRequest1(String statement) {
//        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
//        String reply = repliesToIllegalRequest[randomIndex];
//        if (rand.nextBoolean()) {
//            reply = reply + statement;
//        }
//        return reply;
//    }
    String replyToIllegalRequest(String statement) {
        return this.replacePlaceholderInARandomPattern(repliesToIllegalRequest, ChatterBot.PLACEHOLDER_FOR_ILLEGAL_REQUEST, statement);

    }
    //    String replyToLegalRequest1(String statement) {
//        int randomIndex = rand.nextInt(repliesToLegalRequest.length);
//        String reply = repliesToLegalRequest[randomIndex];
//        if (rand.nextBoolean()) {
//            reply = reply + statement;
//        }
//        return reply;
//    }
    String replyToLegalRequest(String statement) {
        return this.replacePlaceholderInARandomPattern(repliesToLegalRequest, ChatterBot.PLACEHOLDER_FOR_REQUESTED_PHRASE, statement);
//        return reply;
    }
    String replacePlaceholderInARandomPattern(String[] patterns, // array of optional answers
                                              String placeholder, // sub-string of that needs to be replaced
                                              String replacement) // string that will replace "placeholder"
    {
        int randomIndex = rand.nextInt(patterns.length);
        String reply = patterns[randomIndex].replaceAll(placeholder, replacement);
//        reply = reply.replaceAll(placeholder, replacement);
        return reply;
    };

    String getName() {
        return name;
    }
}
