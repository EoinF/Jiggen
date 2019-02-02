package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.TemplateCreator.components.BaseLinesGenerator;
import com.github.eoinf.jiggen.TemplateCreator.components.BaseTemplateGenerator;
import com.github.eoinf.jiggen.TemplateCreator.components.BaseTemplateRenderer;
import com.github.eoinf.jiggen.TemplateCreator.components.DistortedLinesGenerator;
import com.github.eoinf.jiggen.TemplateCreator.components.DistortedLinesRenderer;
import com.github.eoinf.jiggen.TemplateCreator.components.PieceConnectorRenderer;
import com.github.eoinf.jiggen.TemplateCreator.components.TemplateCreatorComponent;
import com.github.eoinf.jiggen.TemplateCreator.components.TemplateCreatorData;
import com.github.eoinf.jiggen.TemplateCreator.lines.StraightLine;

import java.util.Random;

public class TemplateCreator {

    private TemplateCreatorComponent root;

    public TemplateCreator() {
        this(
                new GridPoint2(50, 50),
                new Vector2(1, 1),
                new GridPoint2(2, 2),
                new WaveDistortionData(StraightLine.flatLine));
    }

    public TemplateCreator(GridPoint2 maxSize,
                           Vector2 aspectRatio,
                           GridPoint2 dimensions,
                           WaveDistortionData waveDistortionData) {
        TemplateCreatorData baseData = new TemplateCreatorData(
                aspectRatio,
                dimensions,
                maxSize,
                waveDistortionData,
                new Random().nextLong()
        );

        PieceConnectorRenderer pieceConnectorRenderer = new PieceConnectorRenderer();
        DistortedLinesRenderer distortedLinesRenderer = new DistortedLinesRenderer(pieceConnectorRenderer);
        DistortedLinesGenerator distortedLinesGenerator = new DistortedLinesGenerator(distortedLinesRenderer);
        BaseLinesGenerator baseLinesGenerator = new BaseLinesGenerator(distortedLinesGenerator);
        BaseTemplateRenderer baseTemplateRenderer = new BaseTemplateRenderer(baseLinesGenerator);

        this.root = new BaseTemplateGenerator(baseData, baseTemplateRenderer);
    }

    public void setWaveDistortion(WaveDistortionData waveDistortionData) {
        TemplateCreatorData newData = new TemplateCreatorData(root.getData());
        newData.waveDistortionData = waveDistortionData;
        root.setData(newData);
    }


    public void setMaxSize(GridPoint2 maxSize) {
        TemplateCreatorData newData = new TemplateCreatorData(root.getData());
        newData.maxSize = maxSize;
        root.setData(newData);
    }

    public void setAspectRatio(Vector2 aspectRatio) {
        TemplateCreatorData newData = new TemplateCreatorData(root.getData());
        newData.aspectRatio = aspectRatio;
        root.setData(newData);
    }

    public void setDimensions(GridPoint2 dimensions) {
        TemplateCreatorData newData = new TemplateCreatorData(root.getData());
        newData.dimensions = dimensions;
        root.setData(newData);
    }

    public Pixmap getGeneratedPixmap() {
        return root.getPixmap();
    }
}
