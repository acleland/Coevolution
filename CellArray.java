
public class CellArray {
    private boolean[] cells;

    public CellArray (int length) {
        cells = new boolean[length];
        setAllZero();
    }

    public int get(int index) {
        if (cells[index]) {
            return 1;
        }
        return 0;
    }

    public int length() {
        return cells.length;
    }

    public void setOne(int i) {
        cells[i] = true;
    }

    public void setZero(int i) {
        cells[i] = false;
    }

    public void setAllZero() {
        for (int i = 0; i < length(); i++) {
            setZero(i);
        }
    }

    public void setAllOne() {
        for (int i = 0; i < length(); i++) {
            setOne(i);
        }
    }

    public void flip(int i) {
        cells[i] = !cells[i];
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