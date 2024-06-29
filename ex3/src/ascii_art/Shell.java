package ascii_art;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ascii_art.ascii_exception.*;
import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

public class Shell {
    private static final String ADD_ERROR_MESSAGE = "Did not add due to incorrect format."; // Constant for add error message
    private static final String REMOVE_ERROR_MESSAGE = "Did not remove due to incorrect format."; // Constant for remove error message
    private static final String RES_ERROR_MESSAGE = "Did not change resolution due to %s."; // Constant for res error message
    private static final String RESOLUTION_FORMAT = "Resolution set to %d."; // Format for resolution message
    private static final String RESOLUTION_BOUNDARY_ERROR = "exceeding boundaries."; // Constant for resolution boundary error
    private static final String RESOLUTION_FORMAT_ERROR = "incorrect format"; // Resolution format error
    private static final String IMAGE_FORMAT_ERROR = "Did not change image method due to incorrect format."; // Image format error.
    private static final String DEF_STRING = ">>> ";
    private static final String EXIT = "exit";
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res"; // New command: res
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final String IMAGE = "image";
    private static final int START_ASCII_ALL = 32;
    private static final int END_ASCII_ALL = 126;
    private static final char SPACE_CHAR = ' ';
    private static final String HYPHEN_CHAR = "-";
    private static final int INITIAL_RESOLUTION = 128; // Initial number of characters per line
    private static final String DEF_IMAGE = "C:\\Users\\amirt\\IdeaProjects\\oop-course-67125\\ex3\\src\\examples\\cat.jpeg";
    private static final char[] DEF_CHARS = new char[]{'0','1', '2', '3', '4', '5', '6', '7', '8', '9'};

    // New char set instance
    private SubImgCharMatcher charSet = new SubImgCharMatcher(DEF_CHARS);

    private int currentResolution = INITIAL_RESOLUTION; // Track current resolution
    private Image img;
    int min_resolution = Math.max(1, img.getWidth()/img.getHeight());
    int max_resolution = this.img.getWidth();


    public Shell() throws IOException {
        setImage(DEF_IMAGE);
    }
    private void setImage(String fileName) throws IOException{
        Image imageToPad = new Image(fileName);
        PaddedImage paddedImage = new PaddedImage(imageToPad);
        this.img = paddedImage.padToPowerOfTwo();
    }

    public void run() {
        System.out.print(DEF_STRING);
        List<String> input = readString();
        while (!input.get(0).equals(EXIT)) {
            try {
                manageInput(input);
            }
            catch (AsciiException e) {
                System.out.println(e.getMessage());
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.print(DEF_STRING);
            input = readString();
        }
    }

    private void manageInput(List<String> input) throws AsciiException, IOException {
        switch (input.get(0)) {
            case CHARS:
                charsCommandHandler();
                break;
            case ADD:
                addCommandHandler(input);
                break;
            case REMOVE:
                removeCommandHandler(input);
                break;
            case RES: // Handle res command
                resCommandHandler(input);
                break;
            case IMAGE:
                imageCommandHandler(input);
                break;
            default:
                throw new InvalidInputException("Unknown command: " + input.get(0));
        }
    }

    private void addCommandHandler(List<String> input) throws AsciiException {
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

    private void removeCommandHandler(List<String> input) throws AsciiException {
        if (input.size() < 2) {
            throw new InvalidInputException(REMOVE_ERROR_MESSAGE);
        }

        String subCommand = input.get(1);
        if (subCommand.length() == 1 && isValidAsciiChar(subCommand.charAt(0))) {
            charSet.removeChar(subCommand.charAt(0));
        } else if (subCommand.equals(ALL)) {
            removeAllAsciiCharacters();
        } else {
            throw new InvalidInputException(REMOVE_ERROR_MESSAGE);
        }
    }

    private void resCommandHandler(List<String> input) throws AsciiException {

        String subCommand = (input.size() == 1) ? "" : input.get(1);
        if (!subCommand.equals(UP) && !subCommand.equals(DOWN)) {
            subCommand = "";
        }

        switch (subCommand) {
            case "":
                System.out.println(String.format(RESOLUTION_FORMAT, currentResolution));
                break;
            case UP:
                increaseResolution();
                break;
            case DOWN:
                decreaseResolution();
                break;
            default:
                throw new InvalidInputException(String.format(RES_ERROR_MESSAGE, RESOLUTION_FORMAT_ERROR));
        }
    }

    private void imageCommandHandler(List<String> input) throws IOException, InvalidInputException{
        if (input.size() < 2) {
            throw new InvalidInputException(IMAGE_FORMAT_ERROR);
        }
        setImage(input.get(1));
        if(currentResolution > max_resolution){
            currentResolution = 2;
        }
    }
    private void increaseResolution() throws AsciiException {
        int newResolution = currentResolution * 2;
        validateResolution(newResolution);
        currentResolution = newResolution;
        System.out.println(String.format(RESOLUTION_FORMAT, currentResolution));
    }

    private void decreaseResolution() throws AsciiException {
        int newResolution = currentResolution / 2;
        validateResolution(newResolution);
        currentResolution = newResolution;
        System.out.println(String.format(RESOLUTION_FORMAT, currentResolution));
    }

    private void validateResolution(int resolution) throws AsciiException {
        if (resolution < min_resolution || resolution > max_resolution) {
            throw new ExceedingBoundaryException(String.format(RES_ERROR_MESSAGE, RESOLUTION_BOUNDARY_ERROR));
        }
    }

    private void handleRangeAddition(String range) throws AsciiException {
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

    private void removeAllAsciiCharacters() {
        for (int i = START_ASCII_ALL; i <= END_ASCII_ALL; i++) {
            charSet.removeChar((char) i);
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

    public static void main(String[] args) throws Exception{
        Shell shell = new Shell();
        shell.run();
    }
}
