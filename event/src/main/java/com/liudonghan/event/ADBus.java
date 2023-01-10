package com.liudonghan.event;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:2023/1/10
 */
public class ADBus {
    private static final String TAG = "RxBus";
    private static ADBus instance;

    public static synchronized ADBus get() {
        if (instance == null) {
            synchronized (ADBus.class) {
                if (instance == null) {
                    instance = new ADBus();
                }
            }
        }
        return instance;
    }

    private ADBus() {
    }

    private ConcurrentHashMap<Object, List<Subject<ADEvent, ADEvent>>> subjectMapper = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Observable, Subscription> subscriptionMapper = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public Observable<ADEvent> register(@NonNull String tag) {
        return register(tag,null);
    }

    public Observable<ADEvent> register(@NonNull String tag, ADEventSubscriber<ADEvent> eventSubscriber) {
        List<Subject<ADEvent, ADEvent>> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<ADEvent, ADEvent> subject = PublishSubject.create();
        subjectList.add(subject);
        if (eventSubscriber != null) {
            subject.subscribe(eventSubscriber);
            subscriptionMapper.put(subject, eventSubscriber);
        }
        return subject;
    }


    public void unregister(@NonNull String tag, Observable<ADEvent> observable) {
        if (observable == null){
            return;
        }
        List<Subject<ADEvent, ADEvent>> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (subjects.isEmpty()) {
                subjectMapper.remove(tag);
            }
        }

        if (subscriptionMapper.containsKey(observable)) {
            Subscription eventSubscriber = subscriptionMapper.get(observable);
            if (!eventSubscriber.isUnsubscribed()) {
                eventSubscriber.unsubscribe();
            }
            subscriptionMapper.remove(observable);
        }
    }

    public void post(@NonNull String tag) {
        post(tag, new ADEvent());
    }

    public void post(@NonNull String tag, @NonNull ADEvent event) {
        List<Subject<ADEvent, ADEvent>> subjectList = subjectMapper.get(tag);
        if (subjectList != null && !subjectList.isEmpty()) {
            for (Subject<ADEvent, ADEvent> subject : subjectList) {
                subject.onNext(event);
            }
        }
    }

    public void postNoChange(@NonNull String tag){
        ADEvent event = new ADEvent();
        event.status = ADEvent.STATUS_NO_MORE;
        post(tag, event);
    }

    public void postSuccess(@NonNull String tag) {
        ADEvent event = new ADEvent();
        event.status = ADEvent.STATUS_OK;
        post(tag, event);
    }

    public void postNoCompleted(@NonNull String tag) {
        ADEvent event = new ADEvent();
        event.status = ADEvent.STATUS_NOT_COMPLETE;
        post(tag, event);
    }


    public void postError(@NonNull String tag, @NonNull Throwable error) {
        ADEvent event = new ADEvent();
        event.status = ADEvent.STATUS_FAILED;
        event.setError(error);
        post(tag, event);
    }
}
