public class CoevPair {
    public CA ca;
    public CellArray ic;

    public CoevPair(CA cellularAutomata, CellArray initialCondition) {
        this.ca = cellularAutomata;
        this.ic = initialCondition;
    }
}