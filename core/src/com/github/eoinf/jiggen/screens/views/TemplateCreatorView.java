package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.TemplateCreator.TemplateCreator;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;

import java.util.function.Consumer;

public class TemplateCreatorView {
    private OrthographicCamera camera;
    private Stage stage;
    private Table root;
    private Image templateImage;
    private TemplateCreatorViewController templateCreatorViewController;

    private static final int IMAGE_PADDING = 10;

    public TemplateCreatorView(OrthographicCamera camera, SpriteBatch batch, Skin skin,
                               TemplateCreatorViewModel templateCreatorViewModel,
                               TemplateCreatorViewController templateCreatorViewController) {

        this.camera = camera;
        this.templateCreatorViewController = templateCreatorViewController;
        Viewport viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport, batch);
        this.root = new Table(skin);

        TemplateCreator templateCreator = new TemplateCreator(
                templateCreatorViewController,
                templateCreatorViewModel,
                new GridPoint2(viewport.getScreenWidth(), viewport.getScreenHeight())
        );

        Texture texture = new Texture(templateCreator.getGeneratedPixmap());
        templateImage = new Image(texture);
        templateImage.setScaling(Scaling.fit);

        root.setFillParent(true);
        root.add(templateImage).align(Align.center).pad(IMAGE_PADDING);
        stage.addActor(root);

        templateCreatorViewModel.getTemplatePixmapObservable().subscribe(new Consumer<Pixmap>() {
            @Override
            public void accept(Pixmap pixmap) {
                redrawTemplate(pixmap);
            }
        });
    }

    private void redrawTemplate(Pixmap pixmap) {
        templateImage.setDrawable(
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(pixmap)
                        )
                )
        );
    }

    public void act(float delta) {
        camera.update();

        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        templateCreatorViewController.setMaxSize(
                new GridPoint2(width - (IMAGE_PADDING * 2), height - (IMAGE_PADDING * 2)));
        camera.setToOrtho(true, width, height);
    }
}