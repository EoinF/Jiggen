package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.github.eoinf.jiggen.views.ScreenManager;
import com.github.eoinf.jiggen.views.ViewManager;

public class Jiggen extends Game {
	public static final String BACKEND_ENDPOINT = "http://localhost:4567/";

	
	@Override
	public void create () {
		ViewManager.init();
		ScreenManager.init(this);
		//TemplateService.init(BACKEND_ENDPOINT);

		ScreenManager.switchToPuzzleOverview();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ViewManager.batch.setProjectionMatrix(ViewManager.camera.combined);
		super.render();
	}
	
	@Override
	public void dispose () {
		ViewManager.batch.dispose();
	}
}
