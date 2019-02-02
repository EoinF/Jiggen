package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
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
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;

import java.util.function.Consumer;

public class TemplateCreatorView implements ScreenView {
    private OrthographicCamera camera;
    public Stage stage;
    private Table root;
    private Image templateImage;

    private static final int IMAGE_PADDING = 10;

    public TemplateCreatorView(OrthographicCamera camera, SpriteBatch batch, Skin skin,
                               TemplateCreatorViewModel templateCreatorViewModel,
                               TemplateCreatorViewController templateCreatorViewController) {

        this.camera = camera;
        Viewport viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport, batch);
        this.root = new Table(skin);

        TemplateCreator templateCreator = new TemplateCreator(
                new GridPoint2(viewport.getScreenWidth(), viewport.getScreenHeight()),
                templateCreatorViewModel.getTemplateAspectRatioObservable().getValue(),
                templateCreatorViewModel.getTemplateDimensionsObservable().getValue(),
                templateCreatorViewModel.getWaveDistortionObservable().getValue(),
                templateCreatorViewModel.getRandomSeedObservable().getValue()
        );

        Texture texture = new Texture(templateCreator.getGeneratedPixmap());
        templateImage = new Image(texture);
        templateImage.setScaling(Scaling.fit);

        root.setFillParent(true);
        root.add(templateImage)
                .align(Align.center)
                .pad(IMAGE_PADDING)
                .padTop(TemplateCreatorToolbar.TOOLBAR_HEIGHT);
        stage.addActor(root);

        templateCreatorViewModel.getTemplatePixmapObservable().subscribe(new Consumer<Pixmap>() {
            @Override
            public void accept(Pixmap pixmap) {
                redrawTemplate(pixmap);
            }
        });

        templateCreatorViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 size) {
                int width = size.x;
                int height = size.y;
                stage.getViewport().update(width, height);
                templateCreatorViewController.setMaxSize(
                        new GridPoint2(width - (IMAGE_PADDING * 2), height - (IMAGE_PADDING * 2) - TemplateCreatorToolbar.TOOLBAR_HEIGHT));
                camera.setToOrtho(true, width, height);
            }
        });

        templateCreatorViewModel.getTemplateAspectRatioObservable().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 aspectRatio) {
                templateCreator.setAspectRatio(aspectRatio);
                templateCreatorViewController.setPixmap(templateCreator.getGeneratedPixmap());
            }
        });

        templateCreatorViewModel.getTemplateDimensionsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 dimensions) {
                templateCreator.setDimensions(dimensions);
                templateCreatorViewController.setPixmap(templateCreator.getGeneratedPixmap());
            }
        });

        templateCreatorViewModel.getTemplateMaxSizeObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 maxSize) {
                templateCreator.setMaxSize(maxSize);
                templateCreatorViewController.setPixmap(templateCreator.getGeneratedPixmap());
            }
        });

        templateCreatorViewModel.getWaveDistortionObservable().subscribe(new Consumer<WaveDistortionData>() {
            @Override
            public void accept(WaveDistortionData waveDistortionData) {
                templateCreator.setWaveDistortion(waveDistortionData);
                templateCreatorViewController.setPixmap(templateCreator.getGeneratedPixmap());
            }
        });

        templateCreatorViewModel.getRandomSeedObservable().subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long randomSeed) {
                templateCreator.setRandomSeed(randomSeed);
                templateCreatorViewController.setPixmap(templateCreator.getGeneratedPixmap());
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
}