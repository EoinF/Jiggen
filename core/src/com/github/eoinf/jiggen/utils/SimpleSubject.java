package com.github.eoinf.jiggen.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class SimpleSubject<T> extends SimpleObservable<T> {
    private final List<Consumer<T>> subscribers;

    private T currentValue;

    public T getValue() {
        return currentValue;
    }

    private SimpleSubject(T defaultValue) {
        this.currentValue = defaultValue;
        subscribers = new ArrayList<>();
    }

    @Override
    public void subscribe(Consumer<T> observer) {
        subscribers.add(observer);

        if (currentValue != null) {
            observer.accept(currentValue);
        }
    }

    @Override
    public void unsubscribe(Consumer<T> observer) {
        subscribers.remove(observer);
    }

    public void onNext(T t) {
        currentValue = t;
        for (Consumer<T> s: subscribers) {
            s.accept(t);
        }
    }



    public static <T> SimpleSubject<T> create() {
        return new SimpleSubject<T>(null);
    }

    public static <T> SimpleSubject<T> createDefault(T defaultValue) {
        return new SimpleSubject<>(defaultValue);
    }

}
