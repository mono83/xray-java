package com.github.mono83.xray.generators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * Incremental ray id generator.
 * Each new ray will have new incremental number.
 */
public class Incremental implements Supplier<String> {
    /**
     * Counter.
     */
    private final AtomicLong counter = new AtomicLong();

    @Override
    public String get() {
        return String.valueOf(counter.incrementAndGet());
    }
}
