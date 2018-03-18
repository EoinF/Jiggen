package com.github.eoinf.jiggen.View.Widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.Models.Template;

public class TemplatePickerTable extends Table {

    private int currentRowColumns;

    private Skin skin;
    private int maxColumns;
    private int maxRows;

    public TemplatePickerTable(Skin skin) {
        this(skin, 3, 3);
    }

    public TemplatePickerTable(Skin skin, int maxColumns, int maxRows) {
        super();
        currentRowColumns = 0;

        this.skin = skin;
        this.maxColumns = maxColumns;
        this.maxRows = maxRows;

        setFillParent(true);
        pad(30);
    }

    public TemplatePickerTable addTemplate(Template template) {
        currentRowColumns++;
        if (currentRowColumns > maxColumns) {
            row();
            currentRowColumns -= maxColumns;
        }
        add(new TemplateWidget(template, skin))
                .expandX()
                .pad(10);
        return this;
    }
}
