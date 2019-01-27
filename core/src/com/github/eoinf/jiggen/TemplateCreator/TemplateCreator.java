package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateCreator {
    private Pixmap pixmap;
    private Vector2 aspectRatio;
    private GridPoint2 dimensions;

    private TemplateLine[] linesHorizontal;
    private TemplateLine[] linesVertical;

    private TemplateLineWithDistortion[] distortedLinesHorizontal;
    private TemplateLineWithDistortion[] distortedLinesVertical;

    private GridPoint2 maxSize;
    private WaveDistortionData waveDistortionData;

    private void createNewPixmap() {
        float ratio = aspectRatio.x / aspectRatio.y;
        int width, height;
        if (maxSize.y * ratio < maxSize.x) {
            width = (int) ((maxSize.y * aspectRatio.x) / aspectRatio.y);
            height = maxSize.y;
        } else {
            width = maxSize.x;
            height = (int)((maxSize.x * aspectRatio.y) / aspectRatio.x);
        }
        if (this.pixmap != null) {
            this.pixmap.dispose();
        }
        this.pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        this.pixmap.setColor(Color.WHITE);
        this.pixmap.fill();

        this.pixmap.setColor(Color.BLACK);
        this.pixmap.drawRectangle(0, 0, width, height);
    }

    private void generateLineData() {
        linesHorizontal = new TemplateLine[dimensions.y - 1];
        linesVertical = new TemplateLine[dimensions.x - 1];

        for (int i = 0; i < linesHorizontal.length; i++) {
            int y = (int)((i + 1) * ((float)pixmap.getHeight() / dimensions.y));
            int x2 = pixmap.getWidth();
            linesHorizontal[i] = new TemplateLine(0, x2, y, false);
        }
        for (int i = 0; i < linesVertical.length; i++) {
            int x = (int)((i + 1) * ((float)pixmap.getWidth() / dimensions.x));
            int y2 = pixmap.getHeight();
            linesVertical[i] = new TemplateLine(0, y2, x, true);
        }
    }

    private void generateDistortedLineData() {
        distortedLinesHorizontal = new TemplateLineWithDistortion[linesHorizontal.length];
        distortedLinesVertical = new TemplateLineWithDistortion[linesVertical.length];
        for (int x = 0; x < linesHorizontal.length; x++) {
            distortedLinesHorizontal[x] = new TemplateLineWithDistortion(linesHorizontal[x], waveDistortionData);
        }
        for (int y = 0; y < linesVertical.length; y++) {
            distortedLinesVertical[y] = new TemplateLineWithDistortion(linesVertical[y], waveDistortionData);
        }
    }

    private void addPixmapLines() {
        pixmap.setColor(Color.BLACK);
        for (TemplateLineWithDistortion line: distortedLinesHorizontal) {
            drawLine(line);
        }
        for (TemplateLineWithDistortion line: distortedLinesVertical) {
            drawLine(line);
        }
    }

    private void drawLine(TemplateLineWithDistortion line) {
        for (GridPoint2 point: line.getPoints()) {
            pixmap.drawPixel(point.x, point.y);
        }
    }

    private void addPieceConnectors() {
        List<TemplateLineWithDistortion> distortedLinesVerticalWithEdge = new ArrayList<>(Arrays.asList(distortedLinesVertical));
        List<TemplateLineWithDistortion> distortedLinesHorizontalWithEdge = new ArrayList<>(Arrays.asList(distortedLinesHorizontal));

        distortedLinesVerticalWithEdge.add(
                new TemplateLineWithDistortion(
                        new TemplateLine(0, pixmap.getHeight(), pixmap.getWidth(), true),
                        new WaveDistortionData(0,0,0)
                )
        );
        distortedLinesHorizontalWithEdge.add(
                new TemplateLineWithDistortion(
                        new TemplateLine(0, pixmap.getWidth(), pixmap.getHeight(), false),
                        new WaveDistortionData(0,0,0)
                )
        );

        GridPoint2 previous = new GridPoint2(0, 0);
        for (TemplateLineWithDistortion horizontal: distortedLinesHorizontalWithEdge) {
            previous.x = 0;
            GridPoint2 current = new GridPoint2(0, 0);
            for (TemplateLineWithDistortion vertical: distortedLinesVerticalWithEdge) {
                current = horizontal.getIntersectionPoint(vertical);

                if (distortedLinesVerticalWithEdge.indexOf(vertical) < distortedLinesVerticalWithEdge.size() - 1) {
                    // Right side connector
                    int connectorY = (current.y + previous.y) / 2;
                    addHorizontalConnectorAt(connectorY, vertical, (current.y - previous.y) / 3f);
                }

                if (distortedLinesHorizontalWithEdge.indexOf(horizontal) < distortedLinesHorizontalWithEdge.size() - 1) {
                    int connectorX = (current.x + previous.x) / 2;
                    addVerticalConnectorAt(connectorX, horizontal, (current.x - previous.x) / 3f);
                }

                previous.x = current.x;
            }
            previous.y = current.y;
        }
    }


    private void addVerticalConnectorAt(int connectorX, TemplateLineWithDistortion horizontalLine, float connectorSize) {
        drawGapForVerticalConnector(connectorX, horizontalLine, (int)connectorSize);

        pixmap.setColor(Color.BLACK);
        int firstX = (int)(connectorX - (connectorSize / 2f));
        int firstY = horizontalLine.getY(firstX);

        int lastX = (int)(connectorX + (connectorSize / 2f));
        int lastY = horizontalLine.getY(lastX);

        int previousX = firstX;
        int previousY = firstY;

        for (int i = 0; i < connectorSize; i++) {
            float offset = i - connectorSize/2f;
            int x = connectorX + (int)offset;
            int y = horizontalLine.getY(x) + (int)((connectorSize / 2f) * Math.sin((i * Math.PI) / connectorSize));

            pixmap.drawLine(previousX, previousY, x, y);
            previousX = x;
            previousY = y;
        }

        pixmap.drawLine(previousX, previousY, lastX, lastY);
    }

    private void addHorizontalConnectorAt(int connectorY, TemplateLineWithDistortion verticalLine, float connectorSize) {
        drawGapForHorizontalConnector(connectorY, verticalLine, (int)connectorSize);

        pixmap.setColor(Color.BLACK);
        int firstY = (int)(connectorY - (connectorSize / 2f));
        int firstX = verticalLine.getX(firstY);

        int lastY = (int)(connectorY + (connectorSize / 2f));
        int lastX = verticalLine.getX(lastY);

        int previousX = firstX;
        int previousY = firstY;

        for (int i = 0; i < connectorSize; i++) {
            float offset = i - connectorSize/2f;
            int y = connectorY + (int)offset;
            int x = verticalLine.getX(y) + (int)((connectorSize / 2f) * Math.sin((i * Math.PI) / connectorSize));

            // Draw all the x pixels on this line so there are no gaps
            pixmap.drawLine(previousX, previousY, x, y);
            previousX = x;
            previousY = y;
        }

        pixmap.drawLine(previousX, previousY, lastX, lastY);
    }

    private void drawGapForHorizontalConnector(int connectorY, TemplateLineWithDistortion verticalLine, int connectorSize) {
        pixmap.setColor(Color.WHITE);
        for (int offset = -(connectorSize/2); offset < (connectorSize/2); offset++) {
            int y = connectorY + offset;
            int x = verticalLine.getX(y);
            pixmap.drawPixel(x, y);
        }
    }
    private void drawGapForVerticalConnector(int connectorX, TemplateLineWithDistortion horizontalLine, int connectorSize) {
        pixmap.setColor(Color.WHITE);
        for (int offset = -(connectorSize/2); offset < (connectorSize/2); offset++) {
            int x = connectorX + offset;
            int y = horizontalLine.getY(x);
            pixmap.drawPixel(x, y);
        }
    }

    public TemplateCreator() {
        this(
                new GridPoint2(50, 50),
                new Vector2(1, 1),
                new GridPoint2(2, 2),
                new WaveDistortionData(0,0,0));
    }

    public TemplateCreator(GridPoint2 maxSize,
                           Vector2 aspectRatio,
                           GridPoint2 dimensions,
                           WaveDistortionData waveDistortionData) {
        this.maxSize = maxSize;
        this.aspectRatio = aspectRatio;
        this.dimensions = dimensions;
        this.waveDistortionData = waveDistortionData;
        createNewPixmap();
        generateLineData();
        generateDistortedLineData();
        addPixmapLines();
        addPieceConnectors();
    }

    public void setWaveDistortion(WaveDistortionData waveDistortionData) {
        this.waveDistortionData = waveDistortionData;
        createNewPixmap();
        generateDistortedLineData();
        addPixmapLines();
        addPieceConnectors();
    }


    public void setMaxSize(GridPoint2 maxSize) {
        this.maxSize = maxSize;
        createNewPixmap();
        generateLineData();
        generateDistortedLineData();
        addPixmapLines();
        addPieceConnectors();
    }

    public void setAspectRatio(Vector2 aspectRatio) {
        this.aspectRatio = aspectRatio;
        createNewPixmap();
        addPixmapLines();
        addPieceConnectors();
    }

    public void setDimensions(GridPoint2 dimensions) {
        this.dimensions = dimensions;
        createNewPixmap();
        generateLineData();
        generateDistortedLineData();
        addPixmapLines();
        addPieceConnectors();
    }

    public Pixmap getGeneratedPixmap() {
        return pixmap;
    }

    public GridPoint2 getDimensions() {
        return this.dimensions;
    }

    public Vector2 getAspectRatio() {
        return this.aspectRatio;
    }

    public WaveDistortionData getWaveDistortion() {
        return this.waveDistortionData;
    }
}