package com.github.eoinf.jiggen.Models;

public class TemplateRemote implements Template {

    private String id;
    private String name;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
