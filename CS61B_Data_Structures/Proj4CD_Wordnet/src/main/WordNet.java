package main;

import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class WordNet {
    private HashMap<String, Set<Integer>> wordToIds;
    private HashMap<Integer, Set<String>> idToWords;
    private DirectedGraph graph;

    public WordNet(String synsetFile, String hyponymFile) {
        wordToIds = new HashMap<>();
        idToWords = new HashMap<>();
        graph = new DirectedGraph();

        readSynsets(synsetFile);
        readHyponyms(hyponymFile);
    }

    private void readSynsets(String synsetFile) {
        In in = new In(synsetFile);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split(",", 3);

            int id = Integer.parseInt(parts[0]);
            String[] words = parts[1].split(" ");

            Set<String> synsetWords = new HashSet<>();
            for (String word : words) {
                synsetWords.add(word);

                if (!wordToIds.containsKey(word)) {
                    wordToIds.put(word, new HashSet<>());
                }
                wordToIds.get(word).add(id);
            }

            idToWords.put(id, synsetWords);
            graph.addNode(id);
        }
    }

    private void readHyponyms(String hyponymFile) {
        In in = new In(hyponymFile);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split(",");

            int source = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i += 1) {
                int target = Integer.parseInt(parts[i]);
                graph.addEdge(source, target);
            }
        }
    }

    public Set<Integer> getIds(String word) {
        if (!wordToIds.containsKey(word)) {
            return new HashSet<>();
        }
        return new HashSet<>(wordToIds.get(word));
    }

    public Set<String> getWords(int id) {
        if (!idToWords.containsKey(id)) {
            return new HashSet<>();
        }
        return new HashSet<>(idToWords.get(id));
    }

    public Set<Integer> getNeighbors(int id) {
        return new HashSet<>(graph.adj(id));
    }

    private void dfs(int id, Set<Integer> visited) {
        visited.add(id);
        for (int next : graph.adj(id)) {
            if (!visited.contains(next)) {
                dfs(next, visited);
            }
        }
    }

    public Set<Integer> getReachableIds(String word) {
        Set<Integer> startIds = getIds(word);
        Set<Integer> visited = new HashSet<>();

        for (int id : startIds) {
            if (!visited.contains(id)) {
                dfs(id, visited);
            }
        }

        return visited;
    }

    public Set<String> hyponymsSet(String word) {
        Set<Integer> reachableIds = getReachableIds(word);
        Set<String> resultSet = new HashSet<>();

        for (int id : reachableIds) {
            resultSet.addAll(getWords(id));
        }

        return resultSet;
    }

    public Set<String> hyponyms(String word) {
        Set<String> resultSet = hyponymsSet(word);
        Set<String> sortedResult = new TreeSet<>();
        sortedResult.addAll(resultSet);
        return sortedResult;
    }

    public Set<String> hyponymsList(java.util.List<String> words) {
        Set<String> sortedResult = new TreeSet<>();

        if (words.isEmpty()) {
            return sortedResult;
        }

        boolean firstWord = true;
        Set<String> resultSet = new HashSet<>();

        for (String word : words) {
            Set<String> currentSet = hyponymsSet(word);

            if (firstWord) {
                resultSet.addAll(currentSet);
                firstWord = false;
            } else {
                resultSet.retainAll(currentSet);
            }
        }

        sortedResult.addAll(resultSet);
        return sortedResult;
    }
    public Set<String> hyponymsList(java.util.List<String> words,
                                    int startYear, int endYear, int k, NGramMap ngm) {
        Set<String> candidates = hyponymsList(words);

        HashMap<String, Double> counts = new HashMap<>();
        for (String word : candidates) {
            double total = 0.0;
            for (double value : ngm.countHistory(word, startYear, endYear).values()) {
                total += value;
            }
            if (total > 0) {
                counts.put(word, total);
            }
        }

        Set<String> resultSet = new HashSet<>();

        for (int i = 0; i < k; i += 1) {
            String bestWord = null;
            double bestCount = -1;

            for (String word : counts.keySet()) {
                double currentCount = counts.get(word);
                if (currentCount > bestCount) {
                    bestCount = currentCount;
                    bestWord = word;
                }
            }

            if (bestWord == null) {
                break;
            }

            resultSet.add(bestWord);
            counts.remove(bestWord);
        }

        Set<String> sortedResult = new TreeSet<>();
        sortedResult.addAll(resultSet);
        return sortedResult;
    }
}
