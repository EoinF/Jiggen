package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.TemplateCreator.TemplateLine;
import com.github.eoinf.jiggen.TemplateCreator.TemplateLineWithDistortion;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;

public class TemplateCreatorData {
    // Primary data
    public Vector2 aspectRatio;
    public GridPoint2 dimensions;

    public GridPoint2 maxSize;
    public WaveDistortionData waveDistortionData;
    public Long randomSeed;

    // Derived data
    public GridPoint2 size;

    public TemplateLine[] linesHorizontal;
    public TemplateLine[] linesVertical;

    public TemplateLineWithDistortion[] distortedLinesHorizontal;
    public TemplateLineWithDistortion[] distortedLinesVertical;

    public Pixmap pixmap;

    public TemplateCreatorData(Vector2 aspectRatio, GridPoint2 dimensions, GridPoint2 maxSize, WaveDistortionData waveDistortionData, long randomSeed) {
        // Primary data
        this.aspectRatio = aspectRatio;
        this.dimensions = dimensions;
        this.maxSize = maxSize;
        this.waveDistortionData = waveDistortionData;
        this.randomSeed = randomSeed;

        // Derived data
        this.size = null;
        this.linesHorizontal = null;
        this.linesVertical = null;
        this.distortedLinesHorizontal = null;
        this.distortedLinesVertical = null;
        this.pixmap = null;
    }

    TemplateCreatorData() {
        this.aspectRatio = null;
        this.dimensions = null;
        this.size = null;
        this.linesHorizontal = null;
        this.linesVertical = null;
        this.distortedLinesHorizontal = null;
        this.distortedLinesVertical = null;
        this.maxSize = null;
        this.waveDistortionData = null;
        this.randomSeed = null;
        this.pixmap = null;
    }

    public TemplateCreatorData(TemplateCreatorData data) {
        this.aspectRatio = data.aspectRatio;
        this.dimensions = data.dimensions;
        this.linesHorizontal = data.linesHorizontal;
        this.linesVertical = data.linesVertical;
        this.distortedLinesHorizontal = data.distortedLinesHorizontal;
        this.distortedLinesVertical = data.distortedLinesVertical;
        this.size = data.size;
        this.maxSize = data.maxSize;
        this.waveDistortionData = data.waveDistortionData;
        this.randomSeed = data.randomSeed;
        this.pixmap = data.pixmap;
    }
}
