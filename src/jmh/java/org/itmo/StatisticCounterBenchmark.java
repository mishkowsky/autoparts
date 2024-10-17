package org.itmo;

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
@Warmup(iterations = 3) // прогрев JVM, чтобы всякие оптимизации внутри произошли
@Measurement(iterations = 5) // кол-во замеров
public class StatisticCounterBenchmark {

    @Param({"0", "5"}) // delay in ns (1000000000 ns = 1 sec)
    private long delay = 0;

    @Param({"16", "32", "48", "64"})//, "100000"})//, "250000", "500000"})
    private int n = 50;

    private List<Purchase> purchases;

    @Setup(Level.Invocation)
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