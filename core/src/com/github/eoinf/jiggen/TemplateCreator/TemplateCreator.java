package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;

public class TemplateCreator {

    private final AspectRatioTemplateCreator aspectRatioComponent;
    private final DimensionsTemplateCreator dimensionComponent;

    public TemplateCreator() {
        aspectRatioComponent = new AspectRatioTemplateCreator();
        dimensionComponent = new DimensionsTemplateCreator();

        dimensionComponent.setChild(aspectRatioComponent);
    }

    public void setAspectRatio(Vector2 aspectRatio) {
        aspectRatioComponent.setValue(aspectRatio);
    }

    public Pixmap getGeneratedPixmap() {
        return aspectRatioComponent.getGeneratedPixmap();
    }

    public void setDimensions(GridPoint2 dimensions) {
        dimensionComponent.setValue(dimensions);
    }
}
