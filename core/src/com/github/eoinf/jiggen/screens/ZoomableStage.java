package com.github.eoinf.jiggen.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ZoomableStage extends Stage {

    private static final float MAX_ZOOM = 4;
    private static final float MIN_ZOOM = 0.1f;
    private static final float ZOOM_RATE = 0.1f;

    public ZoomableStage(OrthographicCamera camera, SpriteBatch batch) {
        super(new ScreenViewport(camera), batch);
        addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int scrollDirection) {
                camera.zoom += scrollDirection * ZOOM_RATE;
                camera.zoom = Math.max(MIN_ZOOM, camera.zoom);
                camera.zoom = Math.min(MAX_ZOOM, camera.zoom);
                return true;
            }
        });
    }
}
