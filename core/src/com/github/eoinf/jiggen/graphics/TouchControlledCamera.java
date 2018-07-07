package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

public class TouchControlledCamera extends WorldBoundedCamera {

    private static float ZOOM_MULTIPLIER = 0.05f;

    private float initialZoom;
    private Map<Integer, Vector2> pointersInitial;
    private Map<Integer, Vector2> pointersCurrent;

    public TouchControlledCamera(OrthographicCamera camera, int worldWidth, int worldHeight) {
        super(camera, worldWidth, worldHeight);
        pointersInitial = new HashMap<>();
        pointersCurrent = new HashMap<>();
        initialZoom = -1;
    }

    public void setInitialPointer(float screenX, float screenY, int pointer) {
        this.pointersInitial.put(pointer, new Vector2(
                camera.position.x + (screenX * camera.zoom),
                camera.position.y + (screenY * camera.zoom))
        );
    }

    public void dragTo(float x, float y, int pointer) {
        Vector2 pivotCurrent = pointersInitial.get(pointer);
        if (pivotCurrent != null) {
            pointersCurrent.put(pointer, new Vector2(
                    pivotCurrent.x - (x * camera.zoom),
                    pivotCurrent.y - (y * camera.zoom))
            );
        }

        if (pointersInitial.size() == 1) {
            Vector2 delta = pointersCurrent.get(0);
            setX(delta.x);
            setY(delta.y);
        } else if (pointersInitial.size() == 2) {
            Vector2[] pointersInitialArray = new Vector2[2];
            pointersInitial.values().toArray(pointersInitialArray);
            Vector2[] pointersCurrentArray = new Vector2[2];
            pointersCurrent.values().toArray(pointersCurrentArray);

            float initialDistanceBetweenPivots = pointersInitialArray[0].dst2(pointersInitialArray[1]);
            float currentDistanceBetweenPivots = pointersCurrentArray[0].dst(pointersCurrentArray[1]);

            float zoomChange = (initialDistanceBetweenPivots - currentDistanceBetweenPivots) * ZOOM_MULTIPLIER;
            setZoom(initialZoom + zoomChange);
        }
    }

    public void liftPivot(int pointer) {
        pointersInitial.remove(pointer);
        pointersCurrent.remove(pointer);
    }
}
