package com.github.eoinf.jiggen.webapp.screens.models;

public class IntRectangle {
    public int x; public int y; public int width; public int height;
    public IntRectangle() {
        // Default constructor for json deserialization
    }
    public IntRectangle(int x, int y, int width, int height) {
        this.x = x; this.y = y; this.width = width; this.height = height;
    }
}
