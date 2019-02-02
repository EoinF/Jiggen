package com.github.eoinf.jiggen.TemplateCreator.connectors;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.TemplateCreator.lines.Line;

public abstract class PieceConnector {
    private int x1;
    private int y1;
    protected Line shapingLine;
    private int distance;
    private GridPoint2 direction;

    public PieceConnector(int x1, int y1, int x2, int y2, ConnectorDirection direction) {

        switch (direction) {
            case UP:
                distance = x2 - x1;
                this.direction = new GridPoint2(0, 1);
                break;
            case DOWN:
                distance = x2 - x1;
                this.direction = new GridPoint2(0, 1);
                break;
            case LEFT:
                this.direction = new GridPoint2(1, 0);
                distance = y2 - y1;
                break;
            case RIGHT:
                this.direction = new GridPoint2(1, 0);
                distance = y2 - y1;
                break;
        }
        this.x1 = x1;
        this.y1 = y1;
    }

    public void draw(Pixmap pixmap) {
        int previousX = x1;
        int previousY = y1;
        for (int i = 0; i <= distance; i++) {
            int calculatedPoint = shapingLine.getY(i);
            int y = y1 + this.direction.x * i + this.direction.y * calculatedPoint;
            int x = x1 + this.direction.y * i + this.direction.x * calculatedPoint;

            // Draw all the x pixels on this line so there are no gaps
            pixmap.drawLine(previousX, previousY, x, y);
            previousX = x;
            previousY = y;
        }
    }
}
