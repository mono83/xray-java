package com.github.mono83.xray.events;

import com.github.mono83.xray.Arg;
import com.github.mono83.xray.Args;
import com.github.mono83.xray.generators.Incremental;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Event based implementation of {@link com.github.mono83.xray.Ray}.
 */
public class Ray extends Abstract {
    /**
     * Static reference to root-level ray.
     */
    public static final Ray ROOT;
    /**
     * Static reference for special ray, that should be used in
     * application startup process.
     */
    public static final Ray BOOT;

    static {
        ROOT = new Ray("ROOT", "", new Incremental());
        BOOT = ROOT.fork().with("BOOT");
    }

    /**
     * Parent ray.
     */
    final Ray parent;
    /**
     * Ray ID generator.
     */
    final Supplier<String> rayIdGenerator;
    /**
     * Ray ID.
     */
    final String id;
    /**
     * Ray logger name.
     */
    final String name;
    /**
     * Ray metric prefix.
     */
    final String metricPrefix;
    /**
     * Ray arguments.
     */
    final Args args;

    /**
     * Registered consumers.
     */
    volatile Consumer<LoggingEvent>[] consumersLog;
    /**
     * Registered consumers.
     */
    volatile Consumer<MetricEvent>[] consumersMetric;

    /**
     * Utility method, used to concatenate two metric prefixes.
     *
     * @param prefix Prefix
     * @param value  Value
     * @return Concatenated value
     */
    static String concat(final String prefix, final String value) {
        if (prefix == null || prefix.length() == 0) {
            return value;
        }
        if (value == null || value.length() == 0) {
            return prefix;
        }

        return prefix.trim() + "." + value.trim();
    }

    /**
     * Public constructor, used to create top level (root) ray.
     *
     * @param name           Ray name
     * @param metricPrefix   Metric prefix
     * @param rayIdGenerator Ray ID generator
     */
    public Ray(final String name, final String metricPrefix, final Supplier<String> rayIdGenerator) {
        this.parent = null;
        this.rayIdGenerator = Objects.requireNonNull(rayIdGenerator, "rayIdGenerator");
        this.id = "ROOT";
        this.name = Objects.requireNonNull(name, "name");
        this.metricPrefix = Objects.requireNonNull(metricPrefix, "metricPrefix");
        this.args = Args.of();
    }

    /**
     * Private constructor, used to clone ray with new arguments.
     *
     * @param parent       Parent ray
     * @param id           Ray identifier
     * @param name         Name of new ray
     * @param metricPrefix Metric prefix to append
     * @param args         Arguments
     */
    private Ray(
            final Ray parent,
            final String id,
            final String name,
            final String metricPrefix,
            final Args args
    ) {
        this.parent = parent;
        this.rayIdGenerator = parent.rayIdGenerator;
        this.id = id == null ? parent.id : id;
        this.name = name == null ? parent.name : name;
        this.metricPrefix = metricPrefix == null
                ? parent.metricPrefix
                : concat(parent.metricPrefix, metricPrefix);
        this.args = args == null ? parent.args : args;
    }

    @Override
    protected void emit(final LoggingEvent event) {
        if (event != null) {
            Consumer<LoggingEvent>[] local = consumersLog;
            if (local != null) {
                for (Consumer<LoggingEvent> consumer : local) {
                    consumer.accept(event);
                }
            }
            if (parent != null) {
                parent.emit(event);
            }
        }
    }

    @Override
    protected void emit(final MetricEvent event) {
        if (event != null) {
            Consumer<MetricEvent>[] local = consumersMetric;
            if (local != null) {
                for (Consumer<MetricEvent> consumer : local) {
                    consumer.accept(event);
                }
            }
            if (parent != null) {
                parent.emit(event);
            }
        }
    }

    /**
     * Registers new consumer, that will receive all logs, emitted
     * by current ray and forked one.
     *
     * @param consumer Consumer to register
     */
    @SuppressWarnings("unchecked")
    public synchronized void handleLogs(final Consumer<LoggingEvent> consumer) {
        if (consumer != null) {
            if (consumersLog == null) {
                consumersLog = new Consumer[]{consumer};
            } else {
                Consumer<LoggingEvent>[] newArray = new Consumer[consumersLog.length + 1];
                System.arraycopy(consumersLog, 0, newArray, 0, consumersLog.length);
                newArray[newArray.length - 1] = consumer;
                consumersLog = newArray;
            }
        }
    }

    /**
     * Registers new consumer, that will receive all logs, emitted
     * by current ray and forked one.
     *
     * @param consumer Consumer to register
     */
    @SuppressWarnings("unchecked")
    public synchronized void handleMetrics(final Consumer<MetricEvent> consumer) {
        if (consumer != null) {
            if (consumersMetric == null) {
                consumersMetric = new Consumer[]{consumer};
            } else {
                Consumer<MetricEvent>[] newArray = new Consumer[consumersMetric.length + 1];
                System.arraycopy(consumersMetric, 0, newArray, 0, consumersMetric.length);
                newArray[newArray.length - 1] = consumer;
                consumersMetric = newArray;
            }
        }
    }

    @Override
    protected Args getArgs() {
        return args;
    }

    @Override
    protected String composeMetricName(final String suffix) {
        if (suffix == null || suffix.length() == 0) {
            return this.metricPrefix;
        }
        return concat(this.metricPrefix, suffix);
    }

    @Override
    public Ray fork() {
        return new Ray(this, rayIdGenerator.get(), null, null, null);
    }

    @Override
    public Ray with(final String name) {
        if (name == null || name.isEmpty()) {
            return this;
        }
        return new Ray(this, null, name, null, null);
    }

    @Override
    public Ray with(final Supplier<String> nameSupplier) {
        return this.with(Objects.requireNonNull(nameSupplier, "nameSupplier").get());
    }

    @Override
    public Ray with(final String name, final String metricPrefix) {
        return new Ray(
                this,
                null,
                Objects.requireNonNull(name, "name"),
                Objects.requireNonNull(metricPrefix, "metricPrefix"),
                null
        );
    }

    @Override
    public Ray with(final Arg... args) {
        if (args == null || args.length == 0) {
            return this;
        }
        return new Ray(this, null, null, null, Args.append(this.args, args));
    }
}
