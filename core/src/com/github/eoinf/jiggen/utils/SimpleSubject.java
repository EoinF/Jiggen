package com.github.eoinf.jiggen.utils;

import java.util.function.Consumer;

public final class SimpleSubject<T> extends SimpleObservable<T> {

    private SimpleSubject(T defaultValue) {
        super();
        this.value = defaultValue;
    }

    public void onNext(T t) {
        value = t;
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
