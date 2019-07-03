package com.github.mono83;

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

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkArg {
    @Param({"null", "long", "generic"})
    public String type;
    @Param({"1", "10"})
    public int size;

    private Supplier<Arg> supplier;
    private Arg value;

    @Setup
    public void setUp() {
        switch (type) {
            case "generic":
                String[] values_g = new String[size];
                for (int i = 0; i < values_g.length; i++) {
                    values_g[i] = "foo" + i;
                }
                supplier = () -> new ArgGeneric<>("foo", values_g);
                break;
            case "long":
                long[] values_l = new long[size];
                for (int i = 0; i < values_l.length; i++) {
                    values_l[i] = i;
                }
                supplier = () -> new ArgLong("foo", values_l);
                break;
            default:
                supplier = () -> new ArgNull("foo");
        }

        value = supplier.get();
    }

    @Benchmark
    public Object create() {
        return supplier.get();
    }

    @Benchmark
    public int size() {
        return value.size();
    }

    @Benchmark
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Benchmark
    public String getFirst() {
        return value.getValue(0);
    }

    @Benchmark
    public String getLast() {
        return value.getValue(1_000_000);
    }
}
