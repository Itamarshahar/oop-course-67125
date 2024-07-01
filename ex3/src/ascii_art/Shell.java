package ascii_art;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.TreeSet;
import ascii_art.ascii_exception.*;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import ascii_output.OutputType;
import image.Image;
import image.PaddedImage;
import image_char_matching.SubImgCharMatcher;

/**
 * The Shell class handles the user interface operations for the ASCII art application.
 * It translates user commands and executes them.
 */
public class Shell {

    // Error and message constants
    private static final String ADD_ERROR_MESSAGE = "Did not add due to incorrect format.";
    private static final String REMOVE_ERROR_MESSAGE = "Did not remove due to incorrect format.";
    private static final String RES_ERROR_MESSAGE = "Did not change resolution due to %s.";
    private static final String RESOLUTION_FORMAT = "Resolution set to %d.";
    private static final String RESOLUTION_BOUNDARY_ERROR = "exceeding boundaries.";
    private static final String RESOLUTION_FORMAT_ERROR = "incorrect format";
    private static final String IMAGE_FORMAT_ERROR = "Did not change image method due to incorrect format.";
    private static final String IMAGE_LOADING_ERROR = "Did not execute due to problem with image file.";
    private static final String OUTPUT_EXCEPTION = "Did not change output method due to incorrect format.";
    private static final String EMPTY_CHARSET_EXCEPTION = "Did not execute. Charset is too small.";
    private static final String INVALID_COMMAND_EXCEPTION = "Did not execute due to incorrect command.";

    // Command constants
    private static final String DEF_STRING = ">>> ";
    private static final String EXIT = "exit";
    private static final String CHARS = "chars";
    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String RES = "res";
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final String IMAGE = "image";
    private static final String OUTPUT = "output";
    private static final String HTML = "html";
    private static final String CONSOLE = "console";
    private static final String ASCII_ART = "asciiArt";

    // Miscellaneous constants
    private static final String FONT = "Courier New";
    private static final int START_ASCII_ALL = 32;
    private static final int END_ASCII_ALL = 126;
    private static final char SPACE_CHAR = ' ';
    private static final String HYPHEN_CHAR = "-";
    private static final String EMPTY = "";
    private static final String IMAGE_SUFFIX = ".jpeg";
    private static final String OUT_FORMAT = "out.html";
    private static final int INITIAL_RESOLUTION = 128; // Initial number of characters per line
    private static final String DEF_IMAGE = "C:\\Users\\amirt\\IdeaProjects\\oop-course-67125\\ex3\\src\\examples\\cat.jpeg";
    private static final char[] DEF_CHARS = new char[]{'0','1', '2', '3', '4', '5', '6', '7', '8', '9'};

    // Character set instance
    private SubImgCharMatcher charSet = new SubImgCharMatcher(DEF_CHARS);

    // Tracks the current resolution
    private int currentResolution = INITIAL_RESOLUTION;

    // Output type
    OutputType output = OutputType.CONSOLE;

    // Image and resolution limits
    private Image img;
    int minResolution;
    int maxResolution;

    /**
     * Constructor initializes the Shell with the default image.
     * @throws IOException if there is an issue loading the default image.
     */
    public Shell() throws IOException {
        setImage(DEF_IMAGE);
    }

    // Set the image the algorithm is currently working on
    private void setImage(String fileName) throws IOException {
        Image imageToPad = new Image(fileName);
        PaddedImage paddedImage = new PaddedImage(imageToPad);
        this.img = paddedImage.padToPowerOfTwo();
        this.minResolution = Math.max(1, img.getWidth()/img.getHeight());
        this.maxResolution = this.img.getWidth();
    }

