package com.github.mono83.xray;

import java.util.Iterator;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Tree set implementation of arguments collection.
 */
public class ArgsTreeSet implements Args {
    /**
     * Arguments.
     */
    private final TreeSet<Arg> values;

    /**
     * Constructor.
     *
     * @param values Values to place into collection
     * @param second Second values collection, used to override first one
     */
    ArgsTreeSet(final Iterable<Arg> values, final Iterable<Arg> second) {
        this.values = new TreeSet<>();
        for (Arg value : values) {
            if (!this.values.add(value)) {
                this.values.remove(value);
                this.values.add(value);
            }
        }
        if (second != null) {
            for (Arg value : second) {
                if (!this.values.add(value)) {
                    this.values.remove(value);
                    this.values.add(value);
                }
            }
        }
    }

    @Override
    public Optional<Arg> get(final String name) {
        return Optional.ofNullable(find(name));
    }

    /**
     * Locates argument by name.
     *
     * @param name Argument name
     * @return Found argument or null
     */
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
