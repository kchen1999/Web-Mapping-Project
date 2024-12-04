package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.LinkedList;

public class Solver {
    private LinkedList<WorldState> list = new LinkedList<>();

    private class SearchNode implements Comparable<SearchNode> {
        private WorldState ws;
        private SearchNode prev;
        private int moves;
        private int priority;

        private SearchNode(WorldState w, SearchNode p, int m) {
            ws = w;
            prev = p;
            moves = m;
            priority = moves + ws.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode node) {
            return priority - node.priority;
        }
    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        SearchNode initialSNode = new SearchNode(initial, null, 0);
        SearchNode goal = null;
        pq.insert(initialSNode);

        while (!pq.isEmpty()) {
            SearchNode min = pq.delMin();
            if (min.ws.isGoal()) {
                goal = min;
                break;
            }
            for (WorldState ws : min.ws.neighbors()) {
                if (min.prev == null || !ws.equals(min.prev.ws)) {
                    pq.insert(new SearchNode(ws, min, min.moves + 1));
                }
            }
        }
        while (goal != null) {
            list.addFirst(goal.ws);
            goal = goal.prev;
        }
    }
    public int moves() {
        return list.size() - 1;
    }
    public Iterable<WorldState> solution() {
        //System.out.println(list);
        return list;
    }
}
