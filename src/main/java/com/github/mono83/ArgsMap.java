package com.github.mono83;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

/**
 * Arguments collection, implemented as map.
 * Provides values deduplication.
 */
public class ArgsMap implements Args {
    private final HashMap<String, Arg> args;

    /**
     * Constructs new arguments collection from provided arguments.
     *
     * @param args Arguments
     */
    public ArgsMap(final Arg... args) {
        Objects.requireNonNull(args, "args");
        this.args = new HashMap<>(args.length);
        for (Arg a : args) {
            this.args.put(a.getKey(), a);
        }
    }

    /**
     * Constructs new arguments collection from provided collection.
     *
     * @param source Iterable arguments collection
     */
    public ArgsMap(final Iterable<Arg> source) {
        Objects.requireNonNull(source, "source");
        this.args = new HashMap<>();
        for (Arg a : source) {
            this.args.put(a.getKey(), a);
        }
    }

    @Override
    public Optional<Arg> get(final String key) {
        Objects.requireNonNull(key, "key");
        return Optional.ofNullable(args.get(key));
    }

    @Override
    public int size() {
        return args.size();
    }

    @Override
    public Iterator<Arg> iterator() {
        return args.values().iterator();
    }
}
