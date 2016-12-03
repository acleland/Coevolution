
public class Coevolution {
    private CoevGrid grid;
    private double[][] caFitness;
    private double[][] icFitness;
    public static final int MAX_ITER = 300;
    public static final int ARRAY_SIZE = 149;

    public Coevolution(int N) {
        grid = new CoevGrid(N);
        caFitness = new double[N][N];
        icFitness = new double[N][N];
        initUniform();
        computeFitnesses(true);
    }

    public double[][] getCAFitnesses() {
        return caFitness;
    }

    public double[][] getICFitnesses() {
        return icFitness;
    }

    public int size() {
        return grid.length();
    }

    public void initUnbiased() {
        for (int i=0; i < grid.length(); i++) {
            for (int j=0; j < grid.length(); j++) {
                grid.set(i, j, new CA(), new CellArray(ARRAY_SIZE));
                grid.getCA(i,j).getRuleTable().initRandomUnbiased();
                grid.getIC(i,j).initRandomUnbiased();
            }
        }
    }

    public void initUniform() {
        for (int i=0; i < grid.length(); i++) {
            for (int j=0; j < grid.length(); j++) {
                grid.set(i, j, new CA(), new CellArray(ARRAY_SIZE));
                grid.getCA(i,j).getRuleTable().initUniform();
                grid.getIC(i,j).initUniform();
            }
        }
    }

    public void computeFitness(int row, int col, boolean printing) {
        CA ca = grid.getCA(row, col);
        CellArray ic = grid.getIC(row, col);
        CellArray finalState = new CellArray(ic.length());
        int numCorrect = 0;
        boolean correct = false;

        /* For each cell in neighborhood of (row, col), run the CA on the associated IC. The fraction correct
        is the fitness of the CA. */
        if (printing) {
            System.out.printf("Getting fitness for %d, %d...\n", row, col);
            System.out.println("CA rule table: ");
            System.out.println(grid.getCA(row, col));
        }

        for (int i = row-1; i <= row + 1; i++) {
            for (int j = col-1; j <= col+1; j++) {
                ic = grid.getIC(i,j);
                finalState = ca.run(ic, MAX_ITER);
                correct = isCorrect(ic, finalState);
                if (correct) {
                    numCorrect++;
                }

                if ((i == row) && (j == col)) {
                    icFitness[row][col] = getICFitness(ic, correct);
                }

                if (printing) {
                    System.out.printf("%d, %d:\n", i, j);
                    System.out.println(ic);
                    System.out.println(finalState);
                    if (correct) {
                        System.out.println("Correct");
                    }
                    else {
                        System.out.println("Incorrect");
                    }
                }
            }
        }

        if (printing) {
            System.out.printf("Fitness: %.2f\n", (numCorrect/9.0));
        }

        caFitness[row][col] = numCorrect/9.0; // There are nine cells in the neighborhood  
    }

    public void computeFitness(int row, int col) {
        computeFitness(row, col, false);
    }    

    public static boolean isCorrect(CellArray ic, CellArray fc) {
        /* Determines if the final configuration fc correctly classifies the initial configuration ic */
        if ((double)ic.numOnes()/ic.length() > 0.5) {
            return fc.isAllOne();
        }
        return fc.isAllZero();
    }

    public void computeFitnesses(boolean printing) {
        if (printing) {
            System.out.println("Computing Fitnesses...");
        }
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                computeFitness(i,j);
                if (printing) {
                    System.out.printf("(%d, %d) CA Fitness: %.2f IC Fitness %.2f\n", 
                        i, j, caFitness[i][j], icFitness[i][j]);
                }
            }
        }
    }

    public static double density(CellArray cells) {
        return (double) cells.numOnes()/cells.length();
    }

    public int adjustIndex(int i) {
        return (((i % size()) + size()) % size());  // Periodic boundary conditions
    }

    public double getICFitness(CellArray ic, boolean correct) {
        if (correct) {
            return 0.0;
        }
        return Math.abs(density(ic) - 0.5);
    }


    public void selectParent(int i, int j) {
    /*
        * for each neighbor of (i,j), collect associated ca,ic pair.
        * sort this collection by fitness
        * select CA or IC such that that each has a (.5)^rank probability of being
          selected. Except the last rank, which gets (.5)^8 so that it sums to 1.
        * Return the CA or IC?? 
    */
    }


    public static int choose(double[] probabilities) {
        /* 
        probabilities is an n-length array of values that should sum to less than 1.
        This function returns an integer in range [0, n] (inclusive!), with
        probability(i) = probabilities[i] for i = 0..n-1
                       = 1 - sum(probabilities) for i = n 
        */ 
        
        // Create partitions
        int n = probabilities.length;
        double[] partition = new double[n];
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += probabilities[i];
            partition[i] = sum;
        }

        // Validate input
        assert (sum <= 1);

        // Get random number in range 0..1. 
        double randomNum = Math.random();

        // Discover where in the parition the random number falls and return the appropriate value
        if (randomNum < partition[0]) return 0;
        for (int i = 1; i < n; i++) {
            if ((randomNum >= partition[i-1]) && (randomNum < partition[i])) {
                return i;
            }
        }
        return n;
    }

}

