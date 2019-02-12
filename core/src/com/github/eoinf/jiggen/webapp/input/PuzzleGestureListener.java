package com.github.eoinf.jiggen.webapp.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.eoinf.jiggen.webapp.screens.controllers.PuzzleViewController;
import com.github.eoinf.jiggen.webapp.screens.widgets.ConnectedPiecesGroup;

public class PuzzleGestureListener implements EnhancedGestureListener {

    private PuzzleViewController puzzleViewController;
    private Stage stage;
    private static float ZOOM_MULTIPLIER = 0.004f;
    private int numPointersTouchedDown = 0;

    public PuzzleGestureListener(PuzzleViewController puzzleViewController, Stage stage) {
        this.puzzleViewController = puzzleViewController;
        this.stage = stage;
    }

    private boolean isTouching() {
        return numPointersTouchedDown > 0;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 mousePositionOnScreen = new Vector3(x, y, 0);

        Vector3 mousePositionInWorld = stage.getCamera().unproject(mousePositionOnScreen);
        Actor a = stage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);

        if (a != null && !isTouching()) {
            if (a instanceof ConnectedPiecesGroup) {
                puzzleViewController.pickUpPiece(((ConnectedPiecesGroup) a).getConnectedPieces(), new Vector2(
                        a.getX() - mousePositionInWorld.x,
                        a.getY() - mousePositionInWorld.y
                ));
                a.toFront();
            }
        }
        numPointersTouchedDown++;
        return false;
    }

    @Override
    public boolean touchUp(float x, float y, int pointer, int button) {
        numPointersTouchedDown--;
        puzzleViewController.dropPiece();
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        puzzleViewController.panBy(deltaX, deltaY);
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }


    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        // Drop any piece we are holding so the user can focus on zooming in and out
        puzzleViewController.dropPiece();

        float initialDistanceBetweenPivots = initialPointer1.dst(initialPointer2);
        float currentDistanceBetweenPivots = pointer1.dst(pointer2);

        float zoomChange = (initialDistanceBetweenPivots - currentDistanceBetweenPivots) * ZOOM_MULTIPLIER;
        puzzleViewController.zoomBy(zoomChange);
        return false;
    }

    @Override
    public void pinchStop() {

    }

}