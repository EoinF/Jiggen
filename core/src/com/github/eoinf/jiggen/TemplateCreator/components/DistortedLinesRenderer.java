package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.TemplateCreator.TemplateLineWithDistortion;

public final class DistortedLinesRenderer extends TemplateCreatorComponent {

    public DistortedLinesRenderer(PieceConnectorRenderer pieceConnectorRenderer) {
        super(pieceConnectorRenderer);
    }


    @Override
    boolean shouldRecalculate(TemplateCreatorData newData) {
        return newData.distortedLinesHorizontal != data.distortedLinesHorizontal
                || newData.distortedLinesVertical != data.distortedLinesVertical;
    }

    @Override
    protected TemplateCreatorData calculate(TemplateCreatorData newData) {
        pixmap.setColor(Color.BLACK);
        for (TemplateLineWithDistortion line : newData.distortedLinesHorizontal) {
            drawLine(line);
        }
        for (TemplateLineWithDistortion line : newData.distortedLinesVertical) {
            drawLine(line);
        }
        return newData;
    }

    private void drawLine(TemplateLineWithDistortion line) {
        GridPoint2[] points = line.getPoints();
        for (int i = 0; i < points.length - 1; i++) {
            pixmap.drawLine(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
        }
    }
}
