public class CA_test {
    public static void cellArrayTest() {
        CellArray cells = new CellArray(128);
        System.out.println(cells);
        cells.setAllOne();
        System.out.println(cells);
        for (int i = 0; i < cells.length(); i++) {
            if (i%2 != 0) {
                cells.flip(i);
            } 
        }
        System.out.println(cells);
    }

    public static void main(String[] args) {
        cellArrayTest();
        System.out.println(CA.pow(2,7));
    }
}