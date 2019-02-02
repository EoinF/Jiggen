package com.github.eoinf.jiggen.TemplateCreator.lines;

/**
 * Representation of:
 *
 * y = rx
 *
 * r = dy/dx
 *
 */
public class StraightLine implements Line {

    public static final StraightLine flatLine = new StraightLine(0);
    private double ratio;

    public StraightLine(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public int getY(int x) {
        return (int)(ratio * x);
    }

    @Override
    public Line scaled(double scale) {
        return new StraightLine(scale * ratio);
    }
}
