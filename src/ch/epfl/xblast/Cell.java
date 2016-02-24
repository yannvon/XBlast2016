package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LoicVandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Cell {

    public final static int COLUMNS = 15, ROWS = 13, COUNT = ROWS * COLUMNS;
    public final static List<Cell> ROW_MAJOR_ORDER = Collections.unmodifiableList(rowMajorOrder());
    public final static List<Cell> SPIRAL_ORDER = Collections.unmodifiableList(spiralOrder());;
    private final int x, y;

    /**
     * @return ArrayList<Cell> tableau de cellules dans l'ordre de lecture
     */
    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> order = new ArrayList<Cell>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                order.add(new Cell(j, i));
            }
        }
        return order;
    }

    // retourne le tableau de cellules dans l'ordre spirale
    private static ArrayList<Cell> spiralOrder() {
        ArrayList<Integer> ix = new ArrayList<Integer>();
        ArrayList<Integer> iy = new ArrayList<Integer>();
        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i);
        }
        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }
        boolean horizontal = true;
        ArrayList<Cell> spiral = new ArrayList<Cell>();
        while (!ix.isEmpty() && !iy.isEmpty()) {
            ArrayList<Integer> i1 = horizontal ? ix : iy;
            ArrayList<Integer> i2 = horizontal ? iy : ix;
            int c2 = i2.get(0);
            i2.remove(0);
            for (int c1 = 0; c1 < i1.size(); c1++) {
                spiral.add(horizontal ? new Cell(i1.get(c1), c2) : new Cell(c2,i1.get(c1) ));
            }
            Collections.reverse(i1);
            horizontal = !horizontal;
        }
        return spiral;

    }

    // constructeur
    public Cell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int rowMajorIndex() {
        return x + y * COLUMNS; // FIXME shouldn't it be COLUMNS?
    }

    /**
     * @param dir
     * @return
     */
    public Cell neighbor(Direction dir) {
        switch (dir) {
        case N:
            return new Cell(x, y - 1);
        case S:
            return new Cell(x, y + 1);
        case W:
            return new Cell(x - 1, y);
        case E:
            return new Cell(x + 1, y);
        }
        throw new Error();
    }

    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this.getClass().equals(that.getClass())) {
            return this.x == ((Cell) that).x() && this.y() == ((Cell) that).y();
        }
        return false;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
