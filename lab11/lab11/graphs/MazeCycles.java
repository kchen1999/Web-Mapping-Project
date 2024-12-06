package lab11.graphs;

import java.util.Stack;
import java.util.HashSet;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private Maze maze;
    private boolean cycleFound = false;
    int cycle;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {
        dfs(maze.xyTo1D(1, 1));
        displayCycle(cycle);
    }

    private void dfs(int v) {

    }

    private void displayCycle(int c) {

    }


}

