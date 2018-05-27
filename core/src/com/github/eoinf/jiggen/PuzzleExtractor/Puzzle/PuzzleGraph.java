package com.github.eoinf.jiggen.PuzzleExtractor.Puzzle;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.GraphEdge;

import java.util.ArrayList;

public class PuzzleGraph {
    private VertexList vertices;
    private EdgeList edges;

    public class VertexList extends ArrayList<PuzzlePiece> {
        @Override
        public String toString() {
            return "";
        }
    }

    public class EdgeList extends ArrayList<GraphEdge> {
        @Override
        public String toString() {
            return super.toString();
        }
    }

    public VertexList getVertices() {
        return vertices;
    }

    public EdgeList getEdges() {
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
        vertices = new VertexList();
        edges = new EdgeList();
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
