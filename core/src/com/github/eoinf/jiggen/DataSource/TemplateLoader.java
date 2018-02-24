package com.github.eoinf.jiggen.DataSource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.eoinf.jiggen.Models.Template;
import com.github.eoinf.jiggen.Models.TemplateLocal;
import com.github.eoinf.jiggen.Models.TemplateRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TemplateLoader {

    private HttpRequestBuilder httpRequestBuilder;
    private String templatesUrl;

    public TemplateLoader(String templatesUrl) {
        this.httpRequestBuilder = new HttpRequestBuilder();
        this.templatesUrl = templatesUrl;
    }

    public List<Template> getLocalTemplates() {
        FileHandle[] files = Gdx.files.internal("templates").list();

        List<FileHandle> templateFiles = new ArrayList<>();
        for (FileHandle f : files) {
            if (!f.isDirectory() && f.name().endsWith(".jpg")) {
                templateFiles.add(f);
            }
        }
        return templateFiles.stream().map(TemplateLocal::new).collect(Collectors.toList());
    }

    public CompletableFuture<List<TemplateRemote>> getRemoteTemplates() {
        CompletableFuture<List<TemplateRemote>> future = new CompletableFuture<>();

        Net.HttpRequest request = httpRequestBuilder
                .newRequest()
                .method(Net.HttpMethods.GET)
                .url(templatesUrl)
                .build();

        Net.HttpResponseListener responseListener = new Net.HttpResponseListener (){
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                Json json = new Json();
                ArrayList<JsonValue> values = json.fromJson(ArrayList.class, httpResponse.getResultAsString());

                List<TemplateRemote> templates = values.stream()
                        .map(val -> json.readValue(TemplateRemote.class, val))
                        .collect(Collectors.toList());
                future.complete(templates);
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        };

        Gdx.net.sendHttpRequest(request, responseListener);
        return future;
    }

}
