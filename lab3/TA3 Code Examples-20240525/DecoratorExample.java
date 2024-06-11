////interface Fish {
////	String getDescription();
////}
////
////class BabyFish implements Fish {
////	@Override
////	public String getDescription() {
////		return "fish";
////	}
////}
////interface FishDecorator extends Fish {}
////
////class Gold implements FishDecorator {
////	Fish fish;
////
////	public Gold(Fish fish) {
////		this.fish = fish;
////	}
////
////	@Override
////	public String getDescription() {
////		return "Gold "+fish.getDescription();
////	}
////}
////
////class Stripes implements FishDecorator {
////
////	Fish fish;
////	private String color;
////
////	public Stripes(Fish fish, String color) {
////		this.fish = fish;
////		this.color = color;
////	}
////
////	@Override
////	public String getDescription() {
////		return color+" Stripes "+fish.getDescription();
////	}
////}
////
////public class DecoratorExample {
////
////	public static void main(String[] args) {
////		Fish clownFish = new BabyFish();
////		clownFish = new Gold(clownFish);
////		clownFish = new Stripes(clownFish, "White");
////
////		//Alternative initilize
////		//Fish clownFish = new Stripes(new Gold(new BabyFish()),"White");
////		System.out.println("A Clown fish is: a "+clownFish.getDescription());
////	}
////}
//
//
//abstract class Fish {
//    abstract public String getDescription();
//}
//
//class BabyFish implements Fish {
//    @Override
//    public String getDescription() {
//        return "fish";
//    }
//}
//
//abstract class FishDecorator extends Fish {
//    Fish fish;
//
//    FishDecorator(Fish fish) {
//        this.fish = fish;
//    }
//
//    String getDescription() {
//        return this.fish.getDescription();
//    }
//}
//
//class Gold extends FishDecorator {
//    public Gold(Fish fish) {
//        super(fish);
//    }
//
//    @Override
//    public String getDescription() {
//        return "Gold " + super.getDescription();
//    }
//}
//
//class Stripes extends FishDecorator {
//    private final String color;
//
//    public Stripes(Fish fish, String color) {
//        super(fish);
//        this.color = color;
//    }
//
//    @Override
//    public String getDescription() {
//        return color + " Stripes " + fish.getDescription();
//    }
//}
//
//public class DecoratorExample {
//
//    public static void main(String[] args) {
//        Fish clownFish = new BabyFish();
//        clownFish = new Gold(clownFish);
//        clownFish = new Stripes(clownFish, "White");
//
//        //Alternative initilize
//        //Fish clownFish = new Stripes(new Gold(new BabyFish()),"White");
//        System.out.println("A Clown fish is: a " + clownFish.getDescription());
//    }
//}