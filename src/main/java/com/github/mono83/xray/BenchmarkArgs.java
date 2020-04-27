package com.github.mono83.xray;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkArgs {
    @Param({"array", "map", "cascade", "tree"})
    public String type;
    @Param({"1", "10", "1000"})
    public int size;

    private Supplier<Args> supplier;
    private Args values;

    @Setup
    public void setUp() {
        ArrayList<Arg> args = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            args.add(new ArgNull("foo" + i));
        }
        Arg[] argsArray = args.toArray(new Arg[0]);

        switch (type) {
            case "array":
                supplier = () -> new ArgsArray(argsArray);
                break;
            case "map":
                supplier = () -> new ArgsMap(args);
                break;
            case "cascade":
                supplier = () -> {
                    ArgsCascade last = null;
                    for (Arg a : args) {
                        last = new ArgsCascade(a, last);
                    }
                    return last;
                };
                break;
            case "tree":
                supplier = () -> new ArgsTreeSet(args);
                break;
            default:
                throw new AssertionError("Unsupported " + type);
        }

        values = supplier.get();
    }

    @Benchmark
    public Object create() {
        return supplier.get();
    }

    @Benchmark
    public Object iterate() {
        Arg value = null;
        for (Arg arg : values) {
            value = arg;
        }
        return value;
    }

    @Benchmark
    public Object getFirst() {
        return values.get("foo0");
    }

    @Benchmark
    public Object getLast() {
        return values.get("foo" + (size-1));
    }

    @Benchmark
    public Object getMiss() {
        return values.get("foo");
    }
}
