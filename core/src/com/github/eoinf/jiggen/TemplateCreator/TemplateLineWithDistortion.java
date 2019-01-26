package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.math.GridPoint2;

public class TemplateLineWithDistortion {
    private GridPoint2[] points;
    private TemplateLine line;
    private WaveDistortionData distortionData;

    public TemplateLineWithDistortion(TemplateLine templateLine, WaveDistortionData waveDistortionData) {
        line = templateLine;
        distortionData = waveDistortionData;

        this.points = new GridPoint2[line.to - line.from];

        for (int i = 0; i < points.length; i++) {
            int current = line.from + i;
            int pivot = line.staticPoint + distortionData.getDistortion(i);
            if (line.isVertical) {
                this.points[i] = new GridPoint2(pivot, current);
            } else {
                this.points[i] = new GridPoint2(current, pivot);
            }
        }
    }

    public GridPoint2 getIntersectionPoint(TemplateLineWithDistortion templateLineWithDistortion) {
        assert line.isVertical != templateLineWithDistortion.line.isVertical;

        if (line.isVertical) {
            return findIntersectionPoint(this, templateLineWithDistortion);
        } else {
            return findIntersectionPoint(templateLineWithDistortion, this);
        }
    }

    private static GridPoint2 findIntersectionPoint(
            TemplateLineWithDistortion verticalLine,
            TemplateLineWithDistortion horizontalLine
    ) {
        GridPoint2 estimate = new GridPoint2(
                verticalLine.line.staticPoint,
                horizontalLine.line.staticPoint
        );

        return estimate;
    }

    public int getX(int y) {
        assert line.isVertical;
        int distortion = distortionData.getDistortion(y);
        return line.staticPoint + distortion;
    }

    public int getY(int x) {
        assert !line.isVertical;
        int distortion = distortionData.getDistortion(x);
        return line.staticPoint + distortion;
    }

    public GridPoint2[] getPoints() {
        return points;
    }
}
