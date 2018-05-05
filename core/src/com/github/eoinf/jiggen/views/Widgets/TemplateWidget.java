package com.github.eoinf.jiggen.views.Widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.Models.Template;
import com.github.eoinf.jiggen.utils;

public class TemplateWidget extends Table {

    public TemplateWidget(Template template, Skin skin) {
        super(skin);
        String labelText = "Choose a template...";

        Table table = new Table();

        if (template != null) {
            Sprite textureSprite = new Sprite();
            if (template.getTexture() != null) {
                textureSprite = new Sprite(template.getTexture());
            }
            labelText = template.getName();
            table.add(new Image(textureSprite))
                    .fill()
                    .expand();
        }
        utils.setBackgroundColour(table, Color.CYAN);

        add(table)
        .fill().expand();
        row();
        add(new Label(labelText, skin));
    }
}
