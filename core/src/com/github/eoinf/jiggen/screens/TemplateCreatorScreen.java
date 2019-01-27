package com.github.eoinf.jiggen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
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
import com.github.eoinf.jiggen.screens.views.TemplateCreatorToolbar;
import com.github.eoinf.jiggen.screens.views.TemplateCreatorView;

public class TemplateCreatorScreen implements Screen {

    private final TemplateCreatorToolbar toolbar;
    private final TemplateCreatorView view;
    private final TemplateCreatorViewController templateCreatorViewController;

    public TemplateCreatorScreen(
            Jiggen jiggen,
            SpriteBatch batch,
            TextureAtlas uiTextureAtlas,
            Skin skin) {
        OrthographicCamera camera = new OrthographicCamera();

        TemplateCreatorViewModel templateCreatorViewModel = new TemplateCreatorViewModel(
                new GridPoint2(10, 10),
                new Vector2(4, 3),
                new WaveDistortionData(8f, 1, 0.02f)
        );
        templateCreatorViewController = new TemplateCreatorViewController(templateCreatorViewModel);
        view = new TemplateCreatorView(camera, batch, skin, templateCreatorViewModel,
                templateCreatorViewController);
        toolbar = new TemplateCreatorToolbar(camera, batch, skin, templateCreatorViewModel,
                templateCreatorViewController
        );


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(toolbar.stage);
        multiplexer.addProcessor(view.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        view.act(delta);
        toolbar.act(delta);

        view.draw();
        toolbar.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Force the width and height to be integers
        // There is a bug with GWT where it is putting real numbers in here
        templateCreatorViewController.resizeScreen(Math.round(width), Math.round(height));
    }


    @Override
    public void show() {

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
