public class CoevPair {
    private CA ca;
    private CellArray ic;
    private double caFitness = 0.0;
    private double icFitness = 0.0;

    public CoevPair(CA cellularAutomata, CellArray initialCondition) {
        this.ca = cellularAutomata;
        this.ic = initialCondition;
    }




    // Obligatory get and set methods.

    public CA getCA() {
        return ca;
    }

    public CellArray getIC() {
        return ic;
    }

    public double getCAFitness() {
        return caFitness;
    }

    public double getICFitness() {
        return icFitness;
    }

    public void setCA(CA ca) {
        this.ca = ca;
    }

    public void setIC(CellArray ic) {
        this.ic = ic;
    }

    public void setCAFitness(double caFitness) {
        this.caFitness = caFitness;
    }

    public void setICFitness(double icFitness) {
        this.icFitness = icFitness;
    }




}