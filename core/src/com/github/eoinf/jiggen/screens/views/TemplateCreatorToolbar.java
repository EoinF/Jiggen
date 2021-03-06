package com.github.eoinf.jiggen.screens.views;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.eoinf.jiggen.TemplateCreator.WaveDistortionData;
import com.github.eoinf.jiggen.TemplateCreator.lines.SinWave;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewController;
import com.github.eoinf.jiggen.screens.controllers.TemplateCreatorViewModel;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Random;
import java.util.function.Consumer;

import static com.github.eoinf.jiggen.webapp.utils.PixmapUtils.setBackgroundColour;

public class TemplateCreatorToolbar implements ScreenView {
    public Stage stage;
    File saveDirectory;

    public static final int TOOLBAR_HEIGHT = 50;

    public TemplateCreatorToolbar(OrthographicCamera camera,
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

        Table toolbarTable = new Table();
        setBackgroundColour(toolbarTable, new Color(0.3f, 0.6f, 1f, 1f));
        mainTable
                .add(toolbarTable)
                .expand()
                .fillX()
                .bottom();

        stage.addActor(mainTable);

        /*
         * ----------
         * Dimensions
         * ----------
         */
        GridPoint2 dimensions = templateCreatorViewModel.getTemplateDimensionsObservable().getValue();
        TextField dimensionsX = new TextField(String.valueOf(dimensions.x), skin);
        TextField dimensionsY = new TextField(String.valueOf(dimensions.y), skin);
        dimensionsX.setMaxLength(3);
        dimensionsY.setMaxLength(3);

        FocusListener dimensionsChangeListener = new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                try {
                    GridPoint2 newValue = new GridPoint2(
                            Integer.parseInt(dimensionsX.getText()),
                            Integer.parseInt(dimensionsY.getText())
                    );
                    if (!newValue.equals(templateCreatorViewModel.getTemplateDimensionsObservable().getValue())
                            && newValue.x > 0
                            && newValue.y > 0) {
                        templateCreatorViewController.setDimensions(newValue);
                    }
                } catch (NumberFormatException ex) {
                    // Ignore all inputs that aren't integers
                }
            }
        };

        dimensionsX.addListener(dimensionsChangeListener);
        dimensionsY.addListener(dimensionsChangeListener);

        templateCreatorViewModel.getTemplateDimensionsObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 dimensions) {
                dimensionsX.setText(String.valueOf(dimensions.x));
                dimensionsY.setText(String.valueOf(dimensions.y));
            }
        });

        toolbarTable.add(new Label("Dimensions", skin)).padLeft(2);
        toolbarTable.add(dimensionsX).width(40);
        toolbarTable.add(dimensionsY).width(40);


        /*
         * ----------
         * Aspect Ratio
         * ----------
         */
        Vector2 aspectRatio = templateCreatorViewModel.getTemplateAspectRatioObservable().getValue();
        TextField aspectRatioX = new TextField(String.valueOf(aspectRatio.x), skin);
        TextField aspectRatioY = new TextField(String.valueOf(aspectRatio.y), skin);
        aspectRatioX.setMaxLength(4);
        aspectRatioY.setMaxLength(4);

        FocusListener aspectRatioChangeListener = new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                try {
                    Vector2 newValue = new Vector2(
                            Float.parseFloat(aspectRatioX.getText()),
                            Float.parseFloat(aspectRatioY.getText())
                    );
                    if (!newValue.epsilonEquals(templateCreatorViewModel.getTemplateAspectRatioObservable().getValue())
                            && newValue.x > 0
                            && newValue.y > 0) {
                        templateCreatorViewController.setAspectRatio(newValue);
                    }
                } catch (NumberFormatException ex) {
                    // Ignore all inputs that aren't numbers
                }
            }
        };

        aspectRatioX.addListener(aspectRatioChangeListener);
        aspectRatioY.addListener(aspectRatioChangeListener);

        templateCreatorViewModel.getTemplateAspectRatioObservable().subscribe(new Consumer<Vector2>() {
            @Override
            public void accept(Vector2 aspectRatio) {
                aspectRatioX.setText(String.valueOf(aspectRatio.x));
                aspectRatioY.setText(String.valueOf(aspectRatio.y));
            }
        });

        toolbarTable.add(new Label("Aspect ratio", skin)).padLeft(2);
        toolbarTable.add(aspectRatioX).width(40);
        toolbarTable.add(aspectRatioY).width(40);


        /*
         * ----------
         * Wave distortion data
         * ----------
         */
        SinWave sinWave = (SinWave)(templateCreatorViewModel.getWaveDistortionObservable().getValue().distortionLine);
        TextField sinAmplitude = new TextField(String.valueOf(sinWave.sinAmplitude), skin);
        TextField sinPeriod = new TextField(String.valueOf(sinWave.sinPeriod), skin);
        TextField sinPhase = new TextField(String.valueOf(sinWave.sinPhase), skin);
        sinAmplitude.setMaxLength(7);
        sinPeriod.setMaxLength(7);
        sinPhase.setMaxLength(7);

        FocusListener waveDistortionChangeListener = new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                try {
                    WaveDistortionData newValue = new WaveDistortionData(
                            new SinWave(
                                    Double.parseDouble(sinAmplitude.getText()),
                                    Double.parseDouble(sinPhase.getText()),
                                    Double.parseDouble(sinPeriod.getText())
                            )
                    );
                    if (!newValue.equals(templateCreatorViewModel.getWaveDistortionObservable().getValue())) {
                        templateCreatorViewController.setWaveDistortionData(newValue);
                    }
                } catch (NumberFormatException ex) {
                    // Ignore all inputs that aren't numbers
                }
            }
        };

        sinAmplitude.addListener(waveDistortionChangeListener);
        sinPeriod.addListener(waveDistortionChangeListener);
        sinPhase.addListener(waveDistortionChangeListener);

        templateCreatorViewModel.getWaveDistortionObservable().subscribe(new Consumer<WaveDistortionData>() {
            @Override
            public void accept(WaveDistortionData waveDistortionData) {
                SinWave line = (SinWave)waveDistortionData.distortionLine;
                sinAmplitude.setText(String.format("%.3f", line.sinAmplitude));
                sinPeriod.setText(String.format("%.3f", line.sinPeriod));
                sinPhase.setText(String.format("%.3f", line.sinPhase));
            }
        });

        toolbarTable.add(new Label("p*Sin(q*A+r)", skin)).padLeft(2);
        toolbarTable.add(sinAmplitude).width(40);
        toolbarTable.add(sinPeriod).width(40);
        toolbarTable.add(sinPhase).width(40);


        /*
         * ----------
         * Random Seed
         * ----------
         */
        long randomSeed = templateCreatorViewModel.getRandomSeedObservable().getValue();
        TextField randomSeedField = new TextField(String.valueOf(randomSeed), skin);
        TextButton randomSeedButton = new TextButton("reroll", skin);

        randomSeedField.addListener(new FocusListener() {
            @Override
            public void keyboardFocusChanged(FocusEvent event, Actor actor, boolean focused) {
                try {
                    Long newValue = Long.parseLong(randomSeedField.getText());
                    if (!newValue.equals(templateCreatorViewModel.getRandomSeedObservable().getValue())) {
                        templateCreatorViewController.setRandomSeed(newValue);
                    }
                } catch (NumberFormatException ex) {
                    // Ignore all inputs that aren't numbers
                }
            }
        });

        randomSeedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                templateCreatorViewController.setRandomSeed(new Random().nextLong());
            }
        });

        templateCreatorViewModel.getRandomSeedObservable().subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long randomSeed) {
                randomSeedField.setText(String.valueOf(randomSeed));
            }
        });

        toolbarTable.add(new Label("Seed", skin)).padLeft(2);
        toolbarTable.add(randomSeedField);
        toolbarTable.add(randomSeedButton).width(40);


        /*
         * -----------
         * Save button
         * -----------
         */
        TextButton saveButton = new TextButton("Save", skin);

        toolbarTable
                .add(saveButton)
                .maxHeight(TOOLBAR_HEIGHT)
                .expandX()
                .align(Align.right);


        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                chooser.setCurrentDirectory(saveDirectory);

                GridPoint2 dimensions = templateCreatorViewModel.getTemplateDimensionsObservable().getValue();
                Vector2 aspectRatio = templateCreatorViewModel.getTemplateAspectRatioObservable().getValue();

                chooser.setSelectedFile(new File(
                        String.format("%s/%dx%d-%.1f_%.1f.png", chooser.getCurrentDirectory().getPath(),
                                dimensions.x, dimensions.y, aspectRatio.x, aspectRatio.y)
                ));

                chooser.setFileFilter(new FileNameExtensionFilter("png images", "png"));
                chooser.showDialog(null, "Save as PNG");
                File file = chooser.getSelectedFile();

                saveDirectory = chooser.getCurrentDirectory();

                if (file != null) {
                    if (!file.getPath().endsWith(".png")) {
                        file = new File(file.getPath() + ".png");
                    }
                    templateCreatorViewController.saveTemplateToFile(file);
                }
            }
        });

        templateCreatorViewModel.getResizeScreenObservable().subscribe(new Consumer<GridPoint2>() {
            @Override
            public void accept(GridPoint2 screenBounds) {
                viewport.update(screenBounds.x, screenBounds.y, true);
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
