public class CA {
    private CellArray ruleTable;
    private int length;

    public CA(int length, int neighborhoodSize) {
        ruleTable = new CellArray(1<<neighborhoodSize);
        this.length = length;
    }


    public static int pow(int base, int exponent) {
        if (exponent == 0) 
            return 1;
        if (exponent == 1)
            return base;
        if (exponent < 0)
            throw new IllegalArgumentException("Exponent must be 0 or greater");
        return base*pow(base, exponent-1);
    }
}