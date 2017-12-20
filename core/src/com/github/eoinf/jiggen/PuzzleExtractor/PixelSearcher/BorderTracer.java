package com.github.eoinf.jiggen.PuzzleExtractor.PixelSearcher;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.PuzzleExtractor.TemplatePiece;
import com.github.eoinf.jiggen.PuzzleExtractor.TemplatePieceBT;
import com.github.eoinf.jiggen.utils;

public class BorderTracer extends PixelSearcher {

    public enum BorderDirection {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        INNER_PIECE // This is a special value for when the pixel is inside of the border
    }

    // A 2d array of each border pixel which represent the direction of the outside of the piece
    private BorderDirection[][] borderDirections;

    public BorderTracer(Pixmap templatePixmap, int width, int height) {
        super(templatePixmap, width, height);
    }

    @Override
    void initSearchState(int startX, int startY) {
        super.initSearchState(startX, startY);
        borderDirections = new BorderDirection[height + 2][];

        for (int y = 0; y < borderDirections.length; y++) {
            borderDirections[y] = new BorderDirection[width + 2];
        }
    }

    public TemplatePiece extractPiece(int startX, int startY) {
        initSearchState(startX, startY);

        // The direction we are travelling
        int directionX = 1;
        int directionY = 0;

        // First find the border at the top of the piece
        while (!utils.isPixelDark(templatePixmap, startX, startY - 1, width, height)) {
            startY -= 1;
        }

        int currentX = startX;
        int currentY = startY;

        BorderDirection borderDirection = getBorderDirection(-directionY, directionX);

        // Trace out the border by moving in a clockwise direction while hugging the border
        do {
            int tmpX = directionX;
            int tmpY = directionY;

            // Check if the border is still there
            if (!utils.isPixelDark(templatePixmap, currentX + directionY, currentY - directionX, width, height)) {
                //
                // Border is not there, we need to change direction (clockwise)
                //

                /*
                 The state transition table is as follows:
                     ( 1,  0) -> ( 0, -1)
                     (-1,  0) -> ( 0,  1)
                     ( 0,  1) -> ( 1,  0)
                     ( 0, -1) -> (-1,  0)
                */

                directionX = tmpY;
                directionY = -tmpX;

                borderDirection = getBorderDirection(-directionY, directionX);
            }
            // Check if we can continue in the same direction
            else if (utils.isPixelDark(templatePixmap,currentX + directionX, currentY + directionY, width, height)) {
                /*
                 The state transition table is as follows:
                     ( 1,  0) -> ( 0,  1)
                     (-1,  0) -> ( 0, -1)
                     ( 0,  1) -> (-1,  0)
                     ( 0, -1) -> ( 1,  0)
                */
                directionX = -tmpY;
                directionY = tmpX;

                borderDirection = getBorderDirection(-directionY, directionX);
            }

            // Record the direction to get to the inside of the piece
            borderDirections[currentY + 1][currentX + 1] = borderDirection;

            // Move to the next point (adjacent to the border and clockwise)
            currentX += directionX;
            currentY += directionY;

            updateBounds(currentX, currentY);

        } while (currentX != startX || currentY != startY);

        adjustBorders();
        return new TemplatePieceBT(borderDirections, new GridPoint2(minX, minY), maxX, maxY);
    }

    private void updateBounds(int currentX, int currentY) {
        if (currentX > maxX) {
            maxX = currentX;
            maxXCoord = new GridPoint2(currentX, currentY);
        } else if (currentX < minX) {
            minX = currentX;
        }
        if (currentY > maxY) {
            maxY = currentY;
            maxYCoord = new GridPoint2(currentX, currentY);
        } else if (currentY < minY) {
            minY = currentY;
        }
    }

    private BorderDirection getBorderDirection(int directionX, int directionY) {
        if (directionX == 1) {
            return BorderDirection.RIGHT;
        } else if (directionX == -1) {
            return BorderDirection.LEFT;
        } else if (directionY == 1) {
            return BorderDirection.DOWN;
        } else {
            return BorderDirection.UP;
        }
    }
}
