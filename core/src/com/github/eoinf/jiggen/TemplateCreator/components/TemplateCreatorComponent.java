package com.github.eoinf.jiggen.TemplateCreator.components;

import com.badlogic.gdx.graphics.Pixmap;

public abstract class TemplateCreatorComponent {
    private TemplateCreatorComponent nextComponent;

    protected TemplateCreatorData data;
    protected Pixmap pixmap;

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
            if (pixmap != null) {
                nextComponent.pixmap = pixmap;
            }
            nextComponent.setData(newData);
        }
    }

    public Pixmap getPixmap() {
        if (nextComponent != null) {
            Pixmap nextPixmap = nextComponent.getPixmap();
            return nextPixmap != null ? nextPixmap: this.pixmap;
        } else {
            return this.pixmap;
        }
    }

    public void setPixmap(Pixmap pixmap) {
        if (this.pixmap != pixmap) {
            this.pixmap = pixmap;
            calculate(this.data);

            if (nextComponent != null) {
                nextComponent.setPixmap(pixmap);
            }
        }
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
