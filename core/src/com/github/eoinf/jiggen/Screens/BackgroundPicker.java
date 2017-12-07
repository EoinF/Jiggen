package com.github.eoinf.jiggen.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.Jiggen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class BackgroundPicker implements Screen {

    private static final float MAX_ZOOM = 4;
    private static final float MIN_ZOOM = 0.1f;
    private static final float ZOOM_RATE = 0.1f;

    Jiggen game;
    OrthographicCamera camera;
    Texture backgroundTexture;
    Stage pickerStage;

    public BackgroundPicker(Jiggen _game) {
        this.game = _game;
        this.camera = game.camera;

        pickerStage = new Stage(new ScreenViewport(game.camera), game.batch);
        pickerStage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    game.setScreen(new TemplatePicker(game, backgroundTexture));
                }
                return super.keyDown(event, keycode);
            }

            @Override
            public boolean scrolled(InputEvent event, float x, float y, int scrollDirection) {
                OrthographicCamera orthographicCamera = camera;
                orthographicCamera.zoom += scrollDirection * ZOOM_RATE;
                orthographicCamera.zoom = Math.max(MIN_ZOOM, orthographicCamera.zoom);
                orthographicCamera.zoom = Math.min(MAX_ZOOM, orthographicCamera.zoom);
                return true;
            }
        });
        Gdx.input.setInputProcessor(pickerStage);

        FileHandle[] files = Gdx.files.internal("backgrounds/").list();

        List<FileHandle> backgroundFiles = new ArrayList<>();
        for (FileHandle f: files) {
            if (!f.isDirectory() && f.name().endsWith(".jpg")) {
                backgroundFiles.add(f);
            }
        }

        FileHandle fileHandle;
        if (backgroundFiles.size() != 0) {
            Random random = new Random();
            fileHandle = backgroundFiles.get(random.nextInt(backgroundFiles.size()));
        } else {
            fileHandle = Gdx.files.internal("backgrounds/pizza.jpg");
        }
        this.backgroundTexture = new Texture(fileHandle);

        pickerStage.addActor(new Image(backgroundTexture));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.updateCameraInput(delta);

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
