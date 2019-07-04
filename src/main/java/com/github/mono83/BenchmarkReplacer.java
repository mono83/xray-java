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
import java.util.function.BiFunction;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class BenchmarkReplacer {

    private final Args args = Args.of(
            Names.ID.of(365762, 2),
            Names.TYPE.of("micro"),
            Names.NAME.of("admin"),
            Names.STATUS.of("success")
    );
    private final String pattern = "Transaction :type :id for user :name on realm :id completed with status :status";
    private BiFunction<String, Args, String> replacer;

    @Param({"simple", "regex"})
    private String type;

    @Setup
    public void setUp() {
        switch (type) {
            case "simple":
                replacer = new ReplacerColon();
                break;
            case "regex":
                replacer = new ReplacerRegex();
                break;
            default:
                replacer = null;
        }
    }

    @Benchmark
    public String standard() {
        return replacer.apply(pattern, args);
    }
}
