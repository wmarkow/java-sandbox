package com.github.wmarkow.distiller.domain.interactor;

import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by wmarkowski on 2018-03-23.
 */

public abstract class UseCase {

    protected final ThreadExecutor threadExecutor;
    protected final PostExecutionThread postExecutionThread;

    protected Subscription subscription = Subscriptions.empty();

    protected UseCase() {
        threadExecutor = null;
        postExecutionThread = null;
    }

    protected UseCase(ThreadExecutor threadExecutor,
                      PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable buildUseCaseObservable();

    public abstract void destroy();

    public void execute(Subscriber useCaseSubscriber) {
        unsubscribe();

        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(useCaseSubscriber);
    }

    public void unsubscribe() {
        if (subscription.isUnsubscribed()) {
            return;
        }

        subscription.unsubscribe();
    }
}
