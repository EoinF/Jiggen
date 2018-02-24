package com.github.eoinf.jiggen.Models;

import com.badlogic.gdx.files.FileHandle;

public class TemplateLocal implements Template {

    private String id;
    private String name;
    private FileHandle templateFile;

    public TemplateLocal(FileHandle templateFile) {
        this.templateFile = templateFile;
        this.id = templateFile.path();
        this.name = templateFile.nameWithoutExtension();
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
