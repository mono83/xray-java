import com.github.mono83.BenchmarkArg;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Benchmark {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(BenchmarkArg.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    private Benchmark() {
    }
}
