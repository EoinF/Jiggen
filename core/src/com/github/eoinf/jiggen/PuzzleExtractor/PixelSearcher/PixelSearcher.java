package com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.TemplatePiece;
import com.github.eoinf.jiggen.utils;


public abstract class PixelSearcher {

    Pixmap templatePixmap;
    int width;
    int height;

    int maxX;
    int maxY;
    GridPoint2 maxXCoord;
    GridPoint2 maxYCoord;
    int minX;
    int minY;

    public PixelSearcher(Pixmap templatePixmap, int width, int height) {
        this.templatePixmap = templatePixmap;
        this.width = width;
        this.height = height;
    }


    public abstract TemplatePiece extractPiece(int startX, int startY);

    void initSearchState(int startX, int startY) {
        minX = maxX = startX;
        minY = maxY = startY;

        maxXCoord = new GridPoint2(startX, startY);
        maxYCoord = new GridPoint2(startX, startY);
    }

    void adjustBorders() {
        // Take the extra width of the border into account for the new templatePixmap
        maxX += 1;
        while(maxX < width
                && utils.isPixelDark(templatePixmap, maxX, maxXCoord.y, width, height)) {
            maxX++;
        }

        // Do the same for the y max
        maxY += 1;
        while(maxY < height
                && utils.isPixelDark(templatePixmap ,maxYCoord.x, maxY, width, height)) {
            maxY++;
        }

        minY--;
        minX--;
    }


}
