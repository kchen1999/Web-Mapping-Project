import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Node implements Comparable<Node> {
    private long id;
    private String name;
    private double lat;
    private double lon;
    private Map<Long, Long> adj;
    private double distFromS;
    private double distToT;
    private Node prev;

    public Node(long id, double lon, double lat) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.adj = new HashMap<Long, Long>();
        this.distFromS = Double.MAX_VALUE;
        this.prev = null;
    }

    public long getId() {
        return this.id;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public Map<Long, Long> getAdj() {
        return this.adj;
    }

    public double getDistFromS() {
        return this.distFromS;
    }

    public Node getPrev() {
        return this.prev;
    }

    public void setDistFromS(double distFromS) {
        this.distFromS = distFromS;
    }

    public void setDistToT(double distToT) {
        this.distToT = distToT;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void addAdj(Long endNodeId, Long wayId) {
        this.adj.put(endNodeId, wayId);
    }

    @Override
    public int compareTo(Node w) {
        double k = (this.distFromS + this.distToT - w.distFromS - w.distToT);
        if ((int)k != 0) {
            return (int)k;
        }
        if (k > 0) {
            return 1;
        } else if (k < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
