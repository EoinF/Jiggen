package com.github.eoinf.jiggen.views.Screens;

import java.util.function.Consumer;

public abstract class SimpleObservable<T> {
    public abstract void subscribe(Consumer<T> observer);
    public abstract void unsubscribe(Consumer<T> observer);
}
