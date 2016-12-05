import java.util.*;

public class CoevGrid {
    private CoevPair[][] grid;
    public static final int MAX_ITER = 300;
    public static final int ARRAY_SIZE = 149;
    private double CROSSOVER_PROB = 0.1;
    private double MUTATE_PROB = 0.01; 
    
    public CoevGrid(int N, boolean init) {
        grid = new CoevPair[N][N];
        if (init) {
            for (int i=0; i < this.size(); i++) {
                for (int j=0; j < this.size(); j++) {
                    this.set(i, j, new CoevPair(new CA(), new CellArray(149)));
                }
            }
        }
    }

    public CoevGrid(int N) {
        this(N, true);
    }

    public CoevGrid(int N, double crossoverProb, double mutateProb) {
        this(N);
        CROSSOVER_PROB = crossoverProb;
        MUTATE_PROB = mutateProb;

    }

    // Accessor stuff

    public CoevPair getCell(int row, int col) {
        return grid[adjustIndex(row)][adjustIndex(col)];
    }

    public CA getCA(int row, int col) {
        return this.getCell(row, col).getCA();
    }

    public CellArray getIC(int row, int col) {
        return this.getCell(row,col).getIC();
    }

    public void set(int row, int col, CoevPair cp) {
        grid[adjustIndex(row)][adjustIndex(col)] = cp;
    }

    public void set(int row, int col, CA ca, CellArray ic) {
        this.set(row, col, new CoevPair(ca, ic));
    }

    public int adjustIndex(int i) {
        return (((i % size()) + size()) % size());  // Periodic boundary conditions
    }

    public int size() {
        return grid.length;
    }


    // Coevolution Methods

    public void initUnbiased() {
        for (int i=0; i < this.size(); i++) {
            for (int j=0; j < this.size(); j++) {
                this.set(i, j, new CA(), new CellArray(ARRAY_SIZE));
                this.getCA(i,j).getRuleTable().initRandomUnbiased();
                this.getIC(i,j).initRandomUnbiased();
            }
        }
    }

    public void init(boolean CA_uniform, boolean IC_uniform) {
        for (int i=0; i < this.size(); i++) {
            for (int j=0; j < this.size(); j++) {
                this.set(i, j, new CA(), new CellArray(ARRAY_SIZE));
                this.getCA(i,j).getRuleTable().init(CA_uniform);
                this.getIC(i,j).init(IC_uniform);
            }
        }
    }

    public void initUniform() {
        for (int i=0; i < this.size(); i++) {
            for (int j=0; j < this.size(); j++) {
                this.set(i, j, new CA(), new CellArray(ARRAY_SIZE));
                this.getCA(i,j).getRuleTable().initUniform();
                this.getIC(i,j).initUniform();
            }
        }
    }

    public void init_CAUnbiased_ICUniform() {
        for (int i=0; i < this.size(); i++) {
            for (int j=0; j < this.size(); j++) {
                this.set(i, j, new CA(), new CellArray(ARRAY_SIZE));
                this.getCA(i,j).getRuleTable().initRandomUnbiased();
                this.getIC(i,j).initUniform();
            }
        }
    }

    public void computeFitness(int row, int col, boolean printing) {
        CA ca = this.getCA(row, col);
        CellArray ic = this.getIC(row, col);
        CellArray finalState = new CellArray(ic.length());
        int numCorrect = 0;
        boolean correct = false;

        /* For each cell in neighborhood of (row, col), run the CA on the associated IC. The fraction correct
        is the fitness of the CA. */
        if (printing) {
            System.out.printf("Getting fitness for %d, %d...\n", row, col);
            System.out.println("CA rule table: ");
            System.out.println(this.getCA(row, col));
        }

        for (int i = row-1; i <= row + 1; i++) {
            for (int j = col-1; j <= col+1; j++) {
                ic = this.getIC(i,j);
                finalState = ca.run(ic, MAX_ITER);
                correct = isCorrect(ic, finalState);
                if (correct) {
                    numCorrect++;
                }

                if ((i == row) && (j == col)) {
                    this.getCell(row, col).setICFitness(getICFitness(ic, correct));
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
        double caFitness = numCorrect/9.0;  // There are nine cells in the neighborhood  
        this.getCell(row, col).setCAFitness(caFitness); 
    }

    public void computeFitness(int row, int col) {   // Short hand for computing fitness without printing
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
                    System.out.println(getCell(i,j));
                }
            }
        }
    }

