public class CA {
    private CellArray ruleTable;
    private int neighborhoodSize = 3;

    public CA(int neighborhoodSize) {
        this.neighborhoodSize = neighborhoodSize;
        ruleTable = new CellArray(1<<(2*neighborhoodSize+1));
    }

    /*
    length = 2^(2n+1)  -> lg(length) = 2n + 1  -> n = [lg(length) - 1]/2
     */

    public CA() {
        this(3);  // By default, initialize with neighborhood of 3
    }

    public CA(CellArray ruleTable) {
        this.ruleTable = ruleTable;
    }

    public CellArray getRuleTable() {
        return ruleTable;
    }


      public int length() {
        return ruleTable.length();
    }

    public int neighborhoodSize() {
        return neighborhoodSize;
    }


    public int getNeighborhood(int cellIndex, CellArray cells) {
        int neighborhood = 0;
        int mask = 0;
        int counter = 1;
        int neighborhoodLength = 2 * neighborhoodSize + 1;
        for (int i= cellIndex-neighborhoodSize; i < cellIndex + neighborhoodSize+1; i++) {
            mask = cells.get(i) << (neighborhoodLength - counter);
            neighborhood = neighborhood | mask;
            /* Some printing for testing. 
            System.out.println("i: " + i);
            System.out.println("v: " + cells.get(i));
            System.out.println("counter: " + counter);
            System.out.println("neighborhoodLength - counter: " + (neighborhoodLength - cells.get(i)));
            System.out.println("mask: " + mask);
            System.out.println("neighborhood: " + neighborhood);
            */
            counter++;
        }
        return neighborhood;                  
    }

    public CellArray getNextState(CellArray oldState) {
        int neighborhood;
        CellArray nextState = new CellArray(oldState.length());
        for (int i = 0; i < oldState.length(); i++) {
            neighborhood = getNeighborhood(i, oldState);
            nextState.set(i, ruleTable.get(neighborhood));
        }
        return nextState;
    }

    public CellArray run(CellArray initialState, int maxIter, boolean printing) {
        CellArray state = initialState;
        if (printing) 
            System.out.println(initialState);
        for (int i = 0; i < maxIter; i++) {
            state = getNextState(state);
            if (printing)
                System.out.println(state);
            if (state.isAllOne() || state.isAllZero()) {
                break;
            }
        }
        return state;
    }

    public CellArray run(CellArray initialState, int maxIter) {
        return run(initialState, maxIter, false);
    }

    public CA crossover(CA other) {
        // Return new CA with rule tables crossed over with another CA
        // This assumes this and other have the same neighborhoodSize.
        assert (this.neighborhoodSize() == other.neighborhoodSize());
        return new CA(this.getRuleTable().crossover(other.getRuleTable()));
    }

    @Override
    public String toString() {
        return ruleTable.toString();
    }



    
}