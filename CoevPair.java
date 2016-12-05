import java.util.*;

public class CoevPair implements Comparable<CoevPair> {
    private CA ca;
    private CellArray ic;
    private double caFitness = 0.0;
    private double icFitness = 0.0;
    private boolean caFitnessComputed = false;
    private boolean icFitnessComputed = false;

    public CoevPair(CA cellularAutomata, CellArray initialCondition) {
        this.ca = cellularAutomata;
        this.ic = initialCondition;
    }

    // Accessor methods

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
        caFitnessComputed = true;
    }

    public void setICFitness(double icFitness) {
        this.icFitness = icFitness;
        icFitnessComputed = true;
    }

    public void clearFitnesses() {
        caFitness = 0.0;
        icFitness = 0.0;
        caFitnessComputed = false;
        icFitnessComputed = false;
    }

    // For printing
    @Override
    public String toString() {
        String s = "(";
        if (caFitnessComputed) {
            s += String.format("%.2f", getCAFitness());
        }
        else {
            s += "None";
        }
        s += ", ";
        if (icFitnessComputed) {
            s += String.format("%.2f", getICFitness());
        } 
        else {
            s += "None";
        }
        s += ")";
        return s;  
    }

    // For comparisons / Sorting
    // In the default case, sort by CA Fitness
    public int compareTo(CoevPair cp) {
        return (int) Math.signum(this.getCAFitness() - cp.getCAFitness());
    }

    // To sort by IC fitness, we need to define a comparator.

    public static Comparator<CoevPair> IC_Comparator 
        = new Comparator<CoevPair>() {
            public int compare(CoevPair cp1, CoevPair cp2) {
                return (int) Math.signum(cp1.getICFitness() - cp2.getICFitness());
            }
    };   

    // I may as well make one for CA comparisons as well.
    public static Comparator<CoevPair> CA_Comparator 
        = new Comparator<CoevPair>() {
            public int compare(CoevPair cp1, CoevPair cp2) {
                return (int) Math.signum(cp1.getCAFitness() - cp2.getCAFitness());
            }
    };
    

}