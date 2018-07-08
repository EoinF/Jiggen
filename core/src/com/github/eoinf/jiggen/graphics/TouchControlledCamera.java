package com.github.eoinf.jiggen.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

public class TouchControlledCamera extends WorldBoundedCamera {

    private static float ZOOM_MULTIPLIER = 0.005f;

    private float initialZoom;
    private Vector3 initialCameraPosition;
    private Map<Integer, Vector2> pointersInitial;
    private Map<Integer, Vector2> pointersCurrent;

    private TouchMode currentMode;

    public TouchControlledCamera(OrthographicCamera camera, int worldWidth, int worldHeight) {
        super(camera, worldWidth, worldHeight);
        pointersInitial = new HashMap<>();
        pointersCurrent = new HashMap<>();
        initialZoom = -1;
        currentMode = TouchMode.NONE;
    }

    public void setInitialPointer(float screenX, float screenY, int pointer) {
        this.initialZoom = camera.zoom;
        this.initialCameraPosition = this.camera.position.cpy();
        this.pointersInitial.put(pointer, new Vector2(screenX, screenY));
    }

    public void dragTo(float x, float y, int pointer) {
        pointersCurrent.put(pointer, new Vector2(x, y));

        if (pointersInitial.size() == 1) {
            if (currentMode == TouchMode.PAN) {
                Vector2 initial = pointersInitial.get(pointer);
                Vector2 current = pointersCurrent.get(pointer);
                float deltaX = (initial.x - current.x) * initialZoom;
                float deltaY = (initial.y - current.y) * initialZoom;

                setX(initialCameraPosition.x + deltaX);
                setY(initialCameraPosition.y + deltaY);
            } else if (currentMode == TouchMode.NONE) {
                currentMode = TouchMode.PAN;
            }
        } else if (pointersInitial.size() == 2) {
            if (currentMode == TouchMode.PINCH) {
                Vector2[] pointersInitialArray = new Vector2[2];
                pointersInitial.values().toArray(pointersInitialArray);
                Vector2[] pointersCurrentArray = new Vector2[2];
                pointersCurrent.values().toArray(pointersCurrentArray);

                float initialDistanceBetweenPivots = pointersInitialArray[0].dst(pointersInitialArray[1]);
                float currentDistanceBetweenPivots = pointersCurrentArray[0].dst(pointersCurrentArray[1]);

                float zoomChange = (initialDistanceBetweenPivots - currentDistanceBetweenPivots) * ZOOM_MULTIPLIER;
                setZoom(initialZoom + zoomChange);
            } else {
                currentMode = TouchMode.PINCH;
            }
        }
    }

    public void liftPivot(int pointer) {
        pointersInitial.remove(pointer);
        pointersCurrent.remove(pointer);

        if (pointersInitial.size() == 0) {
            currentMode = TouchMode.NONE;
        }
    }

    public boolean isPinching() {
        return currentMode == TouchMode.PINCH;
    }

    enum TouchMode {
        NONE,
        PAN,
        PINCH
    }
}
