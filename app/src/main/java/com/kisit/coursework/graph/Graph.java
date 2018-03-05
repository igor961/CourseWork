package com.kisit.coursework.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Игорь on 25.01.2017.
 */
public class Graph {
    private double[][] adjMatrix;
    private int numberV;
    private static Graph instance;
    private boolean[] used;

    private Graph() {
    }

    public Graph init(double[][] matrix, int size) {
        this.adjMatrix = new double[size][size];
        this.numberV = size;
        this.adjMatrix = matrix;
        this.used = new boolean[size];
        return instance;
    }

    public static Graph getInstance() {
        if (instance == null)
            instance = new Graph();
        return instance;
    }

    public double[][] getAdjMatrix() {
        return adjMatrix;
    }

    public void normalizeM() {
        for (int i = 0; i < numberV; i++) {
            for (int j = 0; j < numberV; j++) {
                if (i > j) {
                    adjMatrix[i][j] = adjMatrix[j][i];
                } else if (i == j) {
                    adjMatrix[i][j] = 0;
                }
            }
        }
    }

    private int minKey(double[] key, boolean[] set, int verticesCount) {
        double min = Double.MAX_VALUE;
        int minIndex = 0;
        for (int v = 0; v < verticesCount; ++v) {
            if (set[v] == false && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private double[][] primMST(double[][] graph, int verticesCount) {
        int[] parent = new int[verticesCount];
        double[] key = new double[verticesCount];
        boolean[] mstSet = new boolean[verticesCount];
        for (int i = 0; i < verticesCount; ++i) key[i] = Double.MAX_VALUE;
        key[0] = 0;
        parent[0] = -1;

        for (int i = 0; i < verticesCount - 1; ++i) {
            int u = minKey(key, mstSet, verticesCount);
            mstSet[u] = true;
            for (int v = 0; v < verticesCount; ++v) {
                if (mstSet[v] == false && graph[u][v] < key[v] && graph[u][v] > 0) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        double[][] mat2 = new double[verticesCount][verticesCount];
        for (int i = 1; i < verticesCount; ++i) {
            mat2[parent[i]][i] = graph[i][parent[i]];
            mat2[i][parent[i]] = graph[i][parent[i]];
        }
        return mat2;
    }

    public double[][] getMST() {
        return primMST(adjMatrix, numberV);
    }

    public List<Integer> getBridges() {
        return null;
    }

    public List<Integer> getSorted() {
        List<Integer> sorted = new ArrayList<>();
        used = new boolean[numberV];
        for (int i = 0; i < numberV; i++)
            if (!used[i]) {
                Stack<Integer> stack = new Stack<>();
                stack.push(i);
                used[i] = true;
                while (!stack.empty()) {
                    int f = stack.peek();
                    stack.pop();
                    for (int j = 0; j < numberV; j++) {
                        if (used[j] && adjMatrix[f][j] != 0) return null;
                        stack.push(j);
                        if (!used[j] && adjMatrix[f][j] != 0)
                            used[j] = true;
                        else stack.pop();
                    }
                    sorted.add(f);
                }
            }
        return sorted;
    }

    public String getSortedAsString() {
        String tmp = "";
        List<Integer> tmpList = getSorted();
        if (tmpList != null)
            for (Integer i : tmpList) tmp += " " + i;
        else tmp = "Back or cross edge was found. Topological sort is not available.";
        return tmp;
    }

    public void setWeight(boolean oriented) {
        for (int i = 0; i < numberV; i++) {
            for (int j = 0; j < numberV; j++) {
                if (adjMatrix[i][j] > 0) {
                    if (oriented)
                        GView.edges.add(new Edge(GView.vertices.get(i), GView.vertices.get(j), adjMatrix[i][j]));
                    else if (i < j)
                        GView.edges.add(new Edge(GView.vertices.get(i), GView.vertices.get(j), adjMatrix[i][j]));
                }
            }
        }
    }
}
