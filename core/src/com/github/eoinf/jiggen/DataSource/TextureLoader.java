package com.github.eoinf.jiggen.DataSource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.github.eoinf.jiggen.Models.Template;
import com.github.eoinf.jiggen.Models.TemplateLocal;
import com.github.eoinf.jiggen.Models.TemplateRemote;

public class TextureLoader {

    Texture loadFromTemplate(Template template) {
        if (template instanceof TemplateLocal) {
            FileHandle handle = ((TemplateLocal) template).getTemplateFile();
            return new Texture(handle);
        } else { //if (template instanceof TemplateRemote)
            return null;
        }
    }
}

