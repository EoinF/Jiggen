package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.eoinf.jiggen.DataSource.TemplateLoader;
import com.github.eoinf.jiggen.Screens.TemplatePicker;

public class Jiggen extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;

	private static final float DEFAULT_CAMERA_SPEED = 200f;
	public static final int VIEWPORT_WIDTH = 1280;
	public static final int VIEWPORT_HEIGHT = 720;
	public static final String TEMPLATES_URL = "http://localhost:4567/templates";

	public TemplateLoader templateLoader = new TemplateLoader(TEMPLATES_URL);

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		this.setScreen(new TemplatePicker(this));
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

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
