//public class DrawingBoard {
//
//    public static void main(String[] args) {
//        DrawingBoard board = new DrawingBoard();
//        board.draw(new Circle(7));
//        board.draw(new Square(4));
//    }
//
//    public void draw(Shape shape) {
//        shape.draw();
//    }
//}
//
//
//class Shape {
//}
//interface Draw {
//    public void draw();
//
//}
//class Circle extends Shape implements Draw {
//    private final int radius;
//
//    public Circle(int radius) {
//        this.radius = radius;
//    }
//
//    public int getRadius() {
//        return this.radius;
//    }
//
//    @Override
//    public void draw() {
//         System.out.println("Drawing circle with radius " + (this.getRadius()));
//    }
//}
//
//class Square extends Shape implements Draw {
//    private final int width;
//
//    public Square(int width) {
//        this.width = width;
//    }
//
//    public int getWidth() {
//        return this.width;
//    }
//
//    @Override
//    public void draw() {
//        System.out.println("Drawing square with width " + (this.getWidth()));
//    }
//}
