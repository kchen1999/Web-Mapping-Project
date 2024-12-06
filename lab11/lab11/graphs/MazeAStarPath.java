package lab11.graphs;

import java.util.ArrayDeque;
/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private ArrayDeque<Integer> q = new ArrayDeque<>();

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        marked[s] = true;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        int min = Integer.MAX_VALUE;
        int minVertex = q.peek();
        for (Integer v : q) {
            if ((distTo[v] + h(v)) < min) {
                min = distTo[v] + h(v);
                minVertex = v;
            }
        }
        return minVertex;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        q.add(s);
        while (!q.isEmpty()) {
            int v = findMinimumUnmarked();
            q.remove(v);
            marked[v] = true;
            announce();
            if (v == t) {
                return;
            }
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    if (distTo[v] + 1 < distTo[w]) {
                        distTo[w] = distTo[v] + 1;
                        edgeTo[w] = v;
                    }
                    q.add(w);
                }
            }
        }

    }

    @Override
    public void solve() {
        astar(s);
    }

}

