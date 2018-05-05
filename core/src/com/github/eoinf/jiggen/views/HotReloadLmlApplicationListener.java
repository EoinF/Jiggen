package com.github.eoinf.jiggen.views;

import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.github.czyzby.lml.util.LmlApplicationListener;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

/**
 * An implementation of {@link LmlApplicationListener} that watches the lml templates given by created views and reloads
 * a view when its respective file is modified.
 * Simply swap your {@link LmlApplicationListener} with this class to get hot reload functionality in your project
 *
 * @author EF
 */
public abstract class HotReloadLmlApplicationListener extends LmlApplicationListener {

    private WatchService watcher;
    private List<Path> watchedPaths;
    private Map<Path, AbstractLmlView> viewsMap;

    public HotReloadLmlApplicationListener() {
        super();
        viewsMap = new HashMap<>();
        watchedPaths = new ArrayList<>();
        try {
            watcher = FileSystems.getDefault().newWatchService();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void render() {
        super.render();
        watchViewFiles();
    }

    private void watchViewFiles() {
        // wait for key to be signalled
        WatchKey key = watcher.poll();

        if (key != null) {
            handleKeyEvent(key);
        }
    }

    private void handleKeyEvent(WatchKey key) {
        key.pollEvents()
                .stream()
                .filter(event -> event.kind() == ENTRY_MODIFY)
                .map(WatchEvent::context)
                .filter(viewsMap::containsKey)
                .forEach(path -> {
                    try {
                        reloadView(viewsMap.get(path));
                    } catch(GdxRuntimeException ex) {
                        System.err.println(ex.getMessage());
                    }
                });

        // Reset the key so it can watch for file changes again
        boolean valid = key.reset();
        if (!valid) {
            System.err.println("Error resetting watch key");
        }
    }

    @Override
    public void initiateView(AbstractLmlView view) {
        super.initiateView(view);
        Path viewPath = Paths.get(view.getTemplateFile().path());
        Path viewsDir = viewPath.getParent();

        if (!watchedPaths.contains(viewsDir)) {
            try {
                viewsDir.register(watcher,
                        ENTRY_MODIFY);
                watchedPaths.add(viewsDir);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

        viewsMap.put(viewPath.getFileName(), view);
    }

    @Override
    public void dispose() {
        try {
            watcher.close();
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

