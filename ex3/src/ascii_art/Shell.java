package ascii_art;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ascii_art.ascii_exception.InvalidInputException;
import image_char_matching.SubImgCharMatcher;

public class Shell {
    private static final String ADD_ERROR_MESSAGE = "Did not add due to incorrect format."; // Constant for error message
    private static final String DEF_STRING = ">>> ";
    private static final String EXIT = "exit";
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final int START_ASCII_ALL = 32;
    private static final int END_ASCII_ALL = 126;
    private static final char SPACE_CHAR = ' ';
    private static final String HYPHEN_CHAR = "-";

    private SubImgCharMatcher charSet = new SubImgCharMatcher(new char[]{'0','1', '2', '3', '4', '5', '6', '7', '8', '9'});

    public Shell() {
    }

    public void run() {
        System.out.print(DEF_STRING);
        List<String> input = readString();
        while (!input.get(0).equals(EXIT)) {
            try {
                manageInput(input);
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
            System.out.print(DEF_STRING);
            input = readString();
        }
    }

    private void manageInput(List<String> input) throws InvalidInputException {
        switch (input.get(0)) {
            case CHARS:
                charsCommandHandler();
                break;
            case ADD:
                addCommandHandler(input);
                break;
        }
    }

    private void addCommandHandler(List<String> input) throws InvalidInputException {
        if (input.size() < 2) {
            throw new InvalidInputException(ADD_ERROR_MESSAGE);
        }

        String subCommand = input.get(1);
        switch (subCommand) {
            case ALL:
                addAllAsciiCharacters();
                break;
            case SPACE:
                charSet.addChar(SPACE_CHAR);
                break;
            default:
                if (subCommand.length() == 1 && isValidAsciiChar(subCommand.charAt(0))) {
                    charSet.addChar(subCommand.charAt(0));
                } else if (subCommand.contains(HYPHEN_CHAR)) {
                    handleRangeAddition(subCommand);
                } else {
                    throw new InvalidInputException(ADD_ERROR_MESSAGE);
                }
        }
    }

    private void handleRangeAddition(String range) throws InvalidInputException {
        String[] rangeParts = range.split(HYPHEN_CHAR);
        if (rangeParts.length != 2 || rangeParts[0].length() != 1 || rangeParts[1].length() != 1) {
            throw new InvalidInputException(ADD_ERROR_MESSAGE);
        }

        char start = rangeParts[0].charAt(0);
        char end = rangeParts[1].charAt(0);

        if (!isValidAsciiChar(start) || !isValidAsciiChar(end)) {
            throw new InvalidInputException(ADD_ERROR_MESSAGE);
        }

        int startInt = start;
        int endInt = end;
        if (startInt > endInt) {
            for (int i = startInt; i >= endInt; i--) {
                charSet.addChar((char) i);
            }
        } else {
            for (int i = startInt; i <= endInt; i++) {
                charSet.addChar((char) i);
            }
        }
    }

    private void addAllAsciiCharacters() {
        for (int i = START_ASCII_ALL; i <= END_ASCII_ALL; i++) {
            charSet.addChar((char) i);
        }
    }

    private void charsCommandHandler() {
        Set<Character> characters = charSet.getCharSet();
        if (!characters.isEmpty()) {
            String result = characters.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" "));
            System.out.println(result);
        }
    }

    private List<String> readString() {
        String input = KeyboardInput.readLine();
        return Arrays.asList(input.split(" "));
    }

    private boolean isValidAsciiChar(char c) {
        return c >= START_ASCII_ALL && c <= END_ASCII_ALL;
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }
}
