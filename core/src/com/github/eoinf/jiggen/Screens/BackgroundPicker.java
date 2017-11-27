package com.github.eoinf.jiggen.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.DecodedTemplate;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.utils;

import java.io.File;
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

    public BackgroundPicker(Jiggen game) {
        this.game = game;
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

        File[] files = new File("core/assets/backgrounds").listFiles();

        List<File> backgroundFiles = new ArrayList<>();
        for (File f: files) {
            if (f.isFile() && f.getName().endsWith(".jpg")) {
                backgroundFiles.add(f);
            }
        }

        Random random = new Random();
        File randomTemplate = backgroundFiles.get(random.nextInt(backgroundFiles.size()));

        this.backgroundTexture = new Texture(new FileHandle(randomTemplate));

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
