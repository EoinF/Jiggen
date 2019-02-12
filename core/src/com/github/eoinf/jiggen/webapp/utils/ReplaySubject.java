package com.github.eoinf.jiggen.webapp.utils;


import java.util.function.Consumer;

public class ReplaySubject<T> extends SimpleObservable<T> {

    private ReplaySubject(T defaultValue) {
        super(defaultValue);
    }

    public void onNext(T t) {
        setValue(t);
    }

    @Override
    public void subscribe(Consumer<T> observer) {
        super.subscribe(observer);
        observer.accept(this.getValue());
    }

    public static <T> ReplaySubject<T> create() {
        return new ReplaySubject<>(null);
    }

    public static <T> ReplaySubject<T> createDefault(T defaultValue) {
        return new ReplaySubject<>(defaultValue);
    }

}
