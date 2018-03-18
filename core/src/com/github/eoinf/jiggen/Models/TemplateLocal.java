package com.github.eoinf.jiggen.Models;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class TemplateLocal implements Template {

    private String id;
    private String name;
    private String extension;
    private FileHandle templateFile;

    public TemplateLocal(FileHandle templateFile) {
        this.templateFile = templateFile;
        this.id = templateFile.path();
        this.name = templateFile.nameWithoutExtension();
    }

    @Override
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public Texture getTexture() {
        throw new NotImplementedException();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public FileHandle getTemplateFile() {
        return templateFile;
    }
}
