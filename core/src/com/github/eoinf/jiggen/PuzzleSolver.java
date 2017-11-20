package com.github.eoinf.jiggen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.List;

public class PuzzleSolver implements Screen {

    private Jiggen game;
    PuzzleGraph puzzleGraph;
    OrthographicCamera camera;
    Texture templateTexture;

    private static final float DEFAULT_CAMERA_SPEED = 200f;
    private static final int VIEWPORT_WIDTH = 1280;
    private static final int VIEWPORT_HEIGHT = 720;


    private PuzzlePiece pieceHeld;
    private Vector2 pieceOffsetHeld;

    Stage gameStage;

    public PuzzleSolver(Jiggen game, PuzzleGraph puzzleGraph, Texture templateTexture) {
        this.game = game;
        this.puzzleGraph = puzzleGraph;
        this.templateTexture = templateTexture;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        gameStage = new Stage(new ScreenViewport(camera), game.batch);
        for (Actor actor: puzzleGraph.getVertices()) {
            gameStage.addActor(actor);
        }

        puzzleGraph.setPosition(150, 100);

        gameStage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Vector3 mousePositionInWorld = camera.unproject(new Vector3(x, gameStage.getHeight() - y, 0));
                Actor a = gameStage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);
                if (a != null) {
                    List<PuzzlePiece> pieceList = puzzleGraph.getVertices();
                    int index = pieceList.indexOf(a);
                    if (index >= 0) {
                        pieceHeld = pieceList.get(index);
                        pieceOffsetHeld = new Vector2(
                                pieceHeld.getPosition().x - mousePositionInWorld.x,
                                pieceHeld.getPosition().y - mousePositionInWorld.y - 1
                        );
                        pieceHeld.toFront();
                    }
                }
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                pieceHeld = null;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    puzzleGraph.shuffle();
                }
                return super.keyDown(event, keycode);
            }
        });

        Gdx.input.setInputProcessor(gameStage);

    }

    @Override
    public void show() {

    }

    public void update(float delta) {
        if (pieceHeld != null) {
            Vector3 mousePositionInWorld = camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            pieceHeld.setPosition(mousePositionInWorld.x + pieceOffsetHeld.x,
                    mousePositionInWorld.y + pieceOffsetHeld.y);
        }
        gameStage.act();
        updateCameraInput(delta);
        camera.update();
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        //game.batch.draw(templateTexture, 350, 50);
        gameStage.draw();
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

    public void updateCameraInput(float delta) {
        float zoom = camera.zoom * camera.zoom * 2;

        int translateX = 0;
        int translateY = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            translateX = -(int)((DEFAULT_CAMERA_SPEED + zoom) * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            translateX = (int)((DEFAULT_CAMERA_SPEED + zoom) * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            translateY = (int)((DEFAULT_CAMERA_SPEED + zoom) * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            translateY = -(int)((DEFAULT_CAMERA_SPEED + zoom) * delta);
        }
        camera.translate(translateX, translateY);
        camera.update();
    }
}
