package com.liudonghan.event;

import rx.Subscriber;

public abstract class ADEventSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        onEvent(t);
    }

    public abstract void onEvent(T t);
}
