//interface ParentType {
//    void printType();
//}
//
//class Child implements ParentType {
//    public void printType() {
//        System.out.println("Child type");
//    }
//
//    void printChild() {
//        System.out.println("Child instance");
//    }
//}
//
//public class Upcast{
//    public static void main(String[] args) {
//        Child childInstance = new Child();
//        childInstance.printChild();   // both valid
//        childInstance.printType();   // and will compile
//        // upcasting to parent type
//        ParentType child = new Child();
//        child.printType(); // still OK
//        child.printChild(); // Does not compile!
//    }
//}