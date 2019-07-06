package com.github.mono83.xray.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.Args;

import java.util.Iterator;
import java.util.Optional;

/**
 * Lazy implementation of {@link Args} arguments collection.
 * This instance is used within logging and metric events, just
 * to avoid arguments merging on events, that are not really used.
 * <p>
 * So this implementation merges events only when they are requested.
 * <p>
 * Thread safe.
 */
class LazyMergedArgs implements Args {
    /**
     * Merges provided arguments.
     *
     * @param args Arguments collection from ray
     * @param a    Arguments collection from event
     * @return Resulting lazy arguments collection
     */
    static Args of(final Args args, final Arg... a) {
        if (args == null || a == null || args.size() == 0 || a.length == 0) {
            return Args.append(args, a);
        }

        return new LazyMergedArgs(args, a);
    }

    /**
     * Arguments from ray.
     */
    private final Args rayArgs;
    /**
     * Arguments from event.
     */
    private final Arg[] eventArgs;
    /**
     * Merged arguments.
     */
    private volatile Args merged;

    /**
     * Constructs lazy arguments collection.
     *
     * @param args Arguments from ray
     * @param a    Arguments from event
     */
    private LazyMergedArgs(final Args args, final Arg[] a) {
        this.rayArgs = args;
        this.eventArgs = a;
    }

    /**
     * Calculates merged arguments collection.
     *
     * @return Merged arguments collection
     */
    private Args getMerged() {
        if (merged == null) {
            synchronized (this) {
                if (merged == null) {
                    merged = Args.append(rayArgs, eventArgs);
                }
            }
        }

        return merged;
    }

    @Override
    public Optional<Arg> get(final String key) {
        return getMerged().get(key);
    }

    @Override
    public int size() {
        return getMerged().size();
    }

    @Override
    public Iterator<Arg> iterator() {
        return getMerged().iterator();
    }
}
