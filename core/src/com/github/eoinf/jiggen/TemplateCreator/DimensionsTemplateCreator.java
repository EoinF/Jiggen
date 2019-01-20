package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.utils.PixmapUtils;

public class DimensionsTemplateCreator extends TemplateCreatorComponent<GridPoint2> {
    private Pixmap pixmap;

    public Pixmap getGeneratedPixmap() {
        return pixmap;
    }

    public void setValue(GridPoint2 dimensions) {
        this.pixmap = PixmapUtils.copyPixmap(this.child.getGeneratedPixmap());
        int linesX = dimensions.x - 1;
        int linesY = dimensions.y - 1;
    }
}
