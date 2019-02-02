package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.graphics.Pixmap;

public abstract class TemplateCreatorComponent {
    private TemplateCreatorComponent nextComponent;

    protected TemplateCreatorData data;

    // Top level component
    TemplateCreatorComponent() {
        this.data = new TemplateCreatorData();
    }

    // Connector component
    TemplateCreatorComponent(TemplateCreatorComponent nextComponent) {
        this();
        this.nextComponent = nextComponent;
    }

    // Root component
    TemplateCreatorComponent(TemplateCreatorData baseData, TemplateCreatorComponent nextComponent) {
        this(nextComponent);
        this.setData(baseData);
    }

    public void setData(TemplateCreatorData newData) {
        if (shouldRecalculate(newData)) {
            newData = calculate(newData);
        }

        this.data = newData;
        if (nextComponent != null) {
            nextComponent.setData(newData);
        }
    }

    public Pixmap getPixmap() {
        if (nextComponent != null) {
            Pixmap nextPixmap = nextComponent.getPixmap();
            if (nextPixmap != null) {
                return nextPixmap;
            }
        }
        return this.data.pixmap;
    }

    public TemplateCreatorData getData() {
        if (nextComponent != null) {
            TemplateCreatorData nextData = nextComponent.getData();
            return nextData != null ? nextData: this.data;
        } else {
            return this.data;
        }
    }

    abstract boolean shouldRecalculate(TemplateCreatorData newData);

    abstract TemplateCreatorData calculate(TemplateCreatorData newData);
}
