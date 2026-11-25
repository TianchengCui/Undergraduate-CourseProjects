package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DirectedGraph {
    private HashMap<Integer, Set<Integer>> graph;
    private int edgeCount;

    public DirectedGraph() {
        graph = new HashMap<>();
        edgeCount = 0;
    }

    public void addNode(int v) {
        if (!graph.containsKey(v)) {
            graph.put(v, new HashSet<>());
        }
    }

    public void addEdge(int v, int w) {
        addNode(v);
        addNode(w);
        if (!graph.get(v).contains(w)) {
            graph.get(v).add(w);
            edgeCount += 1;
        }
    }

    public Set<Integer> adj(int v) {
        if (!graph.containsKey(v)) {
            return new HashSet<>();
        }
        return graph.get(v);
    }

    public int V() {
        return graph.size();
    }

    public int E() {
        return edgeCount;
    }
}
