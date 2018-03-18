package com.github.eoinf.jiggen.DataSource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.ByteArray;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.eoinf.jiggen.Models.Template;
import com.github.eoinf.jiggen.Models.TemplateLocal;
import com.github.eoinf.jiggen.Models.TemplateRemote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class TemplateService {

    private static HttpRequestBuilder httpRequestBuilder;
    private static String _endpoint;

    static {
        httpRequestBuilder = new HttpRequestBuilder();
    }

    public static void init(String endpoint) {
        _endpoint = endpoint;
    }

    public static List<Template> getLocalTemplates() {
        FileHandle[] files = Gdx.files.internal("templates").list();

        List<FileHandle> templateFiles = new ArrayList<>();
        for (FileHandle f : files) {
            if (!f.isDirectory() && f.name().endsWith(".jpg")) {
                templateFiles.add(f);
            }
        }
        return templateFiles.stream().map(TemplateLocal::new).collect(Collectors.toList());
    }

    public static CompletableFuture<List<TemplateRemote>> getRemoteTemplates() {
        CompletableFuture<List<TemplateRemote>> future = new CompletableFuture<>();

        Net.HttpRequest request = httpRequestBuilder
                .newRequest()
                .method(Net.HttpMethods.GET)
                .url(_endpoint + "/templates")
                .build();

        Net.HttpResponseListener responseListener = new Net.HttpResponseListener() {
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

    public static CompletableFuture<Texture> getTemplateImage(String resource) {
        CompletableFuture<Texture> future = new CompletableFuture<>();

        Net.HttpRequest request = httpRequestBuilder
                .newRequest()
                .method(Net.HttpMethods.GET)
                .url(_endpoint + "/images/" + resource)
                .build();

        Net.HttpResponseListener responseListener = new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    ByteArray data = ByteArray.with(httpResponse.getResult());

                    Pixmap pixmap = new Pixmap(data.items, 0, data.size);
                    Gdx.app.postRunnable(() -> future.complete(new Texture(pixmap)));
                } else {
                    byte[] data = httpResponse.getResult();
                    Json json = new Json();
                    HashMap<String, String> values = json.fromJson(HashMap.class, new String(data));

                    System.out.println(values.get("error"));
                    System.out.println("status = " + httpResponse.getStatus().getStatusCode());
                }
            }

            @Override
            public void failed(Throwable t) {
                System.out.println(t.getMessage());
            }

            @Override
            public void cancelled() {

            }
        };

        Gdx.net.sendHttpRequest(request, responseListener);
        return future
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }
}
