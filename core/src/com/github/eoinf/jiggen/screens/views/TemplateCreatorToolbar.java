package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;
import com.github.eoinf.jiggen.utils.PixmapUtils;

import javax.swing.JFileChooser;
import java.io.File;
import java.util.function.Consumer;

public class TemplateCreatorToolbar implements ScreenView {
    public Stage stage;

    public static final int TOOLBAR_HEIGHT = 50;

    public TemplateCreatorToolbar(OrthographicCamera camera,
                                  SpriteBatch batch,
                                  Skin skin,
                                  TemplateCreatorViewModel templateCreatorViewModel,
                                  TemplateCreatorViewController templateCreatorViewController) {
        Viewport viewport = new ScreenViewport();
        stage = new Stage(viewport);

        //
        // Layout
        //
        Table mainTable = new Table();
        mainTable.setFillParent(true);
//        PixmapUtils.setBackgroundColour(mainTable, Color.GREEN);

        Table toolbarTable = new Table();
        PixmapUtils.setBackgroundColour(toolbarTable, Color.BLUE);
        mainTable
                .add(toolbarTable)
                .expand()
                .fillX()
                .bottom();

        TextButton saveButton = new TextButton("Save", skin);

        toolbarTable
                .add(saveButton)
                .height(TOOLBAR_HEIGHT)
                .expandX()
                .align(Align.right);

        stage.addActor(mainTable);

        templateCreatorViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 screenBounds) {
                viewport.update(screenBounds.x, screenBounds.y, true);
            }
        });

        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                JFileChooser chooser = new JFileChooser();
                chooser.setApproveButtonText("Save");
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                chooser.showDialog(null, "Select a file");
                File file = chooser.getSelectedFile();

                if (file != null) {
                    templateCreatorViewController.saveTemplateToFile(file);
                }
            }
        });
    }

    public void act(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw() {
        stage.draw();
    }
}
