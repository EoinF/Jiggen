package com.github.eoinf.jiggen.TemplateCreator;

import com.badlogic.gdx.graphics.Pixmap;

public abstract class TemplateCreatorComponent<T> {
    TemplateCreatorComponent child;
    abstract Pixmap getGeneratedPixmap();
    abstract void setValue(T value);

    void setChild(TemplateCreatorComponent child) {
        this.child = child;
    }
}
