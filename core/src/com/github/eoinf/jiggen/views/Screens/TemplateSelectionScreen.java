package com.github.eoinf.jiggen.views.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.eoinf.jiggen.DataSource.TemplateService;
import com.github.eoinf.jiggen.Models.TemplateRemote;
import com.github.eoinf.jiggen.views.Widgets.TemplatePickerTable;

import java.util.List;


public class TemplateSelectionScreen extends CameraControlledScreen {

    private Stage pickerStage;
    private TemplatePickerTable templateSelectionTable;

    public TemplateSelectionScreen(OrthographicCamera camera, SpriteBatch batch, Skin skin) {
        super(camera);

        pickerStage = new Stage(new ScreenViewport(camera), batch);
        pickerStage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    //ScreenManager.switchToPuzzleOverview();
                }
                return super.keyDown(event, keycode);
            }
        });
        Gdx.input.setInputProcessor(pickerStage);

        System.out.println("Fetching remote template list");

        TemplateService.getRemoteTemplates()
                .thenAccept(this::onFetchTemplates);

        templateSelectionTable = new TemplatePickerTable(skin);
        pickerStage.addActor(templateSelectionTable);
    }

    private void onFetchTemplates(List<TemplateRemote> templateRemotes) {
        System.out.println("Got remote template list");

        for (TemplateRemote template : templateRemotes) {
            System.out.printf("name=%s, id=%s\n", template.getName(), template.getId());

            TemplateService.getTemplateImage(template.getId() + "." + template.getExtension())
                    .thenAccept(image ->
                    {
                            template.setTexture(image);
                            templateSelectionTable.addTemplate(template);
                    });
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        pickerStage.act();
        pickerStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
