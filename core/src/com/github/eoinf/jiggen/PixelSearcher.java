package com.github.eoinf.jiggen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;

import java.util.HashSet;
import java.util.Set;

public class PixelSearcher {

    private Pixmap templatePixmap;
    private Pixmap backgroundPixmap;
    private int width;
    private int height;

    Color buffer;

    // Represents each of the pixels traversed already
    private boolean[][] pixelsTraversed;
    private int maxX;
    private int maxY;
    private GridPoint2 maxXcoord;
    private GridPoint2 maxYcoord;
    private int minX;
    private int minY;

    public static final float BRIGHTNESS_THRESHOLD = 0.95f;

    public PixelSearcher(Pixmap templatePixmap, Pixmap backgroundPixmap, int width, int height) {
        this.templatePixmap = templatePixmap;
        this.backgroundPixmap = backgroundPixmap;
        this.width = width;
        this.height = height;

        this.buffer = Color.BLACK.cpy();
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
            if (targetValue == isPixelDark(x, y)) {
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
    public void floodFillPiece(int startX, int startY) {
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

        maxX = 0;
        maxY = 0;
        minX = width;
        minY = height;

        Set<GridPoint2> paths = new HashSet<>();
        paths.add(new GridPoint2(startX, startY));
        fillNext(paths);
    }

    private void fillNext(Set<GridPoint2> currentPaths) {
        Set<GridPoint2> newPaths = new HashSet<>();
        for (GridPoint2 path: currentPaths) {
            if (pixelsTraversed[path.y + 1][path.x + 1]) {
                continue;
            }
            pixelsTraversed[path.y + 1][path.x + 1] = true;

            if (!isPixelDark(path.x, path.y)) {
                if (path.x > maxX) {
                    maxX = path.x;
                    maxXcoord = new GridPoint2(path.x, path.y);
                }
                if (path.y > maxY) {
                    maxY = path.y;
                    maxYcoord = new GridPoint2(path.x, path.y);
                }
                if (path.x < minX) {
                    minX = path.x;
                }
                if (path.y < minY) {
                    minY = path.y;
                }

                newPaths.add(new GridPoint2(path.x + 1, path.y));
                newPaths.add(new GridPoint2(path.x - 1, path.y));
                newPaths.add(new GridPoint2(path.x, path.y + 1));
                newPaths.add(new GridPoint2(path.x, path.y - 1));
            }
        }

        if (newPaths.size() != 0) {
            fillNext(newPaths);
        }
    }


    public PuzzlePiece generatePuzzlePiece(int startX, int startY) {
        // Take the extra width of the border into account for the new templatePixmap
        int maxXPastBorder = maxXcoord.x + 1;
        while(maxXPastBorder < width && isPixelDark(maxXPastBorder, maxXcoord.y)) {
            maxXPastBorder++;
        }

        // Do the same for the y max
        int maxYPastBorder = maxYcoord.y + 1;
        while(maxYPastBorder < height && isPixelDark(maxYcoord.x, maxYPastBorder)) {
            maxYPastBorder++;
        }

        minX -= 1;
        minY -= 1;

        int pixMapWidth = maxXPastBorder - minX;
        int pixMapHeight = maxYPastBorder - minY;

        Pixmap newPixMap = new Pixmap(pixMapWidth, pixMapHeight, Pixmap.Format.RGBA8888);
        newPixMap.setColor(Color.CLEAR);
        newPixMap.fill();


        for (int x = 0; x < pixMapWidth; x++) {
            for (int y = 0; y < pixMapHeight; y++) {
                boolean isDark = isPixelDark(x + minX, y + minY);
                // If we haven't traversed this ignore it
                if (pixelsTraversed[minY + y + 1][minX + x + 1]) {

                    Color background = isDark ?
                            Color.BLACK.cpy() :
                            getBackgroundPixelColour(x + minX, y + minY);
                    // We have traversed this pixel
                    newPixMap.drawPixel(x, y, Color.rgba8888(background));
                }
            }
        }

        return new PuzzlePiece(minX, minY, pixMapWidth, pixMapHeight, newPixMap);
    }

    private boolean isPixelDark(int currentX, int currentY) {
        return currentX >= width || currentY >= height || currentX < 0 || currentY < 0 ||
                utils.getBrightness(getPixelColour(currentX, currentY)) < BRIGHTNESS_THRESHOLD;
    }

    private Color getPixelColour(int currentX, int currentY) {
        Color.rgba8888ToColor(buffer, templatePixmap.getPixel(currentX, currentY));
        return buffer;
    }

    private Color getBackgroundPixelColour(int currentX, int currentY) {
        Color.rgba8888ToColor(buffer, backgroundPixmap.getPixel(currentX, currentY));
        return buffer;
    }
}
