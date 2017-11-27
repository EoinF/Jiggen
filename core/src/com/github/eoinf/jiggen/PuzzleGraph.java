package com.github.eoinf.jiggen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PuzzleGraph {
    private List<PuzzlePiece> vertices;
    private List<int[]> edges;

    public List<PuzzlePiece> getVertices() {
        return vertices;
    }

    public GridPoint2 position;
    int width;
    int height;
    public float scale;

    public PuzzleGraph(int width, int height) {
        super();
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.scale = 1;
        this.position = new GridPoint2(0,0);
    }

    public void addVertex(PuzzlePiece piece) {
        Random random = new Random();
        float seed = random.nextFloat();
        vertices.add(piece);
        piece.setScale(this.scale);


        /* Color newColour = new Color(0.3f + (seed * 50) % 0.7f,
                0.3f + (seed * 200) % 0.7f,
                0.5f + (seed / 2) % 0.5f,
                1);

        piece.setColor(newColour);
        */
    }

    public void shuffle() {
        Random random = new Random();

        for (PuzzlePiece piece: vertices) {
            float r1 = random.nextFloat();
            float r2 = random.nextFloat();
            piece.setPosition(r1 * width + position.x, r2 * height + position.y);
            if (r1 + r2 > 1){
                piece.toFront();
            }
        }
    }

    public void setScale(float scale) {
        this.scale = scale;
        for (PuzzlePiece vertex: vertices) {
            vertex.setScale(this.scale);
        }
    }

    public void addEdge(int vert1, int vert2) {
        edges.add(new int[] {vert1, vert2});
    }

    /**
     * Flip all of the pieces
     */
    public void flipPieces() {
        for (PuzzlePiece piece: vertices) {
            int x = (int)piece.getPosition().x;
            int y = height - (int)piece.getPosition().y - piece.height;
            piece.setPosition(x, y);
        }
    }

    public void setPosition(int x, int y) {
        int diffX = x - this.position.x;
        int diffY = y - this.position.y;
        for (Actor actor: this.getVertices()) {
            actor.setPosition(actor.getX() + diffX, actor.getY() + diffY);
        }
        this.position = new GridPoint2(x, y);
    }
}
