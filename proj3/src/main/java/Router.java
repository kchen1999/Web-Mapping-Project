import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    private static void relax(GraphDB g, PriorityQueue<Node> fringe, Node v, Node w, Node t) {
        double d = v.getDistFromS() + g.distance(v.getId(), w.getId());
        if (d < w.getDistFromS()) {
            w.setDistFromS(d);
            w.setPrev(v);
            fringe.add(w);
        }
    }

    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        LinkedList<Long> list = new LinkedList<>();
        PriorityQueue<Node> fringe = new PriorityQueue<>();
        Set<Node> allVisitedNodes = new HashSet<>();
        Node s = g.getNode(g.closest(stlon, stlat));
        Node t = g.getNode(g.closest(destlon, destlat));
        fringe.add(s);
        s.setDistFromS(0.0);
        allVisitedNodes.add(s);
        while (!fringe.isEmpty()) {
            Node v = fringe.poll();
            if (v.getId() == t.getId()) {
                break;
            }
            for (Long endId : v.getAdj().keySet()) {
                Node w = g.getNode(endId);
                w.setDistToT(g.distance(w.getId(), t.getId()));
                relax(g, fringe, v, w, t);
                allVisitedNodes.add(w);
            }
        }
        while (t.getPrev() != null) {
            list.addFirst(t.getId());
            t = t.getPrev();
        }

        list.addFirst(s.getId());
        for (Node n : allVisitedNodes) {
            n.setDistFromS(Double.MAX_VALUE);
            n.setDistToT(0.0);
            n.setPrev(null);
        }
        return list;
    }

    private static int computeDirection(double prevBearing, double currentBearing) {
        double bearing = currentBearing - prevBearing;
        if (bearing >= -15f && bearing <= 15f) {
            return NavigationDirection.STRAIGHT;
        } else if (bearing >= -30f && bearing <= 30f) {
            if (bearing < 0) {
                return NavigationDirection.SLIGHT_LEFT;
            }
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (bearing >= -100f && bearing <= 100f) {
            if (bearing < 0) {
                return NavigationDirection.LEFT;
            }
            return NavigationDirection.RIGHT;
        } else if (bearing >= 180f || bearing <= -180f) {
            if (bearing < 0) {
                if (bearing <= -330f) {
                    return NavigationDirection.SLIGHT_RIGHT;
                }
                return NavigationDirection.RIGHT;
            }
            if (bearing >= 330f) {
                return NavigationDirection.SLIGHT_LEFT;
            }
            return NavigationDirection.LEFT;
        }
        if (bearing < 0) {
            return NavigationDirection.SHARP_LEFT;
        }
        return NavigationDirection.SHARP_RIGHT;
    }

    private static long getLastRouteNodeId(List<Long> route) {
        return route.get(route.size() - 1);
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> list = new ArrayList<>();
        int direction = NavigationDirection.START;
        double distance = 0f;
        long prevWayId = -1l;
        double prevBearing = 0f;
        Node v = null;
        for (Long id : route) {
            Node w = g.getNode(id);
            if (v != null) {
                long currentWayId = v.getAdj().get(id);
                double currentBearing = g.bearing(v.getId(), id);
                if (prevWayId > 0 && !g.getWay(currentWayId).equals(g.getWay(prevWayId))) {
                    list.add(new NavigationDirection(direction, g.getWay(prevWayId), distance));
                    direction = computeDirection(prevBearing, currentBearing);
                    distance = g.distance(v.getId(), id);
                    if (id == getLastRouteNodeId(route)) {
                        list.add(new NavigationDirection(direction, g.getWay(currentWayId), distance));
                    }
                } else {
                    distance += g.distance(v.getId(), id);
                    if (id == getLastRouteNodeId(route)) {
                        list.add(new NavigationDirection(direction, g.getWay(currentWayId), distance));
                    }
                }
                prevWayId = currentWayId;
                prevBearing = currentBearing;
            }
            v = w;
        }
        return list;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public NavigationDirection(int direction, String way, double distance) {
            this.direction = direction;
            this.way = way;
            this.distance = distance;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
