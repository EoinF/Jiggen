package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PuzzleGraphTemplate {
    private Map<Integer, PuzzlePieceTemplate> vertices;
    private Map<PuzzlePieceTemplate, Integer> vertexHelper;
    private Map<Integer, Set<Integer>> edgeHelper;
    private List<GraphEdge> edges;

    public Map<Integer, PuzzlePieceTemplate> getVertices() {
        return vertices;
    }

    public PuzzlePieceTemplate getVertex(int vertexNumber) {
        return vertices.get(vertexNumber);
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    private GridPoint2 position;
    private int width;
    private int height;
    private int maxPieceWidth;
    private int maxPieceHeight;

    public GridPoint2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxPieceWidth() {
        return this.maxPieceWidth;
    }
    public int getMaxPieceHeight() {
        return this.maxPieceHeight;
    }

    public PuzzleGraphTemplate(int width, int height) {
        vertices = new HashMap<>();
        edges = new ArrayList<>();

        edgeHelper = new HashMap<>();
        vertexHelper = new HashMap<>();

        this.width = width;
        this.height = height;
        this.position = new GridPoint2(0,0);
    }

    public void addVertex(PuzzlePieceTemplate piece) {
        int index = vertices.values().size();
        vertices.put(index, piece);
        vertexHelper.put(piece, index);
        this.maxPieceWidth = Math.max(this.maxPieceWidth, piece.getWidth());
        this.maxPieceHeight = Math.max(this.maxPieceHeight, piece.getHeight());
    }

    public void addEdge(int v0, int v1) {
        edges.add(new GraphEdge(v0, v1));
        setupHelperEdge(v0, v1);
        setupHelperEdge(v1, v0);
    }

    private void setupHelperEdge(int v0, int v1) {
        Set<Integer> edgesMappedForward = edgeHelper.get(v0);
        if (edgesMappedForward == null) {
            edgesMappedForward = new HashSet<>();
        }
        edgesMappedForward.add(v1);
        edgeHelper.put(v0, edgesMappedForward);
    }

    public boolean hasEdge(PuzzlePieceTemplate v0, PuzzlePieceTemplate v1) {
        int index1 = vertexHelper.get(v0);
        int index2 = vertexHelper.get(v1);
        return edgeHelper.get(index1).contains(index2);
    }
}
