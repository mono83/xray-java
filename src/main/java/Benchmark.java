import com.github.mono83.xray.BenchmarkArg;
import com.github.mono83.xray.BenchmarkArgs;
import com.github.mono83.xray.BenchmarkReplacer;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class Benchmark {
    public static void main(final String[] raw) throws Exception {
        Locale.setDefault(Locale.ROOT);

        Set<String> args = Arrays.stream(raw).map(String::trim).map(String::toLowerCase).collect(Collectors.toSet());

        OptionsBuilder builder = new OptionsBuilder();
        if (args.size() == 0 || args.contains("arg")) {
            builder.include(BenchmarkArg.class.getSimpleName());
        }
        if (args.size() == 0 || args.contains("args")) {
            builder.include(BenchmarkArgs.class.getSimpleName());
        }
        if (args.size() == 0 || args.contains("replacer")) {
            builder.include(BenchmarkReplacer.class.getSimpleName());
        }

        Options opt = builder
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    private Benchmark() {
    }
}
