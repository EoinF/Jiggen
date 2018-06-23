package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzleGraphTemplate {
    private Map<Integer, PuzzlePieceTemplate> vertices;
    private List<GraphEdge> edges;

    public Map<Integer, PuzzlePieceTemplate> getVertices() {
        return vertices;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    private GridPoint2 position;
    private int width;
    private int height;

    public GridPoint2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PuzzleGraphTemplate(int width, int height) {
        vertices = new HashMap<>();
        edges = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.position = new GridPoint2(0,0);
    }

    public void addVertex(PuzzlePieceTemplate piece) {
        vertices.put(vertices.values().size(), piece);
    }

    public void addEdge(int v0, int v1) {
        edges.add(new GraphEdge(v0, v1));
    }
}
