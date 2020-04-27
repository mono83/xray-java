package com.github.mono83.xray;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.TreeSet;

public class ArgsTreeSet implements Args {
    private final TreeSet<Arg> values;

    ArgsTreeSet(final Collection<Arg> values) {
        this.values = new TreeSet<>();
        for (Arg value : values) {
            this.values.remove(value);
            this.values.add(value);
        }
    }

    @Override
    public Optional<Arg> get(final String name) {
        return Optional.ofNullable(find(name));
    }

    private Arg find(final String name) {
        Arg candidate = values.ceiling(new ArgNull(name));
        return candidate != null && candidate.getName().equals(name)
                ? candidate
                : null;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public Iterator<Arg> iterator() {
        return values.iterator();
    }
}
