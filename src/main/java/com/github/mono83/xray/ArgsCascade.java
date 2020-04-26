package com.github.mono83.xray;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Experimental cascade arguments collection
 */
public class ArgsCascade implements Args {
    private final Args parent;
    private final Arg arg;
    private final int size;

    ArgsCascade(final Arg arg, final Args parent) {
        this.arg = Objects.requireNonNull(arg, "arg");
        this.parent = parent;
        this.size = parent == null ? 1 : 1 + parent.size();
    }

    @Override
    public Optional<Arg> get(final String name) {
        return arg.getName().equals(name)
                ? Optional.of(arg)
                : (parent == null ? Optional.empty() : parent.get(name));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Iterator<Arg> iterator() {
        return new CascadeIterator(
                parent == null ? null : parent.iterator(),
                this
        );
    }

    /**
     * Iterator implementation for arguments cascade
     */
    private static class CascadeIterator implements Iterator<Arg> {
        private final Iterator<Arg> parent;
        private final ArgsCascade cascade;
        private boolean read = false;

        CascadeIterator(final Iterator<Arg> parent, final ArgsCascade cascade) {
            this.parent = parent;
            this.cascade = cascade;
        }

        @Override
        public boolean hasNext() {
            return !read;
        }

        @Override
        public Arg next() {
            if (read) {
                throw new NoSuchElementException();
            }
            if (parent != null && parent.hasNext()) {
                Arg candidate = parent.next();
                if (candidate.getName().equals(cascade.arg.getName())) {
                    // Skipping values with same name
                    return this.next();
                }
                return candidate;
            }
            read = true;
            return cascade.arg;
        }
    }
}