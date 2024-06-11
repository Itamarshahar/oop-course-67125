package lab4.src;

class WaterSource{
   String name="Water Source";
}

class Ocean extends WaterSource{
   String name="Ocean";

   void printName(){
       System.out.println(super.name);
       System.out.println(name);
   }
   
   public static void main(String[] args) {
       Ocean sea = new Ocean();
       sea.printName();
   }
}


