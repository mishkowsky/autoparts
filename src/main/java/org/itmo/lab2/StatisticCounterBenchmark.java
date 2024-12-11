package org.itmo.lab2;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class StatisticCounterBenchmark {

    @Param({"0", "5"}) // delay in ns (1000000000 наносек (ns) = 1000000 микросек (us) = 1000 миллисек = 1 сек)
    private long delay = 0;

    @Param({"10", "30", "50", "75", "100", "300", "500", "750", "1000"})
    private int n = 10;

    private List<Purchase> purchases;

    @Setup(Level.Iteration)
    public void setupPurchases() {
        purchases = Purchase.PurchaseGenerator.generatePurchases(n, delay,
                Product.ProductGenerator.generateProducts(delay),
                Buyer.BuyersGenerator.generateBuyers(100, delay));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StatisticCounterBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void countStandardCollectorsParallel(Blackhole bh) {
        StatisticsCounter.Result result = StatisticsCounter.countStandardCollectorsParallel(purchases);
        bh.consume(result);
    }

    @Benchmark
    public void countStandardCollectors(Blackhole bh) {
        StatisticsCounter.Result result = StatisticsCounter.countStandardCollectors(purchases);
        bh.consume(result);
    }

    @Benchmark
    public void countCustomCollectorWithForkJoin(Blackhole bh) {
        StatisticsCounter.Result result = StatisticsCounter.countForkJoin(purchases);
        bh.consume(result);
    }
}