package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

import java.io.File;
import java.util.Random;

public class TemplateCreatorFileSaver {
    private int pixelsPerPiece;

    public TemplateCreatorFileSaver(int pixelsPerPiece) {
        this.pixelsPerPiece = pixelsPerPiece;
    }

    public void saveTemplateToFile(GridPoint2 dimensions,
                                   Vector2 aspectRatio,
                                   WaveDistortionData waveDistortionData,
                                   File file) {
        Pixmap pixmap = new TemplateCreator(
                new GridPoint2(dimensions.x * pixelsPerPiece, dimensions.y * pixelsPerPiece),
                aspectRatio,
                dimensions,
                waveDistortionData,
                new Random().nextLong()).getGeneratedPixmap();
        PixmapIO.writePNG(new FileHandle(file), pixmap);
    }
}
