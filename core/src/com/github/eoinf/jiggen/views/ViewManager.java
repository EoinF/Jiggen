package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public final class ViewManager {

    public static SpriteBatch batch;
    public static OrthographicCamera camera;
    public static Skin skin;

    public static final int VIEWPORT_WIDTH = 1280;
    public static final int VIEWPORT_HEIGHT = 720;

    public static void init() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/Holo-dark-hdpi.json"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    public static Stage createStage() {
        return new Stage(new ScreenViewport(camera), batch);
    }
}
