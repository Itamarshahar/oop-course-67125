import ascii_art.AsciiArtAlgorithm;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import ascii_output.ConsoleAsciiOutput;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String[] paths = {
//                "/Users/itamar_shahar/IdeaProjects/oop-course-67125/ex3/src" +
//                        "/examples/board.jpeg",
                "/Users/itamar_shahar/IdeaProjects/oop-course-67125/ex3/src/examples/cat.jpeg"
        };
        try {
            for (String path : paths) {

                Image image = new Image(path);
                char[] charset = {'0', '1', '2', '3', '7', '8', '4', '5', '6', '9'};
//                char[] charset = {'!', '"', '#', '$', '%', '&', '\'',
//                        '(', ')', '*', '+', ',', '-', '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'}
 ;//, 'i', 'n',
                // 'a',
                int resolution = 128;
                // 'd', '.',
                // '6', '3'};
                AsciiArtAlgorithm algo = new AsciiArtAlgorithm(image, charset, resolution);
                char[][] res = algo.run();
                ConsoleAsciiOutput co = new ConsoleAsciiOutput();
                co.out(res);
                HtmlAsciiOutput hao = new HtmlAsciiOutput("board", "Courier New");
                hao.out(res);
            }
//            System.out.println(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("Hello world!");
    }
}