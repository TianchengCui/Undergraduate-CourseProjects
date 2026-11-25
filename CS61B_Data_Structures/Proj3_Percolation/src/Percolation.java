import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    int N;
    WeightedQuickUnionUF per;
    WeightedQuickUnionUF full;
    boolean[] isopenArray;
    int top;
    int bottom;
    int numberOfOpenSites;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.N = N;
        per = new WeightedQuickUnionUF(N * N + 2);
        full = new WeightedQuickUnionUF(N * N + 1);
        isopenArray = new boolean[N * N];
        top = N * N;
        bottom = N * N + 1;
        numberOfOpenSites = 0;
    }

    public void open(int row, int col) {
        int idx = xyTo1D(row, col);
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (isopenArray[idx]) {
            return;
        }
        isopenArray[idx] = true;
        numberOfOpenSites += 1;
        if (row == 0) {
            per.union(idx, top);
            full.union(idx, top);
        }
        if (row == N - 1) {
            per.union(idx, bottom);
        }
        if (row > 0 && isOpen(row - 1, col)) {
            per.union(idx, xyTo1D(row - 1, col));
            full.union(idx, xyTo1D(row - 1, col));
        }
        if (row < N - 1 && isOpen(row + 1, col)) {
            per.union(idx, xyTo1D(row + 1, col));
            full.union(idx, xyTo1D(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {
            per.union(idx, xyTo1D(row, col - 1));
            full.union(idx, xyTo1D(row, col - 1));
        }
        if (col < N - 1 && isOpen(row, col + 1)) {
            per.union(idx, xyTo1D(row, col + 1));
            full.union(idx, xyTo1D(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int idx = xyTo1D(row, col);
        return isopenArray[idx];
    }

    public boolean isFull(int row, int col) {
        if (!inBounds(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int idx = xyTo1D(row, col);
        return full.connected(idx, top);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        if (per.connected(top, bottom)) {
            return true;
        }
        return false;
    }

    public int xyTo1D(int r, int c) {
        return  N * r + c;
    }
    private boolean inBounds(int row, int col) {
        return row >= 0 && row <= N - 1 && col >= 0 && col <= N - 1;
    }
}
