package com.github.eoinf.jiggen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.Lml;
import com.github.eoinf.jiggen.DataSource.TemplateService;
import com.github.eoinf.jiggen.views.HotReloadLmlApplicationListener;
import com.github.eoinf.jiggen.views.Overview;
import com.github.eoinf.jiggen.views.ViewManager;

public class Jiggen extends HotReloadLmlApplicationListener {
    private static final String BACKEND_ENDPOINT = "http://localhost:4567/";

    @Override
    public void create() {
        ViewManager.init();
        TemplateService.init(BACKEND_ENDPOINT);
        super.create();
        setView(Overview.class);
    }

    @Override
    protected LmlParser createParser() {
        LmlParser parser = Lml.parser(ViewManager.skin).strict(false).build();
        return parser;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ViewManager.batch.setProjectionMatrix(ViewManager.camera.combined);
        super.render();
    }

    @Override
    public void dispose() {
        ViewManager.batch.dispose();
        ViewManager.skin.dispose();
    }
}
