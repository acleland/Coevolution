import java.util.Random;

public class CellArray {
    private boolean[] cells;

    public CellArray (int length) {
        cells = new boolean[length];
        setAllZero();
    }

    public CellArray(CellArray cellsToCopy) {
        this(cellsToCopy.length());
        for (int i = 0; i < length(); i++) {
            this.set(i, cellsToCopy.get(i));
        }
    }

    public int get(int index) {
        index = adjustIndex(index);
        if (cells[index]) {
            return 1;
        }
        return 0;
    }

    public int length() {
        return cells.length;
    }

    public void setOne(int i) {
        cells[adjustIndex(i)] = true;
    }

    public void setZero(int i) {
        cells[adjustIndex(i)] = false;
    }

    public void setAllZero() {
        for (int i = 0; i < length(); i++) {
            setZero(i);
        }
    }

    public void set(int index, int oneOrZero) {
        if (oneOrZero == 1) {
            setOne(index);
        }
        else {
            setZero(index);
        }
    }


    public void setAllOne() {
        for (int i = 0; i < length(); i++) {
            setOne(i);
        }
    }

    public void flip(int i) {
        i = adjustIndex(i);
        cells[i] = !cells[i];
    }

    public void swap(int i, int j) {
        int temp = this.get(i);
        this.set(i, this.get(j));
        this.set(j, temp);
    }

    public int adjustIndex(int i) {
        return (((i % length()) + length()) % length());  // Periodic boundary conditions
    }

    public void initRandomUnbiased() {
        this.setAllZero();
        for (int i=0; i < length(); i++) {
            if (Math.random() < 0.5) {
                this.flip(i);
            }
        }
    }

    public void init(boolean uniform) {
        if (uniform) {
            initUniform();
        }
        else {
            initRandomUnbiased();
        }
    }

    public void initWithDensity(double density) {
        assert ((density > 0) && (density < 1));
        int numOnes = (int) Math.round(density*length());
        this.setAllZero();
        for (int i=0; i < numOnes; i++) {
            setOne(i);
        }
        shuffle();
    }

    public void initUniform() {
        initWithDensity(Math.random());
    }

    public void shuffle() {
        Random rand = new Random();
        int first=0, second=0;
        for (int i = 0; i < 3*length(); i++) {
            first = rand.nextInt(length());
            second = rand.nextInt(length());
            swap(first, second);
        }
    }

    public boolean isAllZero() {
        for (int i = 0; i < cells.length; i++) {
            if (cells[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllOne() {
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i]) {
                return false;
            }
        }
        return true;
    }

    public int numOnes() {
        int sum = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i]) {
                sum++;
            }
        }
        return sum;
    }

    public double density() {
        return (double) numOnes()/length();
    }

    public CellArray crossover(CellArray other) {
        // This assumes this and other have the same length
        assert (this.length() == other.length());
        Random random = new Random();
        CellArray child1 = new CellArray(length());
        CellArray child2 = new CellArray(length());

        int crossoverPoint = random.nextInt(length());
        for (int i = 0; i < crossoverPoint; i++) {
            child1.set(i, this.get(i));
            child2.set(i, other.get(i));
        }

        for (int i = crossoverPoint; i < length(); i++) {
            child1.set(i, other.get(i));
            child2.set(i, this.get(i));
        }

        // Return one of the two children at random
        int flipCoin = random.nextInt(2);
        if (flipCoin == 0) {
            return child1;
        }
        return child2;
    }

    public void mutate(double probability) {
        for (int i = 0; i < length(); i++) {
            if (Math.random() < probability) {
                flip(i);
            }
        }
    }


    @Override
    public String toString() {
        int remainder = length()%4;
        String cellString = "";
        for (int i = 0; i < remainder; i++) {
            cellString += this.get(i);
        }
        for (int i = remainder; i < length(); i++) {
            
            cellString += this.get(i);
        }
        return cellString;
    }


}