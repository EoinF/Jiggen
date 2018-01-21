package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;

import java.util.ArrayList;
import java.util.List;

public class PuzzleGraph {
    private List<PuzzlePiece> vertices;
    private List<GraphEdge> edges;

    public List<PuzzlePiece> getVertices() {
        return vertices;
    }
    public List<GraphEdge> getEdges() {
        return edges;
    }

    private GridPoint2 position;
    private int width;
    private int height;
    public float scale;

    public GridPoint2 getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PuzzleGraph(int width, int height) {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.scale = 1;
        this.position = new GridPoint2(0,0);
    }

    public void addVertex(PuzzlePiece piece) {
        vertices.add(piece);
    }

    public void addEdge(int v0, int v1) {
        edges.add(new GraphEdge(v0, v1));
    }
}
