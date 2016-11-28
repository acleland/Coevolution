
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
        computeFitnesses();
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

    public void computeFitnesses() {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                computeFitness(i,j);
            }
        }
    }

    public static double density(CellArray cells) {
        return (double) cells.numOnes()/cells.length();
    }

    public double getICFitness(CellArray ic, boolean correct) {
        if (correct) {
            return 0.0;
        }
        return Math.abs(density(ic) - 0.5);
    }
}