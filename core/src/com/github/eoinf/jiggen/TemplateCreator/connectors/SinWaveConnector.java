package com.github.eoinf.jiggen.TemplateCreator.connectors;

import com.github.eoinf.jiggen.TemplateCreator.lines.SinWave;

public class SinWaveConnector extends PieceConnector {
    public SinWaveConnector(int x1, int y1, int x2, int y2, ConnectorDirection direction) {
        super(x1, y1, x2, y2, direction);
        int distanceY = y2 - y1;
        int distanceX = x2 - x1;

        double sinAmplitude;

        switch (direction) {
            case UP:
                sinAmplitude = (distanceX / 2f) + Math.abs(distanceY);
                this.shapingLine = SinWave.betweenPoints(x2, y2, x1, y1, sinAmplitude);
                break;
            case DOWN:
                sinAmplitude = (distanceX / 2f) + Math.abs(distanceY);
                this.shapingLine = SinWave.betweenPoints(x1, y1, x2, y2, sinAmplitude);
                break;
            case LEFT:
                sinAmplitude = (distanceY / 2f) + Math.abs(distanceX);
                this.shapingLine = SinWave.betweenPoints(y1, x1, y2, x2, sinAmplitude);
                break;
            case RIGHT:
                sinAmplitude = (distanceY / 2f) + Math.abs(distanceX);
                this.shapingLine = SinWave.betweenPoints(y2, x2, y1, x1, sinAmplitude);
                break;
        }
    }
}
