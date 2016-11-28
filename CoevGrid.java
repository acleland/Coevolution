public class CoevGrid {
    private CoevPair[][] grid;
    
    public CoevGrid(int N) {
        grid = new CoevPair[N][N];
        for (int i=0; i < this.length(); i++) {
            for (int j=0; j < this.length(); j++) {
                this.set(i, j, new CoevPair(new CA(), new CellArray(149)));
            }
        }
    }

    public CoevPair get(int row, int col) {
        return grid[adjustIndex(row)][adjustIndex(col)];
    }

    public CA getCA(int row, int col) {
        return this.get(row, col).ca;
    }

    public CellArray getIC(int row, int col) {
        return this.get(row,col).ic;
    }

    public void set(int row, int col, CoevPair cp) {
        grid[adjustIndex(row)][adjustIndex(col)] = cp;
    }

    public void set(int row, int col, CA ca, CellArray ic) {
        this.set(row, col, new CoevPair(ca, ic));
    }

    public int adjustIndex(int i) {
        return (((i % length()) + length()) % length());  // Periodic boundary conditions
    }

    public int length() {
        return grid.length;
    }
}