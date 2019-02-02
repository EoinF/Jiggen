package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.TemplateCreator.TemplateLine;
import com.github.eoinf.jiggen.TemplateCreator.TemplateLineWithDistortion;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;
import com.github.eoinf.jiggen.TemplateCreator.connectors.ConnectorDirection;
import com.github.eoinf.jiggen.TemplateCreator.connectors.SinWaveConnector;
import com.github.eoinf.jiggen.TemplateCreator.lines.StraightLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class PieceConnectorRenderer extends TemplateCreatorComponent {

    @Override
    boolean shouldRecalculate(TemplateCreatorData newData) {
        return newData.distortedLinesVertical != data.distortedLinesVertical ||
                newData.distortedLinesHorizontal != data.distortedLinesHorizontal
                || !newData.randomSeed.equals(data.randomSeed)
                || newData.maxSize != data.maxSize
                || newData.size != data.size
                || newData.pixmap != data.pixmap;
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        Random random = new Random(newData.randomSeed);
        TemplateCreatorData updatedData = new TemplateCreatorData(newData);
        Pixmap pixmap = updatedData.pixmap;

        List<TemplateLineWithDistortion> distortedLinesVerticalWithEdge = new ArrayList<>(Arrays.asList(newData.distortedLinesVertical));
        List<TemplateLineWithDistortion> distortedLinesHorizontalWithEdge = new ArrayList<>(Arrays.asList(newData.distortedLinesHorizontal));

        distortedLinesVerticalWithEdge.add(
                new TemplateLineWithDistortion(
                        new TemplateLine(0, newData.size.y, newData.size.x, true),
                        new WaveDistortionData(StraightLine.flatLine),
                        new GridPoint2(newData.size.x, newData.size.y)
                )
        );
        distortedLinesHorizontalWithEdge.add(
                new TemplateLineWithDistortion(
                        new TemplateLine(0, newData.size.x, newData.size.y, false),
                        new WaveDistortionData(StraightLine.flatLine),
                        new GridPoint2(newData.size.x, newData.size.y)
                )
        );

        TemplateLineWithDistortion previousHorizontal = new TemplateLineWithDistortion(
                new TemplateLine(0, newData.size.x, 0, false),
                new WaveDistortionData(StraightLine.flatLine),
                newData.maxSize
        );
        for (TemplateLineWithDistortion horizontal : distortedLinesHorizontalWithEdge) {
            TemplateLineWithDistortion previousVertical = new TemplateLineWithDistortion(
                    new TemplateLine(0, newData.size.y, 0, true),
                    new WaveDistortionData(StraightLine.flatLine),
                    newData.maxSize
            );
            for (TemplateLineWithDistortion vertical : distortedLinesVerticalWithEdge) {
                GridPoint2 current = horizontal.getIntersectionPoint(vertical);

                // Right side connector
                if (distortedLinesVerticalWithEdge.indexOf(vertical) < distortedLinesVerticalWithEdge.size() - 1) {
                    GridPoint2 previous = vertical.getIntersectionPoint(previousHorizontal);

                    int connectorY = (current.y + previous.y) / 2;
                    addHorizontalConnectorAt(connectorY, vertical, (current.y - previous.y) / 3f,
                            random, pixmap);
                }

                // Bottom side connector
                if (distortedLinesHorizontalWithEdge.indexOf(horizontal) < distortedLinesHorizontalWithEdge.size() - 1) {
                    GridPoint2 previous = horizontal.getIntersectionPoint(previousVertical);

                    int connectorX = (current.x + previous.x) / 2;
                    addVerticalConnectorAt(connectorX, horizontal, (current.x - previous.x) / 3f,
                            random, pixmap);
                }
                previousVertical = vertical;
            }
            previousHorizontal = horizontal;
        }

        return newData;
    }


    private void addVerticalConnectorAt(int connectorX, TemplateLineWithDistortion horizontalLine, float connectorSize,
                                        Random random, Pixmap pixmap) {
        drawGapForVerticalConnector(connectorX, horizontalLine, (int) connectorSize, pixmap);

        pixmap.setColor(Color.BLACK);
        int firstX = (int) (connectorX - (connectorSize / 2f));
        int firstY = horizontalLine.getY(firstX);

        int lastX = (int) (connectorX + (connectorSize / 2f));
        int lastY = horizontalLine.getY(lastX);
        ConnectorDirection direction;
        if (random.nextBoolean()) {
            direction = ConnectorDirection.DOWN;
        } else {
            direction = ConnectorDirection.UP;
        }
        new SinWaveConnector(firstX, firstY, lastX, lastY, direction).draw(pixmap);
    }

    private void addHorizontalConnectorAt(int connectorY, TemplateLineWithDistortion verticalLine, float connectorSize,
                                          Random random, Pixmap pixmap) {
        drawGapForHorizontalConnector(connectorY, verticalLine, (int) connectorSize, pixmap);

        pixmap.setColor(Color.BLACK);
        int firstY = (int) (connectorY - (connectorSize / 2f));
        int firstX = verticalLine.getX(firstY);

        int lastY = (int) (connectorY + (connectorSize / 2f));
        int lastX = verticalLine.getX(lastY);
        ConnectorDirection direction;
        if (random.nextBoolean()) {
            direction = ConnectorDirection.LEFT;
        } else {
            direction = ConnectorDirection.RIGHT;
        }
        new SinWaveConnector(firstX, firstY, lastX, lastY, direction).draw(pixmap);
    }

    private void drawGapForHorizontalConnector(int connectorY, TemplateLineWithDistortion verticalLine,
                                               int connectorSize, Pixmap pixmap) {
        pixmap.setColor(Color.WHITE);
        for (int offset = -(connectorSize / 2); offset < (connectorSize / 2); offset++) {
            int y = connectorY + offset;
            int x = verticalLine.getX(y);
            pixmap.drawPixel(x, y);
        }
    }

    private void drawGapForVerticalConnector(int connectorX, TemplateLineWithDistortion horizontalLine,
                                             int connectorSize, Pixmap pixmap) {
        pixmap.setColor(Color.WHITE);
        for (int offset = -(connectorSize / 2); offset < (connectorSize / 2); offset++) {
            int x = connectorX + offset;
            int y = horizontalLine.getY(x);
            pixmap.drawPixel(x, y);
        }
    }
}
