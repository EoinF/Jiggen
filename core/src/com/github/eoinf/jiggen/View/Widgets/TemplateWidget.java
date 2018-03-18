package com.github.eoinf.jiggen.View.Widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.Models.Template;

public class TemplateWidget extends Table {

    public TemplateWidget(Template template, Skin skin) {
        super(skin);
        Sprite textureSprite;
        String labelText;
        if (template == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.CYAN);
            pixmap.fill();
            textureSprite = new Sprite(new Texture(pixmap));
            pixmap.dispose();
            labelText = "None selected";
        } else {
            if (template.getTexture() == null) {
                textureSprite = new Sprite();
            } else {
                textureSprite = new Sprite(template.getTexture());
            }
            labelText = template.getName();
        }
        Table table = new Table();

        table.add(new Image(textureSprite))
                .fill()
                .expandX();

        add(table);
        row();
        add(new Label(labelText, skin));
    }
}
