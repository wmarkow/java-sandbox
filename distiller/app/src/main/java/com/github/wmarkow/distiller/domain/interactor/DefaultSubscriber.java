package com.github.wmarkow.distiller.domain.interactor;

public class DefaultSubscriber<T> extends rx.Subscriber<T> {
    @Override
    public void onStart() {
        // do nothing by default
    }

    @Override
    public void onCompleted() {
        // no-op by default.
    }

    @Override
    public void onError(Throwable e) {
        // no-op by default.
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }
}