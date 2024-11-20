package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF wq;
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
        wq = new WeightedQuickUnionUF(N * N + 4);
        virtualTopSiteIndex = N * N;
        virtualBottomSiteIndex = N * N + 2;
        wq.union(virtualTopSiteIndex, N * N + 1);
        wq.union(virtualBottomSiteIndex, N * N + 3);
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

    public void open(int row, int col) {
        validateSite(row, col);
        if (!isOpen(row, col)) { //open the site if not open
            grid[row][col] = true;
            if (row == 0) { //top row attaches to virtual top root (of length two) first
                wq.union(xyToID(row, col), virtualTopSiteIndex);
            } else if (row == grid.length - 1 && isFull(row, col)) {
                //bottom row attaches to virtual bottom root (of length two) first only if it is connected to the top
                wq.union(xyToID(row, col), virtualBottomSiteIndex);
            }
            if (isOpenNeighbour(row + 1, col)) {
                wq.union(xyToID(row, col), xyToID(row + 1, col));
            }
            if (isOpenNeighbour(row - 1, col)) {
                wq.union(xyToID(row, col), xyToID(row - 1, col));
            }
            if (isOpenNeighbour(row, col + 1)) {
                wq.union(xyToID(row, col), xyToID(row, col + 1));
            }
            if (isOpenNeighbour(row, col - 1)) {
                wq.union(xyToID(row, col), xyToID(row, col - 1));
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
        return wq.connected(xyToID(row, col), virtualTopSiteIndex);
        /*for(int j = 0; j < grid.length; j++) { //Linear time
            if(isOpen(0 , j)) { //check for open sites in top row to prevent comparing top row to itself
                if(wq.connected(xyToID(row, col), xyToID(0, j))) {
                    return true;
                }
            }
        } */
    }

    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    public boolean percolates() {
        return wq.connected(virtualBottomSiteIndex, virtualTopSiteIndex);
        /*for(int i = 0; i < grid.length; i++) { //Quadratic time
            for(int j = 0; j < grid.length; j++) {
                if(wq.connected(xyToID(0, i), xyToID(grid.length - 1, j))) {
                    return true;
                }
            }
        }
        return false; */
    }

    public static void main(String[] args) {
        return;
    }
}
