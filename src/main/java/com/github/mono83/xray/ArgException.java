package com.github.mono83.xray;

import java.util.Objects;

/**
 * Special logging argument, that contains exception.
 */
public class ArgException implements Arg {
    /**
     * Contains logging argument key (name).
     */
    private final String key;
    /**
     * Contains argument value.
     */
    private final Throwable error;

    /**
     * Builds and returns argument instance for provided exception.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param key   Argument key (name)
     * @param error Argument values
     * @return Logging argument
     */
    public static Arg of(final String key, final Throwable error) {
        if (error == null) {
            return new ArgNull(key);
        }

        return new ArgException(key, error);
    }

    /**
     * Constructs new logging argument for exception.
     *
     * @param key   Argument key (name)
     * @param error Exception (not null)
     */
    ArgException(final String key, final Throwable error) {
        this.key = Objects.requireNonNull(key, "key");
        this.error = Objects.requireNonNull(error, "error");
    }

    /**
     * @return Exception
     */
    public Throwable getException() {
        return error;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue(final int index) {
        String message = error.getMessage();
        return message == null ? error.getClass().getName() : message;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArgException that = (ArgException) o;
        return key.equals(that.key) && error.equals(that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                key,
                error.getClass(),
                error.getMessage()
        );
    }
}
