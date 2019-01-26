package com.github.eoinf.jiggen.TemplateCreator;

public class TemplateLine {
    public final int from;
    public final int to;
    public final int staticPoint;
    public final boolean isVertical;

    public TemplateLine(int from, int to, int staticPoint, boolean isVertical) {
        assert from < to;
        this.from = from;
        this.to = to;
        this.staticPoint = staticPoint;
        this.isVertical = isVertical;
    }
}

