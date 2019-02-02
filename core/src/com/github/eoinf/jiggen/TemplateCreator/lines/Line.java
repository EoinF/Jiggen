package com.github.eoinf.jiggen.TemplateCreator.lines;


public interface Line {
    int getY(int x);
    Line scaled(double scale);
}
