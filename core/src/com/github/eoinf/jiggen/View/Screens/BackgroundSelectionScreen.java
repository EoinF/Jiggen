package com.github.eoinf.jiggen.View.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.View.ScreenManager;
import com.github.eoinf.jiggen.utils;

import java.util.List;
import java.util.Random;


public class BackgroundSelectionScreen implements Screen {

    private Stage pickerStage;

    public BackgroundSelectionScreen(OrthographicCamera camera, SpriteBatch batch) {
        pickerStage = new Stage(new ScreenViewport(camera), batch);
        pickerStage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    ScreenManager.switchToPuzzleSolver(null);
                }
                return super.keyDown(event, keycode);
            }
        });
        Gdx.input.setInputProcessor(pickerStage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        pickerStage.act();
        pickerStage.draw();
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
