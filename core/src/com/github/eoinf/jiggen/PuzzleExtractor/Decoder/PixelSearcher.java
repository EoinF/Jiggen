package com.github.eoinf.jiggen.PuzzleExtractor.Decoder;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Set;

import static com.github.eoinf.jiggen.utils.PixmapUtils.isPixelDark;

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
            if (targetValue == isPixelDark(templatePixmap, x, y, width, height)) {
                return new GridPoint2(x, y);
            } else {
                newPaths[p * 2] = new GridPoint2(x + 1, y);
                newPaths[p * 2 + 1] = new GridPoint2(x, y + 1);
            }
        }

        return findNearestPixel(targetValue, newPaths);
    }

    /**
     * Map out the interior of the puzzle piece using a flood fill algorithm
     * @return
     */
    public DecodedPiece floodFillPiece(int startX, int startY) {
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
        maxX = startX;
        maxY = startY;
        minX = startX;
        minY = startY;

        Set<GridPoint2> paths = new HashSet<>();
        paths.add(new GridPoint2(startX, startY));
        fillNext(paths);

        adjustBorders();
        return new DecodedPiece(pixelsTraversed, new GridPoint2(minX, minY), maxX, maxY);
    }

    private void fillNext(Set<GridPoint2> currentPaths) {
        Set<GridPoint2> newPaths = new HashSet<>();
        for (GridPoint2 path: currentPaths) {
            if (pixelsTraversed[path.y + 1][path.x + 1]) {
                continue;
            }
            pixelsTraversed[path.y + 1][path.x + 1] = true;

            if (!isPixelDark(templatePixmap, path.x, path.y, width, height)) {
                newPaths.add(new GridPoint2(path.x + 1, path.y));
                newPaths.add(new GridPoint2(path.x - 1, path.y));
                newPaths.add(new GridPoint2(path.x, path.y + 1));
                newPaths.add(new GridPoint2(path.x, path.y - 1));

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
        minX--;
        minY--;

        // Take the extra width of the border into account for the new templatePixmap
        maxX++;
        while(maxX < width
                && isPixelDark(templatePixmap, maxX, maxXCoord.y, width, height)) {
            maxX++;
        }

        // Do the same for the y max
        maxY++;
        while(maxY < height
                && isPixelDark(templatePixmap ,maxYCoord.x, maxY, width, height)) {
            maxY++;
        }
    }
}
