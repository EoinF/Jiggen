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
import com.github.eoinf.jiggen.PuzzleExtractor.DecodedTemplate;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.PuzzleFactory;
import com.github.eoinf.jiggen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TemplatePicker implements Screen {

    private static final float MAX_ZOOM = 4;
    private static final float MIN_ZOOM = 0.1f;
    private static final float ZOOM_RATE = 0.1f;

    Jiggen game;
    OrthographicCamera camera;
    Texture templateTexture;
    Stage pickerStage;
    Texture backgroundTexture;

    TemplatePicker(Jiggen game, Texture backgroundTexture) {
        this.game = game;
        this.camera = game.camera;
        this.backgroundTexture = backgroundTexture;

        pickerStage = new Stage(new ScreenViewport(game.camera), game.batch);
        pickerStage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    DecodedTemplate decodedTemplate = new DecodedTemplate(templateTexture, game.tracingStrategy);

                    if (!backgroundTexture.getTextureData().isPrepared()) {
                        backgroundTexture.getTextureData().prepare();
                    }
                    game.setScreen(new PuzzleSolver(game, PuzzleFactory.generatePuzzle(game.tracingStrategy, decodedTemplate,
                            backgroundTexture.getTextureData().consumePixmap())));
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

        FileHandle[] files = Gdx.files.internal("templates").list();

        List<FileHandle> templateFiles = new ArrayList<>();
        for (FileHandle f: files) {
            if (!f.isDirectory() && f.name().endsWith(".jpg")) {
                templateFiles.add(f);
            }
        }

        FileHandle fileHandle;
        if (templateFiles.size() != 0) {
            Random random = new Random();
            fileHandle = templateFiles.get(random.nextInt(templateFiles.size()));
        } else { // Must explicitly declare filepath when running in the browser
            fileHandle = Gdx.files.internal("templates/5x7puzzletemplate.jpg");
        }

        this.templateTexture = new Texture(fileHandle);

        this.templateTexture = utils.stretchTexture(templateTexture,
                new GridPoint2(backgroundTexture.getWidth(), backgroundTexture.getHeight()));

        pickerStage.addActor(new Image(templateTexture));
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
