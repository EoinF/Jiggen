package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;

import java.util.function.Consumer;

public class TemplateCreatorView {
    private OrthographicCamera camera;
    private Stage stage;
    private Table root;
    private TemplateCreator templateCreator;
    private Image templateImage;


    public TemplateCreatorView(OrthographicCamera camera, SpriteBatch batch, Skin skin,
                               TemplateCreatorViewModel templateCreatorViewModel,
                               TemplateCreatorViewController templateCreatorViewController) {

        this.camera = camera;
        Viewport viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport, batch);
        this.root = new Table(skin);

        templateCreator = new TemplateCreator();

        templateImage = new Image(new Texture(templateCreator.getGeneratedPixmap()));
        templateImage.setScaling(Scaling.fit);

        root.setFillParent(true);
        root.add(templateImage).align(Align.center).pad(10);
        stage.addActor(root);

        templateCreatorViewModel.getTemplateAspectRatio().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 aspectRatio) {
                templateCreator.setAspectRatio(aspectRatio);
                redrawTemplate();
            }
        });

        templateCreatorViewModel.getTemplateDimensions().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 dimensions) {
                templateCreator.setDimensions(dimensions);
                redrawTemplate();
            }
        });
    }

    private void redrawTemplate() {
        templateImage.setDrawable(
                new TextureRegionDrawable(
                        new TextureRegion(
                                new Texture(templateCreator.getGeneratedPixmap())
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
        camera.setToOrtho(true, width, height);
    }
}