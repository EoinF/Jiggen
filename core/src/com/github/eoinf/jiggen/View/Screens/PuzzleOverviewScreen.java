package com.github.eoinf.jiggen.View.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.eoinf.jiggen.View.Widgets.Overview.OverviewTable;

public class PuzzleOverviewScreen implements Screen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Skin skin;

    private Stage stage;

    public PuzzleOverviewScreen(OrthographicCamera camera, SpriteBatch batch, Skin skin, Stage stage) {
        this.camera = camera;
        this.batch = batch;
        this.skin = skin;
        this.stage = stage;

        Table table = new OverviewTable(skin);
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
