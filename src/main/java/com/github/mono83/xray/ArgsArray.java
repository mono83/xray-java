package com.github.mono83.xray;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Arguments collection, implemented as array.
 * This implementation is not effective on CPU,
 * but has very low memory footprint.
 * <p>
 * Should not be used on large collections.
 * <p>
 * Does not provided deduplication.
 */
public class ArgsArray implements Args {
    /**
     * Arguments collection.
     */
    private final Arg[] args;

    /**
     * Constructs arguments collection on top of array.
     *
     * @param args Arguments to use
     */
    public ArgsArray(final Arg... args) {
        Objects.requireNonNull(args, "args");
        this.args = args;
    }

    @Override
    public Optional<Arg> get(final String key) {
        Objects.requireNonNull(key, "key");

        for (int i = args.length - 1; i >= 0; i--) {
            if (key.equals(args[i].getKey())) {
                return Optional.of(args[i]);
            }
        }

        return Optional.empty();
    }

    @Override
    public int size() {
        return args.length;
    }

    @Override
    public Iterator<Arg> iterator() {
        return Arrays.stream(args).iterator();
    }
}
