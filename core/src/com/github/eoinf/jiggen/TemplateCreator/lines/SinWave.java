package com.github.eoinf.jiggen.TemplateCreator.lines;

/**
 * Representation of:
 *
 * y = A * sin(Tx + P)
 *
 * A = amplitude
 * T = period
 * P = phase
 */
public class SinWave implements Line {
    public final double sinAmplitude;
    public final double sinPhase;
    public final double sinPeriod;

    public SinWave(double sinAmplitude, double sinPhase, double sinPeriod) {
        this.sinAmplitude = sinAmplitude;
        this.sinPhase = sinPhase;
        this.sinPeriod = sinPeriod;
    }

    public static SinWave betweenPoints(int fromX, int fromY, int toX, int toY, double sinAmplitude) {
        double distanceY = toY - fromY;
        double distanceX = toX - fromX;
        double offset = Math.asin(distanceY / sinAmplitude);
        double sinPeriod = (Math.PI - offset) / (distanceX);

        return new SinWave(sinAmplitude, 0, sinPeriod);
    }

    public int getY(int x) {
        return (int)(sinAmplitude * Math.sin(sinPhase + sinPeriod * x ));
    }

    @Override
    public Line scaled(double scale) {
        return new SinWave(this.sinAmplitude * scale, this.sinPhase, this.sinPeriod / scale);
    }
}