    /**
     * Starts the Shell and processes user commands.
     */
    public void run() {
        System.out.print(DEF_STRING);
        List<String> input = readString();
        while (!input.get(0).equals(EXIT)) {
            try {
                manageInput(input);
            } catch (AsciiException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.print(DEF_STRING);
            input = readString();
        }
    }

    // Manage the user input and run to relevant command.
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
            case RES:
                resCommandHandler(input);
                break;
            case IMAGE:
                imageCommandHandler(input);
                break;
            case OUTPUT:
                outputCommandHandler(input);
                break;
            case ASCII_ART:
                asciiArtCommandHandler(input);
                break;
            default:
                throw new InvalidCommandException(INVALID_COMMAND_EXCEPTION);
        }
    }
    private void asciiArtCommandHandler(List<String> input) throws InvalidCallException, IOException{
        if (this.charSet.getCharSet().size() < 1){
            throw new InvalidCallException(EMPTY_CHARSET_EXCEPTION);
        }
        char[] charSet = convertToCharArray(this.charSet.getCharSet());
        AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(this.img,charSet,this.currentResolution);
        AsciiOutput output;
        switch (this.output){
            case CONSOLE:
                output = new ConsoleAsciiOutput();
                break;
            default:
                output = new HtmlAsciiOutput(OUT_FORMAT, FONT);
                break;
        }
        output.out(algorithm.run());
    }
    // Convert TreeSet<Char> to a char array.
    private static char[] convertToCharArray(TreeSet<Character> treeSet) {
        char[] charArray = new char[treeSet.size()];
        int i = 0;
        for (Character c : treeSet) {
            charArray[i++] = c;
        }
        return charArray;
    }
    // Handle output command
    private void outputCommandHandler(List<String> input) throws InvalidInputException{
        switch (input.get(1)){
            case HTML:
                this.output = OutputType.HTML;
                return;
            case CONSOLE:
                this.output = OutputType.CONSOLE;
                return;
        }
        throw new InvalidInputException(OUTPUT_EXCEPTION);

    }
    // Handle add command
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
    // Handle remove command.
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
    // Handle resolution command.
    private void resCommandHandler(List<String> input) throws AsciiException {

        String subCommand = (input.size() == 1) ? EMPTY : input.get(1);
        if (!subCommand.equals(UP) && !subCommand.equals(DOWN)) {
            subCommand = EMPTY;
        }

        switch (subCommand) {
            case EMPTY:
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
    // Handle image command.
    private void imageCommandHandler(List<String> input) throws InvalidInputException, IOException {
        if (input.size() < 2) {
            throw new InvalidInputException(IMAGE_FORMAT_ERROR);
        }

        String fileName = input.get(1);
        if (!fileName.endsWith(IMAGE_SUFFIX)) {
            throw new InvalidInputException(IMAGE_FORMAT_ERROR);
        }
        try {
            setImage(fileName);
            if (currentResolution > maxResolution) {
                currentResolution = 2;
                System.out.println(String.format(RESOLUTION_FORMAT, currentResolution));
            }

        } catch (IOException e) {
            throw new IOException(IMAGE_LOADING_ERROR);
        }
    }
    // Handle resolution increasement.
    private void increaseResolution() throws AsciiException {
        int newResolution = currentResolution * 2;
        validateResolution(newResolution);
        currentResolution = newResolution;
        System.out.println(String.format(RESOLUTION_FORMAT, currentResolution));
    }
    // Handle resolution decreasement.
    private void decreaseResolution() throws AsciiException {
        int newResolution = currentResolution / 2;
        validateResolution(newResolution);
        currentResolution = newResolution;
        System.out.println(String.format(RESOLUTION_FORMAT, currentResolution));
    }
    // Check the resolution is valid
    private void validateResolution(int resolution) throws AsciiException {
        if (resolution < minResolution || resolution > maxResolution) {
            throw new ExceedingBoundaryException(String.format(RES_ERROR_MESSAGE, RESOLUTION_BOUNDARY_ERROR));
        }
    }
    // Add range of characters
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
    // Add all the ASCII characters
    private void addAllAsciiCharacters() {
        for (int i = START_ASCII_ALL; i <= END_ASCII_ALL; i++) {
            charSet.addChar((char) i);
        }
    }
    // remove all the ASCII characters
    private void removeAllAsciiCharacters() {
        for (int i = START_ASCII_ALL; i <= END_ASCII_ALL; i++) {
            charSet.removeChar((char) i);
        }
    }
    // Handle chars command
    private void charsCommandHandler() {
        Set<Character> characters = charSet.getCharSet();
        if (!characters.isEmpty()) {
            String result = characters.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(Character.toString(SPACE_CHAR)));
            System.out.println(result);
        }
    }
    // Parse the input and put each command part as a different list entry.
    private List<String> readString() {
        String input = KeyboardInput.readLine();
        return Arrays.asList(input.split(Character.toString(SPACE_CHAR)));
    }
    // Checks a character is in within the ASCII characters range
    private boolean isValidAsciiChar(char c) {
        return c >= START_ASCII_ALL && c <= END_ASCII_ALL;
    }
    /**
     * The main method serves as the entry point for the program.
     * It creates an instance of the Shell class and starts the user interface by calling the run method.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) throws Exception{
        Shell shell = new Shell();
        shell.run();
    }
}
