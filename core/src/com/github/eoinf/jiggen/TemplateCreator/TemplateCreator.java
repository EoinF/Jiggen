package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;

import java.util.function.Consumer;

public class TemplateCreator {
    private Pixmap pixmap;
    private Vector2 aspectRatio = new Vector2(1, 1);
    private GridPoint2 dimensions = new GridPoint2(5, 5);

    private TemplateLine[] linesHorizontal;
    private TemplateLine[] linesVertical;

    private GridPoint2 maxSize;
    private WaveDistortionData waveDistortionData = new WaveDistortionData(0,0,0);

    private void createNewPixmap() {
        float ratio = aspectRatio.x / aspectRatio.y;
        int x, y;
        if (maxSize.y * ratio < maxSize.x) {
            x = (int) ((maxSize.y * aspectRatio.x) / aspectRatio.y);
            y = maxSize.y;
        } else {
            x = maxSize.x;
            y = (int)((maxSize.x * aspectRatio.y) / aspectRatio.x);
        }
        this.pixmap = new Pixmap(x, y, Pixmap.Format.RGBA8888);

        this.pixmap.setColor(Color.WHITE);
        this.pixmap.fill();
    }

    private void generateLineData() {
        linesHorizontal = new TemplateLine[dimensions.y - 1];
        linesVertical = new TemplateLine[dimensions.x - 1];

        for (int i = 0; i < linesHorizontal.length; i++) {
            int y = (int)((i + 1) * ((float)pixmap.getHeight() / dimensions.y));
            int x2 = pixmap.getWidth();
            linesHorizontal[i] = new TemplateLine(0, x2, y, false);
        }
        for (int i = 0; i < linesVertical.length; i++) {
            int x = (int)((i + 1) * ((float)pixmap.getWidth() / dimensions.x));
            int y2 = pixmap.getHeight();
            linesVertical[i] = new TemplateLine(0, y2, x, true);
        }
    }



    private void addPixmapLines() {
        pixmap.setColor(Color.BLACK);
        for (TemplateLine line: linesHorizontal) {
            drawLine(line);
        }
        for (TemplateLine line: linesVertical) {
            drawLine(line);
        }
    }

    private void drawLine(TemplateLine line) {
        if (line.isVertical) {
            for (int y = line.from; y <= line.to; y++) {
                int distortion = waveDistortionData.getDistortion(y);
                pixmap.drawPixel(line.staticPoint + distortion, y);
            }
        } else {
            for (int x = line.from; x <= line.to; x++) {
                int distortion = waveDistortionData.getDistortion(x);
                pixmap.drawPixel(x, line.staticPoint + distortion);
            }
        }

    }


    public TemplateCreator(TemplateCreatorViewController templateCreatorViewController,
                           TemplateCreatorViewModel templateCreatorViewModel, GridPoint2 maxSize) {
        this.maxSize = maxSize;
        createNewPixmap();
        generateLineData();
        addPixmapLines();

        templateCreatorViewModel.getTemplateAspectRatioObservable().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 aspectRatio) {
                setAspectRatio(aspectRatio);
                templateCreatorViewController.setPixmap(pixmap);
            }
        });

        templateCreatorViewModel.getTemplateDimensionsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 dimensions) {
                setDimensions(dimensions);
                templateCreatorViewController.setPixmap(pixmap);
            }
        });

        templateCreatorViewModel.getTemplateMaxSizeObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 maxSize) {
                setMaxSize(maxSize);
                templateCreatorViewController.setPixmap(pixmap);
            }
        });

        templateCreatorViewModel.getWaveDistortionObservable().subscribe(new Consumer<WaveDistortionData>() {
            @Override
            public void accept(WaveDistortionData waveDistortionData) {
                setWaveDistortion(waveDistortionData);
                templateCreatorViewController.setPixmap(pixmap);
            }
        });
    }

    private void setWaveDistortion(WaveDistortionData waveDistortionData) {
        this.waveDistortionData = waveDistortionData;
        createNewPixmap();
        addPixmapLines();
    }


    public void setMaxSize(GridPoint2 maxSize) {
        this.maxSize = maxSize;
        createNewPixmap();
        generateLineData();
        addPixmapLines();
    }

    public void setAspectRatio(Vector2 aspectRatio) {
        this.aspectRatio = aspectRatio;
        this.pixmap.dispose();
        createNewPixmap();
        addPixmapLines();
    }

    public void setDimensions(GridPoint2 dimensions) {
        this.dimensions = dimensions;
        createNewPixmap();
        generateLineData();
        addPixmapLines();
    }

    public Pixmap getGeneratedPixmap() {
        return pixmap;
    }
}
