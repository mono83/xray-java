package com.github.mono83.xray;

import java.time.Duration;
import java.util.Collection;

/**
 * Enumeration of common logging argument names.
 */
public enum Names {
    RAY_ID("rayId", String.class),

    // Primarily string values
    NAME("name", String.class),
    TYPE("type", String.class),
    STATUS("status", String.class),
    STATE("state", String.class),
    SQL("sql", String.class),

    // Exceptions
    ERROR("error", Throwable.class), EXCEPTION("error", Throwable.class),

    // Durations
    ELAPSED("elapsed", Duration.class),
    TTL("ttl", Duration.class),

    // Primarily number values
    ID("id", long.class),
    GATE_ID("gateId", long.class), REALM_ID("gateId", long.class),
    COUNT("count", int.class), SIZE("count", int.class);

    /**
     * Constructs new enum value.
     *
     * @param name  Argument logging name
     * @param clazz Preferred class
     */
    Names(final String name, final Class clazz) {
        this.name = name;
        this.preferredClass = clazz;
    }

    /**
     * Argument name.
     */
    private final String name;
    /**
     * Preferred class.
     */
    private final Class preferredClass;

    /**
     * @return Argument name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Preferred class for this name
     */
    public Class getPreferredClass() {
        return preferredClass;
    }

    /**
     * Checks that argument name equals to current name.
     *
     * @param arg Argument to match key (name)
     * @return True if name matches
     */
    public boolean is(final Arg arg) {
        return arg != null && getName().equals(arg.getKey());
    }

    /**
     * Builds argument with string values.
     *
     * @param values Argument values
     * @return Logging argument
     */
    public Arg of(final String... values) {
        return ArgGeneric.of(getName(), values);
    }

    /**
     * Builds argument with enum values, that will
     * be converted to string.
     *
     * @param values Argument values
     * @return Logging argument
     */
    public Arg of(final Enum... values) {
        return ArgGeneric.of(getName(), values);
    }

    /**
     * Builds argument with {@link Duration} values.
     *
     * @param values Argument values
     * @return Logging argument
     */
    public Arg of(final Duration... values) {
        return ArgGeneric.of(getName(), values);
    }

    /**
     * Builds argument with long values.
     *
     * @param values Argument values
     * @return Logging argument
     */
    public Arg of(final long... values) {
        return ArgLong.of(getName(), values);
    }

    /**
     * Builds argument with integer values.
     *
     * @param values Argument values
     * @return Logging argument
     */
    public Arg of(final int... values) {
        return ArgLong.of(getName(), values);
    }

    /**
     * Builds argument (COUNT or SIZE in most cases) that
     * contains size of provided collection.
     *
     * @param collection Collection to obtains size from
     * @return Argument that contains size
     */
    public Arg of(final Collection collection) {
        if (collection == null) {
            return new ArgNull(getName());
        }
        return ArgLong.of(getName(), (long) collection.size());
    }

    /**
     * Builds argument (ERROR in most cases) that contains exception.
     *
     * @param error Exception to place into logging argument.
     * @return Argument that contains exception
     */
    public Arg of(final Throwable error) {
        return ArgException.of(getName(), error);
    }
}
