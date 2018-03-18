package com.github.eoinf.jiggen.View.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraControlledScreen implements Screen {

    private static final float DEFAULT_CAMERA_SPEED = 200f;

    private OrthographicCamera camera;

    public CameraControlledScreen(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        updateCameraInput(delta);
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

    private void updateCameraInput(float delta) {
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
