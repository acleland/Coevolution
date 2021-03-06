import java.util.*;

public class test {
    public static void cellArrayTest() {
        CellArray cells = new CellArray(10);
        System.out.println(cells);
        cells.setAllOne();
        System.out.println(cells);
        for (int i = 0; i < cells.length(); i++) {
            if (i%2 != 0) {
                cells.flip(i);
            } 
        }
        System.out.println(cells);

        System.out.println("get 9: " + cells.get(9));
        System.out.println("get -1: " + cells.get(-1));
    }

    public static void neighborhoodTest(int neighborhoodSize) {
        CellArray cells = new CellArray(6);
        cells.setAllOne();
        cells.setZero(1);
        cells.setZero(3);
        System.out.println(cells);
        System.out.println("neighborhood size: " + neighborhoodSize);
        CA ca = new CA(neighborhoodSize);
        for(int index = 0; index < cells.length(); index++) {
            System.out.printf("neighborhood at %d: %d\n", index, ca.getNeighborhood(index, cells));
        }
    }

    public static void neighborhoodTest128() {
         CellArray cells = new CellArray(149);
         cells.initRandomUnbiased();
         System.out.println(cells);
         CA ca = new CA(3);
         for(int index = 0; index < 10; index++) {
            System.out.printf("neighborhood at %d: %d\n", index, ca.getNeighborhood(index, cells));
        }
        System.out.println("Rule Table length: " + ca.length());
    }

    public static void cellUpdateTest(int length) {
        CA ca = new CA();
        System.out.println("Rule table:\n" + ca.getRuleTable());
        CellArray ic = new CellArray(length);
        ic.initRandomUnbiased();
        System.out.println("Initial state:\n" + ic);
        CellArray nextState = ca.getNextState(ic);
        System.out.println("Next state:\n" + nextState);
    }

    public static void runTest(int length, int maxIter) {
        CA ca = new CA();
        System.out.println(ca.getRuleTable());
        CellArray ic = new CellArray(length);
        ic.initRandomUnbiased();
        ca.run(ic, maxIter);
    }

    public static void CoevGridTest(int N) {
        CoevGrid grid = new CoevGrid(N);
        for (int i=0; i < grid.size(); i++) {
            for (int j=0; j < grid.size(); j++) {
                grid.set(i, j, new CoevPair(new CA(), new CellArray(149)));
            }
        }
        System.out.println(grid.getCA(0,0).getRuleTable()); 
        System.out.println(grid.getIC(0,0));
    }

    public static void initUniformTest(int N) {
        CellArray cells = new CellArray(N);
        cells.initWithDensity(.61);
        System.out.println(cells);
        System.out.printf("Num 1s: %d\n", cells.numOnes());
        cells.initUniform();
        System.out.println(cells);
        System.out.printf("Num 1s: %d\n", cells.numOnes());
    }


    public static void chooseTest() {
        double[] probs = {.1, .2};
        int[] counts = {0, 0, 0};
        int val = 0;
        int N = 10000;
        for (int i = 0; i < N; i++) {
            val = CoevGrid.choose(probs);
            System.out.println(val);
            switch(val) {
                case 0: counts[0] +=1;
                        break;
                case 1: counts[1] +=1;
                        break;
                case 2: counts[2] +=1;
                        break;

            }

        }
        System.out.printf("\n0: %.2f\n 1: %.2f\n 2: %.2f\n", (double)counts[0]/N, (double)counts[1]/N, (double)counts[2]/N);
    }

    public static void computeFitnessesTest() {
        CoevGrid coev = new CoevGrid(10);
        coev.initUniform();
        coev.print();
        System.out.println("Computing Fitnesses");
        coev.computeFitnesses(false);
        coev.printCAFitnesses();
        coev.printICFitnesses();

        List<CoevPair> neighbors = coev.getNeighbors(4,4);
        System.out.println(neighbors);

        coev.sortByCAFitness(neighbors);
        System.out.println(neighbors);
        CoevPair caParent = coev.selectByRank(neighbors);
        System.out.println(caParent);

        coev.sortByICFitness(neighbors);
        System.out.println(neighbors);
        CoevPair icParent = coev.selectByRank(neighbors);
        System.out.println(icParent);
        
    }

    public static void crossoverTest() {
        CellArray one = new CellArray(21);
        one.initRandomUnbiased();
        CellArray two = new CellArray(21);
        two.initRandomUnbiased();

        System.out.println(one);
        System.out.println(two);

        CellArray three = one.crossover(two);
        System.out.println(three);
    }

    public static void parentSelectionTest() {
        CoevGrid coev = new CoevGrid(3);
        coev.initUniform();
        coev.print();
        System.out.println("Computing Fitnesses");
        coev.computeFitnesses(false);
        coev.printCAFitnesses();
        coev.printICFitnesses();

        System.out.println("Selecting Parents");
        CoevGrid coev2 = coev.selectParents();
        coev2.print();   
    }

    public static void mutateTest() {
        CellArray cells = new CellArray(20);
        cells.initRandomUnbiased();
        System.out.println(cells);
        cells.mutate(.1);
        System.out.println(cells);
    }

    public static void main(String[] args) {
        mutateTest();
    }
}