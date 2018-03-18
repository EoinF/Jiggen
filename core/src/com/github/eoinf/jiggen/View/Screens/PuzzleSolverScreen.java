package com.github.eoinf.jiggen.View.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzleGraph;
import com.github.eoinf.jiggen.PuzzleExtractor.Puzzle.PuzzlePiece;
import com.github.eoinf.jiggen.utils;

import java.util.ArrayList;
import java.util.List;

public class PuzzleSolverScreen extends CameraControlledScreen {

    private OrthographicCamera camera;

    private Actor pieceHeld;
    private Vector2 pieceOffsetHeld;

    private Stage gameStage;
    private List<Actor> pieces;

    public PuzzleSolverScreen(OrthographicCamera camera, SpriteBatch batch, PuzzleGraph puzzleGraph) {
        super(camera);
        this.camera = camera;

        gameStage = new Stage(new ScreenViewport(camera), batch);
        pieces = new ArrayList<>();
        for (PuzzlePiece<TextureRegion> piece: puzzleGraph.getVertices()) {
            Image image = new Image(piece.getData());
            image.setUserObject(piece);
            image.setPosition(piece.getPosition().x, piece.getPosition().y);
            pieces.add(image);
            gameStage.addActor(image);
        }

        gameStage.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Vector3 mousePositionInWorld = new Vector3(x, y, 0);
                Actor a = gameStage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);
                if (a != null) {
                    int index = pieces.indexOf(a);
                    if (index >= 0) {
                        pieceHeld = pieces.get(index);
                        pieceOffsetHeld = new Vector2(
                                pieceHeld.getX() - mousePositionInWorld.x,
                                pieceHeld.getY() - mousePositionInWorld.y - 1
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
                    utils.shuffle(puzzleGraph, pieces);
                }
                return super.keyDown(event, keycode);
            }
        });

        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void show() {

    }

    private void update(float delta) {
        if (pieceHeld != null) {
            Vector3 mousePositionInWorld = camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            pieceHeld.setPosition(mousePositionInWorld.x + pieceOffsetHeld.x,
                    mousePositionInWorld.y + pieceOffsetHeld.y);
        }
        gameStage.act();
    }

    @Override
    public void render(float delta) {
        update(delta);

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
}
