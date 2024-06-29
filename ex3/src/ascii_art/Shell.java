package ascii_art;
import java.util.Arrays;
import java.util.Set;

import image_char_matching.SubImgCharMatcher;

import java.util.List;

public class Shell {
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
    private static final char[] DEF_CHARS = {'0','1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private SubImgCharMatcher charSet = new SubImgCharMatcher(DEF_CHARS);

    public Shell(){
    }
    public void run(){
        System.out.print(DEF_STRING);
        List<String> input = readString();
        while (!input.equals(EXIT)){
            manageInput(input);
            System.out.print(DEF_STRING);
            input = readString();
        }

    }

    private void manageInput(List<String> input){
        switch (input.get(0)){
            case  CHARS:
                charsCommandHandler();
                break;
            case ADD:
                addCommandHandler(input);
                break;

        }

    }
    private void addCommandHandler(List<String> input) {
        String subCommand = input.get(1);
        if (subCommand.length() == 1 && subCommand.charAt(0) >= 32 && subCommand.charAt(0) <= 126) {
            // Case 2.4.1: add followed by a single character
            charSet.addChar(subCommand.charAt(0));
        } else if (subCommand.equals(ALL)) {
            // Case 2.4.2: add followed by "all"
            for (char c = START_ASCII_ALL; c <= END_ASCII_ALL; c++) {
                charSet.addChar(c);
            }
        } else if (subCommand.equals(SPACE)) {
            // Case 2.4.3: add followed by "space"
            charSet.addChar(SPACE_CHAR);
        } else if (subCommand.contains(HYPHEN_CHAR)) {
            // Case 2.4.4: add followed by a range of characters
            String[] range = subCommand.split(HYPHEN_CHAR);
            if (range.length == 2 && range[0].length() == 1 && range[1].length() == 1) {
                char start = range[0].charAt(0);
                char end = range[1].charAt(0);
                if (start >= START_ASCII_ALL && start <= END_ASCII_ALL && end >= START_ASCII_ALL && end <= END_ASCII_ALL) {
                    if (start < end) {
                        for (char c = start; c <= end; c++) {
                            charSet.addChar(c);
                        }
                    } else {
                        for (char c = start; c >= end; c--) {
                            charSet.addChar(c);
                        }
                    }
                }
            }
        }
    }
    private void charsCommandHandler(){
        StringBuilder result = new StringBuilder();
        Set<Character> chars = charSet.getCharSet();
        for (Character ch : chars) {
            result.append(ch).append(SPACE_CHAR);
        }
        System.out.println(result.toString().trim());
    }



    private List<String> readString(){
        String input = KeyboardInput.readLine();
        return Arrays.asList(input.split(" "));
    }

    public static void main(String[] args){
        Shell shell = new Shell();
        shell.run();
    }
}
