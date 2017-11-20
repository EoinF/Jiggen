package com.github.eoinf.jiggen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jiggen extends Game {
	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		File[] files = new File("core/assets").listFiles();

		List<File> templateFiles = new ArrayList<>();
		System.out.println(files.length);
		for (File f: files) {
			System.out.println(f.getName());
			if (f.isFile() && f.getName().endsWith(".jpg")) {
				templateFiles.add(f);
			}
		}

		Random random = new Random();
		File randomTemplate = templateFiles.get(random.nextInt(templateFiles.size()));

		DecodedTemplate decodedTemplate = new DecodedTemplate(randomTemplate);
		this.setScreen(new PuzzleSolver(this, decodedTemplate.getGraph(), decodedTemplate.getTexture()));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
