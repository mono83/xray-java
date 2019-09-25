package com.github.mono83.xray.events.consumers;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * Adapter to provided event consumer that makes all requests to process
 * asynchronously using bocking queue.
 *
 * @param <T> {@link com.github.mono83.xray.events.LoggingEvent} or {@link com.github.mono83.xray.events.MetricEvent}
 */
public class AsyncQueueAdapter<T> implements Consumer<T>, Runnable {
    /**
     * Blocking queue.
     */
    private final BlockingQueue<T> queue;
    /**
     * Next (real) event consumer.
     */
    private final Consumer<T> next;

    /**
     * Constructs new queue adapter that uses {@link LinkedBlockingDeque} and daemon thread.
     *
     * @param next Next (real) event consumer
     */
    public AsyncQueueAdapter(final Consumer<T> next) {
        this(new LinkedBlockingDeque<>(), next, null);
    }

    /**
     * Constructs new queue adapter that uses {@link LinkedBlockingDeque}.
     *
     * @param next    Next (real) event consumer
     * @param factory Thread factory to use. Optional, if null provided, new daemon thread will be created.
     */
    public AsyncQueueAdapter(final Consumer<T> next, final ThreadFactory factory) {
        this(new LinkedBlockingDeque<>(), next, factory);
    }

    /**
     * Constructs new queue adapter.
     *
     * @param queue   Blocking queue to use
     * @param next    Next (real) event consumer
     * @param factory Thread factory to use. Optional, if null provided, new daemon thread will be created.
     */
    public AsyncQueueAdapter(
            final BlockingQueue<T> queue,
            final Consumer<T> next,
            final ThreadFactory factory
    ) {
        this.queue = Objects.requireNonNull(queue, "queue");
        this.next = Objects.requireNonNull(next, "next");
        if (factory == null) {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        } else {
            factory.newThread(this).start();
        }
    }

    @Override
    public void accept(final T t) {
        if (t != null) {
            queue.add(t);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                next.accept(queue.take());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
