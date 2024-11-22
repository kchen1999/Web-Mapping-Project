package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF wq;
    private WeightedQuickUnionUF wq1;
    private int numOfOpenSites;
    private int virtualTopSiteIndex;
    private int virtualBottomSiteIndex;

    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        grid = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }
        wq = new WeightedQuickUnionUF(N * N + 3);
        virtualTopSiteIndex = N * N;
        wq.union(virtualTopSiteIndex, N * N + 1);
        virtualBottomSiteIndex = N * N + 2;

        wq1 = new WeightedQuickUnionUF(N * N + 2);
        wq1.union(virtualTopSiteIndex, N * N + 1);
        numOfOpenSites = 0;
    }

    private void validateSite(int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private boolean isOpenNeighbour(int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid.length) {
            return false;
        }
        return isOpen(row, col);
    }

    private int xyToID(int row, int col) {
        return row * grid.length + col;
    }

    private void connectNeighbour(int row, int col, int nRow, int nCol) {
        //if neighbour is full and current is last row, connect last row to bottom root
        wq.union(xyToID(row, col), xyToID(nRow, nCol));
        wq1.union(xyToID(row, col), xyToID(nRow, nCol));
    }

    public void open(int row, int col) {
        validateSite(row, col);
        if (!isOpen(row, col)) { //open the site if not open
            grid[row][col] = true;
            if (row == 0) { //top row attaches to virtual top root (of length two) first
                wq.union(xyToID(row, col), virtualTopSiteIndex);
                wq1.union(xyToID(row, col), virtualTopSiteIndex);
            } else if (row == grid.length - 1) {
                wq.union(xyToID(row, col), virtualBottomSiteIndex);
            }
            if (isOpenNeighbour(row + 1, col)) {
                connectNeighbour(row, col, row + 1, col);
            }
            if (isOpenNeighbour(row - 1, col)) {
                connectNeighbour(row, col, row - 1, col);
            }
            if (isOpenNeighbour(row, col + 1)) {
                connectNeighbour(row, col, row, col + 1);
            }
            if (isOpenNeighbour(row, col - 1)) {
                connectNeighbour(row, col, row, col - 1);
            }

            numOfOpenSites++;
        }

    }

    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        validateSite(row, col);
        return wq1.connected(xyToID(row, col), virtualTopSiteIndex);
    }

    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    public boolean percolates() {
        return wq.connected(virtualBottomSiteIndex, virtualTopSiteIndex);
    }

    public static void main(String[] args) {

        return;
    }
}
