package com.github.eoinf.jiggen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class SimpleObservable<T> {

    T value;
    final List<Consumer<T>> subscribers;

    SimpleObservable() {
        subscribers = new ArrayList<>();
    }

    public void subscribe(Consumer<T> observer) {
        subscribers.add(observer);
    }

    public void unsubscribe(Consumer<T> observer) {
        subscribers.remove(observer);
    }


    public T getValue() {
        return this.value;
    }
}
