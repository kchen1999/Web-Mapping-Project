import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Trie {
    private Node root = new Node(false);

    private static class Node {
        private boolean isKey;
        private List<Long> locationIds;
        private String fullName;
        private HashMap<Character, Node> map;

        private Node(boolean b) {
            isKey = b;
            map = new HashMap<>();
            locationIds = new ArrayList<>();
        }
    }

    /** Clears all items out of Trie */
    void clear() {
        for (Character c : root.map.keySet()) {
            root.map.remove(c);
        }
    }

    /** Returns list of location ids if Trie contains KEY */
    List<Long> getLocationIds(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i += 1) {
            char c = key.toLowerCase().charAt(i);
            Node x = curr.map.get(c);
            if (x == null) {
                return new ArrayList<>();
            }
            curr = x;
        }
        return curr.locationIds;
    }

    /** Inserts string KEY and long node ID into Trie */
    void add(String key, long id) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i += 1) {
            char c = key.toLowerCase().charAt(i);
            Node x = curr.map.get(c);
            if (x == null) {
                curr.map.put(c, new Node(false));
                curr = curr.map.get(c);
            } else {
                curr = x;
            }

        }
        curr.isKey = true;
        curr.fullName = key;
        curr.locationIds.add(id);
    }

    void keysWithPrefix(Node x, StringBuilder prefix, List<String> list) {
        if (x.isKey) {
            list.add(x.fullName.toString());
        }
        for (char c : x.map.keySet()) {
            prefix.append(c);
            keysWithPrefix(x.map.get(c), prefix, list);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /** Returns a list of all words that start with PREFIX */
    List<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        List<String> list = new ArrayList<>();
        Node curr = root;
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.toLowerCase().charAt(i);
            Node x = curr.map.get(c);
            if (x == null) {
                return null;
            }
            curr = x;
        }
        StringBuilder sb = new StringBuilder(prefix);
        keysWithPrefix(curr, sb, list);
        return list;

    }
}