package com.github.eoinf.jiggen.PuzzleExtractor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.github.eoinf.jiggen.utils;

import java.util.HashSet;
import java.util.Set;

public class PixelSearcher {

    private Pixmap templatePixmap;
    private int width;
    private int height;

    // Represents each of the pixels traversed already
    private boolean[][] pixelsTraversed;
    private int maxX;
    private int maxY;
    private GridPoint2 maxXCoord;
    private GridPoint2 maxYCoord;
    private int minX;
    private int minY;

    public PixelSearcher(Pixmap templatePixmap, int width, int height) {
        this.templatePixmap = templatePixmap;
        this.width = width;
        this.height = height;

    }

    public GridPoint2 findNearestPixel(boolean targetValue, int currentX, int currentY) {
        GridPoint2[] currentPaths = new GridPoint2[] {new GridPoint2(currentX, currentY)};

        return findNearestPixel(targetValue, currentPaths);
    }

    private GridPoint2 findNearestPixel(boolean targetValue, GridPoint2[] currentPaths) {
        GridPoint2[] newPaths = new GridPoint2[currentPaths.length * 2];

        for (int p = 0; p < currentPaths.length; p++) {
            int x = currentPaths[p].x;
            int y = currentPaths[p].y;
            if (targetValue == utils.isPixelDark(templatePixmap, x, y, width, height)) {
                return new GridPoint2(x, y);
            } else {
                newPaths[p * 2] = new GridPoint2(x + 1, y);
                newPaths[p * 2 + 1] = new GridPoint2(x, y + 1);
            }
        }

        return findNearestPixel(targetValue, newPaths);
    }

    public void borderTracePiece(int startX, int startY) {
        // The direction we are travelling
        int directionX = 1;
        int directionY = 0;

        // First find the border at the top of the piece
        while (!utils.isPixelDark(templatePixmap, startX, startY - 1, width, height)) {
            startY -= 1;
        }

        int currentX = startX;
        int currentY = startY;

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
            // Check if we can continue in the same direction
            if (utils.isPixelDark(templatePixmap,currentX + directionX, currentY + directionY, width, height)) {
                /*
                 The state transition table is as follows:
                     ( 1,  0) -> ( 0,  1)
                     (-1,  0) -> ( 0, -1)
                     ( 0,  1) -> (-1,  0)
                     ( 0, -1) -> ( 1,  0)
                */
                directionX = -tmpY;
                directionY = tmpX;

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

            // Move to the next point (adjacent to the border and clockwise)
            currentX += directionX;
            currentY += directionY;

        } while (currentX != startX || currentY != startY);
    }


    /**
     * Map out the interior of the puzzle piece using a flood fill algorithm
     * @return
     */
    public TemplatePiece floodFillPiece(int startX, int startY) {
        pixelsTraversed = new boolean[height + 2][];
        // Map out the implicit border of the values outside the image
        for (int y = 0; y < pixelsTraversed.length; y++) {
            pixelsTraversed[y] = new boolean[width + 2];
            pixelsTraversed[y][0] = true;
            pixelsTraversed[y][width + 1] = true;
        }
        for (int x = 0; x < pixelsTraversed[0].length; x++) {
            pixelsTraversed[0][x] = true;
            pixelsTraversed[height + 1][x] = true;
        }

        maxXCoord = new GridPoint2(0, 0);
        maxYCoord = new GridPoint2(0, 0);
        maxX = 0;
        maxY = 0;
        minX = width;
        minY = height;

        Set<GridPoint2> paths = new HashSet<>();
        paths.add(new GridPoint2(startX, startY));
        fillNext(paths);

        adjustBorders();
        return new TemplatePiece(pixelsTraversed, new GridPoint2(minX, minY), maxX, maxY);
    }

    private void fillNext(Set<GridPoint2> currentPaths) {
        Set<GridPoint2> newPaths = new HashSet<>();
        for (GridPoint2 path: currentPaths) {
            if (pixelsTraversed[path.y + 1][path.x + 1]) {
                continue;
            }
            pixelsTraversed[path.y + 1][path.x + 1] = true;

            if (!utils.isPixelDark(templatePixmap, path.x, path.y, width, height)) {
                newPaths.add(new GridPoint2(path.x + 1, path.y));
                newPaths.add(new GridPoint2(path.x - 1, path.y));
                newPaths.add(new GridPoint2(path.x, path.y + 1));
                newPaths.add(new GridPoint2(path.x, path.y - 1));
            } else {
                if (path.x > maxX) {
                    maxX = path.x;
                    maxXCoord = new GridPoint2(path.x, path.y);
                } else if (path.x < minX) {
                    minX = path.x;
                }
                if (path.y > maxY) {
                    maxY = path.y;
                    maxYCoord = new GridPoint2(path.x, path.y);
                } else if (path.y < minY) {
                    minY = path.y;
                }

            }
        }

        if (newPaths.size() != 0) {
            fillNext(newPaths);
        }
    }

    private void adjustBorders() {
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
    }
}