    public void computeFitnesses() {  // Shorthand for running without printing
        computeFitnesses(false);
    }

    public double getICFitness(CellArray ic, boolean correct) {
        if (correct) {
            return 0.0;
        }
        return Math.abs(ic.density() - 0.5);
    }

    public List<CoevPair> getNeighbors(int row, int col) {
        // Collect neighbors into a list
        List<CoevPair> neighbors = new ArrayList<CoevPair>();
        for (int i = row-1; i <= row+1; i++) {
            for (int j = col-1; j <= col+1; j++) {
                neighbors.add(getCell(i,j));
            }
        }
        return neighbors;  
    }

    public void sortByCAFitness(List<CoevPair> cells) {
        Collections.sort(cells, Collections.reverseOrder());
    }

    public void sortByICFitness(List<CoevPair> cells) {
        Collections.sort(cells, CoevPair.IC_Comparator.reversed());
    }

    public CoevPair selectByRank(List<CoevPair> sorted_cells) {
        int N = sorted_cells.size() - 1;  // Number of partitions
        double[] probabilities = new double[N];
        for (int i = 0; i < N; i++) {
            probabilities[i] = Math.pow(0.5, i+1);  // probability = (.5)^rank
        }
        return sorted_cells.get(choose(probabilities));
    }

    public CoevPair selectParent(int row, int col) {
    /*
        * for each neighbor of (i,j), collect associated ca,ic pair.
        * sort this collection by fitness
        * select CA or IC such that that each has a (.5)^rank probability of being
          selected. Except the last rank, which gets (.5)^8 so that it sums to 1.
        * Return the CA or IC?? 
    */
          List<CoevPair> neighbors = getNeighbors(row, col);
          
          // Select Parent CA
          sortByCAFitness(neighbors);
          CA parentCA = selectByRank(neighbors).getCA();

          // Select Parent IC
          sortByICFitness(neighbors);
          CellArray parentIC = selectByRank(neighbors).getIC();

          return new CoevPair(parentCA, parentIC);
    }

    public CoevGrid selectParents() {
        CoevGrid newGrid = new CoevGrid(size(), false);
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                newGrid.set(row, col, selectParent(row, col));
            }
        }
        return newGrid;
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
        assert (sum <= 1.0);

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

    public CoevGrid crossoverCAs() {
        CoevGrid newGrid = new CoevGrid(size(), false);
        CA currentCA;
        CA otherCA;
        List<CoevPair> neighbors;
        int index;
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                newGrid.set(row, col, this.getCell(row, col));
                if (Math.random() < CROSSOVER_PROB) {
                    currentCA = newGrid.getCA(row,col);
                    neighbors = getNeighbors(row, col);
                    index = (int) Math.random()*neighbors.size();
                    otherCA = neighbors.get(index).getCA();
                    newGrid.getCell(row, col).setCA(currentCA.crossover(otherCA));
                }
            }
        }
        return newGrid;
    }

    public void mutate() {
        for (int row = 0; row < size(); row++) {
            for (int col = 0; col < size(); col++) {
                getCell(row,col).getCA().getRuleTable().mutate(MUTATE_PROB);
                getCell(row,col).getIC().mutate(MUTATE_PROB);
            }
        }
    }

    public List<CoevPair> toList() {
        List<CoevPair> coevList = new ArrayList<CoevPair>();
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                coevList.add(getCell(i,j));
            }
        }
        return coevList;  
    }

    public CoevPair getBestCell() {
        List<CoevPair> coevList = this.toList();
        sortByCAFitness(coevList);
        return coevList.get(0);
    }

    // For printing
    public void print() {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.println("Cell " + i +", " + j);
                System.out.println("CA: " + getCell(i,j).getCA());
                System.out.println("IC: " + getCell(i,j).getIC());
            }
        }
    }

    public void printCAFitnesses() {
        System.out.println("CA Fitnesses:");
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.printf("%.2f ", getCell(i,j).getCAFitness());
            }
            System.out.println();
        }
    }

    public void printICFitnesses() {
        System.out.println("IC Fitnesses:");
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.printf("%.2f ", getCell(i,j).getICFitness());
            }
            System.out.println();
        }
    }

    public CoevPair printBest() {
        CoevPair best = getBestCell();
        System.out.println("Best CA: ");
        System.out.println(best.getCA());
        System.out.println("Fitness: " + best.getCAFitness());
        return best;
    }
}