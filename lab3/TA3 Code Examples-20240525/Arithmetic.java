interface ArithmeticOperation {
    /*  public, static and final! */
    String INVALID_OPERATION_MESSAGE = "This operation is invalid";
    float calculateOperation(float op1, float op2);
}

class MultOperation implements ArithmeticOperation {
    //implement calculateOperation

    @Override
    public float calculateOperation(float op1, float op2) {
        return op1*op2;
    }
}

class DivideOperation implements ArithmeticOperation {
    //implement calculateOperation

    @Override
    public float calculateOperation(float op1, float op2) {
        if(op2==0){
            System.out.println(INVALID_OPERATION_MESSAGE);
            return Float.POSITIVE_INFINITY;
        }
        return op1/op2;
    }
}

class FuncCalculator {
    private final ArithmeticOperation[] calculations;
    private final float[] constants;

    public FuncCalculator(ArithmeticOperation[] calculations, float[] constants) {
        this.calculations = calculations;
        this.constants = constants;
    }

    public float calculateFunction(float x) {
        float result = 0;
        for (int i = 0; i < this.calculations.length; i++) {
            result += this.calculations[i].calculateOperation(constants[i], x);
        }
        return result;
    }
}

class Arithmetic {
    public static void main(String[] args) {

        ArithmeticOperation[] operations=new ArithmeticOperation[]{
                new DivideOperation(),
                new MultOperation()
        };
        float[] constants = new float[]{3,5};
        // calculates 3/x + 5x
        FuncCalculator f=new FuncCalculator(operations,constants);
        System.out.println(f.calculateFunction(10));
    }
}
