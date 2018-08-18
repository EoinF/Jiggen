package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.eoinf.jiggen.graphics.WorldBoundedCamera;
import com.github.eoinf.jiggen.views.Screens.EnhancedGestureListener;
import com.github.eoinf.jiggen.views.Screens.PuzzleViewModel;

public class PuzzleGestureListener implements EnhancedGestureListener {

    private Stage stage;
    private PuzzleViewModel puzzleViewModel;
    private WorldBoundedCamera boundedCamera;
    private static float ZOOM_MULTIPLIER = 0.005f;
    private int numPointersTouchedDown = 0;

    public PuzzleGestureListener(Stage stage, PuzzleViewModel puzzleViewModel, WorldBoundedCamera camera, Skin skin) {
        this.stage = stage;
        this.puzzleViewModel = puzzleViewModel;
        this.boundedCamera = camera;
    }

    private boolean isTouching() {
        return numPointersTouchedDown > 0;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector3 mousePositionOnScreen = new Vector3(x, y, 0);

        Vector3 mousePositionInWorld = boundedCamera.unproject(mousePositionOnScreen);
        Actor a = stage.hit(mousePositionInWorld.x, mousePositionInWorld.y, true);

        if (a != null && !isTouching()) {
            if (a instanceof PuzzlePieceGroup) {
                puzzleViewModel.pickUpPiece((PuzzlePieceGroup) a, new Vector2(
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
        Vector3 mousePositionOnScreen = new Vector3(x, y, 0);

        Vector3 mousePositionInWorld = boundedCamera.unproject(mousePositionOnScreen);
        numPointersTouchedDown--;
        puzzleViewModel.dropPiece(mousePositionInWorld.x, mousePositionInWorld.y);
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
        if (!puzzleViewModel.isHoldingPiece()) {
            System.out.println("Pan to " + x + ", " + y + " -> " + deltaX + ", " + deltaY);
            // Assume only one pointer is being used (Panning is only possible with a single pointer)
            boundedCamera.translate(-deltaX, deltaY);
        }
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
        float initialDistanceBetweenPivots = initialPointer1.dst(initialPointer2);
        float currentDistanceBetweenPivots = pointer1.dst(pointer2);

        float zoomChange = (initialDistanceBetweenPivots - currentDistanceBetweenPivots) * ZOOM_MULTIPLIER;
        boundedCamera.zoomBy(zoomChange);
        return false;
    }

    @Override
    public void pinchStop() {

    }

}