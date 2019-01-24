package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.math.GridPoint2;

public class TemplateLine {
    public final GridPoint2 from;
    public final GridPoint2 to;

    public TemplateLine(GridPoint2 from, GridPoint2 to) {
        this.from = from;
        this.to = to;
    }
}

