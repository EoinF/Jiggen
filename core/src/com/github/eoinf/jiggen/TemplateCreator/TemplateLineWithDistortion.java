package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.TemplateCreator.lines.Line;

public class TemplateLineWithDistortion {
    private GridPoint2[] points;
    private TemplateLine line;
    private GridPoint2 imageSize;

    private Line distortionDataX;
    private Line distortionDataY;

    public TemplateLineWithDistortion(TemplateLine templateLine, WaveDistortionData waveDistortionData, GridPoint2 imageSize) {
        line = templateLine;
        distortionDataX = waveDistortionData.distortionLine.scaled(imageSize.x / 100.0);
        distortionDataY = waveDistortionData.distortionLine.scaled(imageSize.y / 100.0);
        this.imageSize = imageSize;

        this.points = new GridPoint2[line.to - line.from];

        for (int i = 0; i < points.length; i++) {
            int current = line.from + i;
            if (line.isVertical) {
                int pivot = line.staticPoint + distortionDataY.getY(i);
                this.points[i] = new GridPoint2(pivot, current);
            } else {
                int pivot = line.staticPoint + distortionDataX.getY(i);
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
        int currentYUp = horizontalLine.line.staticPoint;

        int currentYDown = currentYUp - 1;

        for (int tries = 0; tries < verticalLine.imageSize.y; tries++) {
            int currentXUp = verticalLine.getX(currentYUp);
            int currentXDown = verticalLine.getX(currentYDown);
            if (Math.abs(currentYUp - horizontalLine.getY(currentXUp)) <= 1) {
                return new GridPoint2(currentXUp, currentYUp);
            } else if (Math.abs(currentYDown - horizontalLine.getY(currentXDown)) <= 1) {
                return new GridPoint2(currentXDown, currentYDown);
            }
            currentYUp++;
            currentYDown--;
        }
        return new GridPoint2(verticalLine.line.staticPoint, horizontalLine.line.staticPoint);
    }

    public int getX(int y) {
        assert line.isVertical;
        int distortion = distortionDataY.getY(y);
        return line.staticPoint + distortion;
    }

    public int getY(int x) {
        assert !line.isVertical;
        int distortion = distortionDataX.getY(x);
        return line.staticPoint + distortion;
    }

    public GridPoint2[] getPoints() {
        return points;
    }
}
