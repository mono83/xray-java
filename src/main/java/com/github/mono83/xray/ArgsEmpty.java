package com.github.mono83.xray;

import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

/**
 * Empty logging arguments collection.
 */
final class ArgsEmpty implements Args {
    /**
     * Empty arguments collection instance.
     */
    static final ArgsEmpty INSTANCE = new ArgsEmpty();

    @Override
    public Optional<Arg> get(final String name) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Iterator<Arg> iterator() {
        return Collections.emptyIterator();
    }
}
