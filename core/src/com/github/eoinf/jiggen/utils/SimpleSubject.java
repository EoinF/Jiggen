package com.github.eoinf.jiggen.utils;

public final class SimpleSubject<T> extends SimpleObservable<T>{

    protected SimpleSubject(T defaultValue) {
        super(defaultValue);
    }

    public void onNext(T t) {
        this.setValue(t);
    }

    public static <T> SimpleSubject<T> create() {
        return new SimpleSubject<>(null);
    }

    public static <T> SimpleSubject<T> createDefault(T defaultValue) {
        return new SimpleSubject<>(defaultValue);
    }

}
