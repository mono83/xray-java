package com.github.mono83.xray;

import java.util.Objects;

/**
 * Special logging argument, that contains exception.
 */
public class ArgException implements Arg {
    /**
     * Contains logging argument name.
     */
    private final String name;
    /**
     * Contains argument value.
     */
    private final Throwable error;
    /**
     * Constructs new logging argument for exception.
     *
     * @param name  Argument name
     * @param error Exception (not null)
     */
    ArgException(final String name, final Throwable error) {
        this.name = Objects.requireNonNull(name, "name");
        this.error = Objects.requireNonNull(error, "error");
    }

    /**
     * Builds and returns argument instance for provided exception.
     * Will return {@link ArgNull} if empty values provided.
     *
     * @param name  Argument name
     * @param error Argument values
     * @return Logging argument
     */
    public static Arg of(final String name, final Throwable error) {
        if (error == null) {
            return new ArgNull(name);
        }

        return new ArgException(name, error);
    }

    /**
     * @return Exception
     */
    public Throwable getException() {
        return error;
    }

    @Override
    public String getName() {
        return name;
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
        return name.equals(that.name) && error.equals(that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name,
                error.getClass(),
                error.getMessage()
        );
    }
}
