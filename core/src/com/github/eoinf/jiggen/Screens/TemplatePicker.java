package com.github.eoinf.jiggen.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.Models.Template;
import com.github.eoinf.jiggen.Models.TemplateRemote;
import com.github.eoinf.jiggen.PuzzleExtractor.Decoder.DecodedTemplate;
import com.github.eoinf.jiggen.Jiggen;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;


public class TemplatePicker implements Screen {

    private static final float MAX_ZOOM = 4;
    private static final float MIN_ZOOM = 0.1f;
    private static final float ZOOM_RATE = 0.1f;

    Jiggen game;
    OrthographicCamera camera;
    Texture templateTexture;
    Stage pickerStage;

    public TemplatePicker(Jiggen game) {
        this.game = game;
        this.camera = game.camera;

        pickerStage = new Stage(new ScreenViewport(game.camera), game.batch);
        pickerStage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    if (!templateTexture.getTextureData().isPrepared()) {
                        templateTexture.getTextureData().prepare();
                    }
                    Pixmap templatePixmap = templateTexture.getTextureData().consumePixmap();

                    DecodedTemplate decodedTemplate = new DecodedTemplate(templatePixmap);
                    game.setScreen(new BackgroundPicker(game,
                            PuzzleFactory.generateTexturePuzzleFromTemplate(decodedTemplate)));
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

        System.out.println("Fetching remote template list");

        game.templateLoader.getRemoteTemplates()
                .thenAccept(templateRemotes -> {
                    System.out.println("Got remote template list");

                    for (TemplateRemote template : templateRemotes) {
                        System.out.printf("name=%s, id=%s\n", template.getName(), template.getId());
                    }
                });



        pickerStage.addActor();
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
