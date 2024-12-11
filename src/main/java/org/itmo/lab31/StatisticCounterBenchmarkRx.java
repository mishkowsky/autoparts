package org.itmo.lab31;

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
public class StatisticCounterBenchmarkRx {

    @Param({"1"}) // delay in ns (1000000000 наносек (ns) = 1000000 микросек (us) = 1000 миллисек = 1 сек)
    private long delay = 1;

//    @Param({"500", "2000"})
    @Param({"500", "2000"})
    private int n = 500;

    private List<Purchase> purchases;

    @Setup(Level.Iteration)
    public void setupPurchases() {
        purchases = Purchase.PurchaseGenerator.generatePurchases(n, delay,
                Product.ProductGenerator.generateProducts(delay),
                Buyer.BuyersGenerator.generateBuyers(100, delay));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StatisticCounterBenchmarkRx.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void countStandardCollectorsParallel(Blackhole bh) {
        StatisticsCounter.Result result = StatisticsCounter.countStandardCollectorsParallel(purchases);
        bh.consume(result);
    }
// System.out.println(expected.getBestBuyer());
//        System.out.println(countRxSimple(purchases).getBestBuyer());
//        System.out.println(countRxWithCallableAction(purchases).getBestBuyer());
//        System.out.println(countNonConcurrentRxWithCallable(purchases).getBestBuyer());
//        System.out.println(countRxWithCollectors(purchases).getBestBuyer());
//        System.out.println(countRxWithParallelCollectors(purchases).getBestBuyer());
//
//        System.out.println(countRxGroupByCategory(purchases).getBestBuyer());
//
//        System.out.println(countRxNestedObservablesWithConcurrentMap(purchases).getBestBuyer());
//        System.out.println(countRxNestedObservableWithMap(purchases).getBestBuyer());
    @Benchmark
    public void countRxSimple(Blackhole bh) {
        StatisticsCounterRx.Result result = StatisticsCounterRx.countRxSimple(purchases);
        bh.consume(result);
    }

    @Benchmark
    public void countRxWithCallableAction(Blackhole bh) {
        StatisticsCounterRx.Result result = StatisticsCounterRx.countRxWithCallableAction(purchases);
        bh.consume(result);
    }
    @Benchmark
    public void countNonConcurrentRxWithCallable(Blackhole bh) {
        StatisticsCounterRx.NonConcurrentResult result = StatisticsCounterRx.countNonConcurrentRxWithCallable(purchases);
        bh.consume(result);
    }
    @Benchmark
    public void countRxWithCollectors(Blackhole bh) {
        StatisticsCounterRx.Result result = StatisticsCounterRx.countRxWithCollectors(purchases);
        bh.consume(result);
    }
    @Benchmark
    public void countRxWithParallelCollectors(Blackhole bh) {
        StatisticsCounterRx.Result result = StatisticsCounterRx.countRxWithParallelCollectors(purchases);
        bh.consume(result);
    }
    @Benchmark
    public void countRxGroupByCategory(Blackhole bh) {
        StatisticsCounterRx.NonConcurrentResult result = StatisticsCounterRx.countRxGroupByCategory(purchases);
        bh.consume(result);
    }
    @Benchmark
    public void countRxNestedObservablesWithConcurrentMap(Blackhole bh) {
        StatisticsCounterRx.Result result = StatisticsCounterRx.countRxNestedObservablesWithConcurrentMap(purchases);
        bh.consume(result);
    }
    @Benchmark
    public void countRxNestedObservableWithMap(Blackhole bh) {
        StatisticsCounterRx.NonConcurrentResult result = StatisticsCounterRx.countRxNestedObservableWithMap(purchases);
        bh.consume(result);
    }
}