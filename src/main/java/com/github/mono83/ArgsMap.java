package com.github.mono83;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

public class ArgsMap implements Args {
    private final HashMap<String, Arg> args;

    public ArgsMap(final Arg... args) {
        Objects.requireNonNull(args, "args");
        this.args = new HashMap<>(args.length);
        for (Arg a : args) {
            this.args.put(a.getKey(), a);
        }
    }

    @Override
    public Optional<Arg> get(final String key) {
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
