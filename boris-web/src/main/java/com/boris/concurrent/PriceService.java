package com.boris.concurrent;

import com.google.common.util.concurrent.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class PriceService<T> {
    ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    public DeferredResult<T> getDeferredResult() {
        DeferredResult<T> deferredResult = new DeferredResult<>(5000L);

        ListenableFuture<T> listenableFuture = listeningExecutorService.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return getResult();
            }
        });

        Futures.addCallback(listenableFuture, new FutureCallback<T>() {
            @Override
            public void onSuccess(T t) {
                deferredResult.setResult(t);
            }

            @Override
            public void onFailure(Throwable throwable) {
                deferredResult.setErrorResult(throwable);
            }
        });
        return deferredResult;
    }

    public abstract T getResult();
}
