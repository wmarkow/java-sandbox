package com.github.wmarkow.distiller.domain.interactor;

import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Should be used in foreground services only as foreground services work on different threads.
 */

public abstract class ForegroundServiceUseCase<T> {

    protected Subscription subscription = Subscriptions.empty();

    protected abstract Observable<T>buildUseCaseObservable();

    public abstract void destroy();

    public void execute(Subscriber<T> useCaseSubscriber) {
        unsubscribe();

        this.subscription = this.buildUseCaseObservable()
                .subscribe(useCaseSubscriber);
    }

    public void unsubscribe() {
        if (subscription.isUnsubscribed()) {
            return;
        }

        subscription.unsubscribe();
    }
}
