public class CA_test {
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
        for (int i=0; i < grid.length(); i++) {
            for (int j=0; j < grid.length(); j++) {
                grid.set(i, j, new CoevPair(new CA(), new CellArray(149)));
            }
        }
        System.out.println(grid.getCA(0,0).getRuleTable()); 
        System.out.println(grid.getIC(0,0));
    }
/*
    public static void CAFitnessTest(int N) {
        Coevolution coev = new Coevolution(N);
        coev.initUnbiased();
        double fitness = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                fitness = coev.getCAFitness(i,j);
                System.out.printf("%.2f ", fitness);
            }
            System.out.println();
        }
    }

    public static void CAFitnessTest2(int N) {
        Coevolution coev = new Coevolution(N);
        coev.initUnbiased();
        double fitness = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                fitness = coev.getCAFitness(i,j, true);
            }
        }
    }
*/
    public static void initUniformTest(int N) {
        CellArray cells = new CellArray(N);
        cells.initWithDensity(.61);
        System.out.println(cells);
        System.out.printf("Num 1s: %d\n", cells.numOnes());
        cells.initUniform();
        System.out.println(cells);
        System.out.printf("Num 1s: %d\n", cells.numOnes());
    }
/*
    public static void CAFitnessTestUniform(int N) {
        Coevolution coev = new Coevolution(N);
        coev.initUniform();
        double fitness = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                fitness = coev.getCAFitness(i,j);
                System.out.printf("%.2f ", fitness);
            }
            System.out.println();
        }
    }

    public static void CAFitnessTest2Uniform(int N) {
        Coevolution coev = new Coevolution(N);
        coev.initUniform();
        double fitness = 0.0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                fitness = coev.getCAFitness(i,j, true);
            }
        }
    }

    public static void fitnessGridTest(int N) {
        Coevolution coev = new Coevolution(N);
        coev.initUniform();
        double[][] fitnessGrid = coev.getFitnessGrid(true);
    }
*/
    public static void printArray(double[][] array) {
        int length = array.length;
        int width = array[0].length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                System.out.printf("%.2f ", array[i][j]);
            }
            System.out.println();
        }
    }

    public static void coevolutionTest(int N) {
        Coevolution coev = new Coevolution(N);
        double[][] caFitness = coev.getCAFitnesses();
        double[][] icFitness = coev.getICFitnesses();
        System.out.println("CA fitnesses:");
        printArray(caFitness);
        System.out.println("\nIC fitnesses");
        printArray(icFitness);
    }

    public static void main(String[] args) {
        coevolutionTest(20);
    }
}