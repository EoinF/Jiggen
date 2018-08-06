package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class TouchControlledCamera extends WorldBoundedCamera {

    private static float ZOOM_MULTIPLIER = 0.005f;

    public TouchControlledCamera(OrthographicCamera camera) {
        super(camera);
    }

    public void panBy(float deltaX, float deltaY) {
        translate(deltaX, deltaY);
    }

    public void pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 currentPointer1, Vector2 currentPointer2) {
        float initialDistanceBetweenPivots = initialPointer1.dst(initialPointer2);
        float currentDistanceBetweenPivots = currentPointer1.dst(currentPointer2);

        float zoomChange = (initialDistanceBetweenPivots - currentDistanceBetweenPivots) * ZOOM_MULTIPLIER;
        zoomBy(zoomChange);
    }
}
