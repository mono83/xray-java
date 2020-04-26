package com.github.mono83.xray;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Experimental cascade arguments collection.
 */
public class ArgsCascade implements Args {
    /**
     * Parent arguments collection.
     */
    private final Args parent;
    /**
     * Argument on top of cascade.
     */
    private final Arg arg;
    /**
     * Total amount of items in cascade.
     */
    private final int size;

    /**
     * Constructor.
     *
     * @param arg    Argument on top of cascade.
     * @param parent Parent arguments collection, optional.
     */
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
        return new CascadeIterator(this);
    }

    /**
     * Iterator implementation for arguments cascade.
     */
    private static class CascadeIterator implements Iterator<Arg> {
        /**
         * Parent iterator.
         */
        private final Iterator<Arg> parent;
        /**
         * Argument on top of cascade.
         */
        private final Arg arg;
        /**
         * True when iterator reaches end.
         */
        private boolean read = false;

        /**
         * Iterator constructor.
         *
         * @param cascade Arguments cascade for iterator.
         */
        CascadeIterator(final ArgsCascade cascade) {
            this.parent = cascade.parent == null ? null : cascade.parent.iterator();
            this.arg = cascade.arg;
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
                if (candidate.getName().equals(arg.getName())) {
                    // Skipping values with same name
                    return this.next();
                }
                return candidate;
            }
            read = true;
            return arg;
        }
    }
}
