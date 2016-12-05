public class Coevolution {
    public static final int GRID_SIZE = 20;
    public static final int MAX_ITER = 10;  // It takes about 2 minutes to run 10 iterations. -> about 300 iterations per hour.

    // By default, the CAs and ICs are initialized using a random unbiased distribution. 
    // To use a uniform distribution, set the following to true
    public static final boolean CA_INIT_UNIFORM = false;   
    public static final boolean IC_INIT_UNIFORM = true;
    
    public static final double CROSSOVER_PROB = 0.2;  // Probability a parent CA will crossover each generation
    public static final double MUTATE_PROB = 0.01;  // Probability each bit of a CA or IC will mutate each generation

    

    public static void run(int maxIter) {
        CoevGrid coev = new CoevGrid(GRID_SIZE, CROSSOVER_PROB, MUTATE_PROB);
        CoevGrid nextGen;
        CoevPair bestCell;
        coev.init(CA_INIT_UNIFORM, IC_INIT_UNIFORM);
        System.out.println("Initial grid initialized.");
        // coev.print();  // Uncomment if you want to print all the (CA, IC) pairs in the initial grid

        for (int i = 1; i <= maxIter; i++) {
            System.out.printf("Iteration %d of %d, computing fitnesses:\n", i, maxIter);
            coev.computeFitnesses();
            coev.printCAFitnesses();
            coev.printICFitnesses();
            System.out.println();
            bestCell = coev.printBest();
            sampleRun(bestCell.getCA(), 300);
            System.out.println();

            nextGen = coev.selectParents();
            nextGen = nextGen.crossoverCAs();
            nextGen.mutate();

        }
    }

    public static void sampleRun(CA ca, int maxIter) {
        CellArray ic = new CellArray(149);
        ic.init(IC_INIT_UNIFORM);
        System.out.println("Sample run with random initial condition:");
        System.out.println(ic);
        System.out.printf("Density: %.2f\n", ic.density());
        CellArray finalState = ca.run(ic, maxIter,true);
        if (CoevGrid.isCorrect(ic, finalState)) {
            System.out.println("Correct");
        }
        else {
            System.out.println("Incorrect");
        }
    }

    public static void main(String[] args) {
        run(MAX_ITER);
    }
}