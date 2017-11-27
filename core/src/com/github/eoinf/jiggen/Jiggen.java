package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.github.eoinf.jiggen.Screens.BackgroundPicker;
import com.github.eoinf.jiggen.Screens.PuzzleSolver;
import com.github.eoinf.jiggen.Screens.TemplatePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jiggen extends Game {
	public SpriteBatch batch;

	public OrthographicCamera camera;

	private static final float DEFAULT_CAMERA_SPEED = 200f;
	private static final int VIEWPORT_WIDTH = 1280;
	private static final int VIEWPORT_HEIGHT = 720;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

		this.setScreen(new BackgroundPicker(this));
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
