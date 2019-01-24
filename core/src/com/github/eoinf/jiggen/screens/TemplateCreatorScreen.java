package com.github.eoinf.jiggen.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;
import com.github.eoinf.jiggen.screens.views.TemplateCreatorView;

public class TemplateCreatorScreen implements Screen {

    TemplateCreatorView templateCreatorView;

    public TemplateCreatorScreen(
            Jiggen jiggen,
            SpriteBatch batch,
            TextureAtlas uiTextureAtlas,
            Skin skin) {
        OrthographicCamera camera = new OrthographicCamera();

        TemplateCreatorViewModel templateCreatorViewModel = new TemplateCreatorViewModel(
                new GridPoint2(7, 5),
                new Vector2(4, 3),
                new WaveDistortionData(8f, 1, 0.02f)
        );
        TemplateCreatorViewController templateCreatorViewController = new TemplateCreatorViewController(templateCreatorViewModel);
        templateCreatorView = new TemplateCreatorView(camera, batch, skin, templateCreatorViewModel,
                templateCreatorViewController);
    }

    @Override
    public void render(float delta) {
        templateCreatorView.act(delta);

        templateCreatorView.draw();
    }

    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {
        templateCreatorView.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
