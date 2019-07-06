package com.github.mono83.xray;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Collection of arguments.
 * Immutable, thread safe.
 */
public interface Args extends Iterable<Arg> {
    /**
     * Maximum amount of arguments that can be packed
     * into array implementation.
     */
    int MAX_ARRAY_ARGS = 5;

    /**
     * Constructs arguments collection from provided
     * arguments. Implementation will be chosen
     * depends on amount of arguments provided.
     *
     * @param args Arguments to pack into collection
     * @return Arguments collection
     */
    static Args of(final Arg... args) {
        if (args == null || args.length == 0) {
            return ArgsEmpty.INSTANCE;
        } else if (args.length <= MAX_ARRAY_ARGS) {
            return new ArgsArray(args);
        } else {
            return new ArgsMap(args);
        }
    }

    /**
     * Constructs new arguments collection eagerly merging.
     * values and deduplicating it using {@link ArgsMap}
     *
     * @param left  Arguments collection
     * @param right Arguments to add, they will override
     *              existing ones if name collides.
     * @return New arguments collection
     */
    static Args append(final Args left, final Arg... right) {
        if (left == null || left.isEmpty()) {
            return of(right);
        }
        if (right == null || right.length == 0) {
            return left;
        }

        ArrayList<Arg> args = new ArrayList<>(left.size() + right.length);
        for (Arg a : left) {
            args.add(a);
        }
        for (Arg a : right) {
            args.add(a);
        }
        return new ArgsMap(args);
    }

    /**
     * Obtains logging argument by its name.
     *
     * @param key Argument key (name)
     * @return Logging argument if found
     */
    Optional<Arg> get(String key);

    /**
     * @return Amount of arguments within arguments collection
     */
    int size();

    /**
     * @return True if arguments collection is empty
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
