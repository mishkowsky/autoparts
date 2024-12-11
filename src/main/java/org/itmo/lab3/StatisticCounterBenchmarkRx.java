package org.itmo.lab3;

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

    @Param({"5"}) // delay in ns (1000000000 наносек (ns) = 1000000 микросек (us) = 1000 миллисек = 1 сек)
    private long delay = 5;

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

//Benchmark                                                                        (delay)   (n)  Mode  Cnt          Score          Error  Units
//o.i.lab3.StatisticCounterBenchmarkRx.countNonConcurrentRxWithCallable                  5   500  avgt    5        920,902 �       26,289  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countNonConcurrentRxWithCallable                  5  2000  avgt    5       4226,481 �     1256,394  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxGroupByCategory                            5   500  avgt    5      11547,245 �     2907,021  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxGroupByCategory                            5  2000  avgt    5      47819,065 �     6871,714  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxNestedObservableWithMap                    5   500  avgt    5       1224,812 �      309,341  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxNestedObservableWithMap                    5  2000  avgt    5       6957,274 �     5115,878  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxNestedObservablesWithConcurrentMap         5   500  avgt    5       1287,123 �      103,178  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxNestedObservablesWithConcurrentMap         5  2000  avgt    5       5614,751 �      819,496  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxSimple                                     5   500  avgt    5       3207,813 �     1404,012  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxSimple                                     5  2000  avgt    5      11640,700 �      624,879  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxWithCallableAction                         5   500  avgt    5       1275,383 �      254,433  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxWithCallableAction                         5  2000  avgt    5       5230,710 �      475,552  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxWithCollectors                             5   500  avgt    5       1928,568 �      148,865  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxWithCollectors                             5  2000  avgt    5       9087,681 �      263,401  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxWithParallelCollectors                     5   500  avgt    5       2039,824 �      218,172  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countRxWithParallelCollectors                     5  2000  avgt    5       9513,264 �      286,106  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countStandardCollectorsParallel                   5   500  avgt    5       1100,312 �       74,472  us/op
//o.i.lab3.StatisticCounterBenchmarkRx.countStandardCollectorsParallel                   5  2000  avgt    5       3884,040 �      343,315  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countNonConcurrentRxWithCallable                 1   500  avgt    5     155271,923 �     2051,470  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countNonConcurrentRxWithCallable                 1  2000  avgt    5     268559,855 �     7129,968  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxGroupByCategory                           1   500  avgt    5   15141638,280 �  1527516,960  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxGroupByCategory                           1  2000  avgt    5   72473004,660 � 42992824,638  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxNestedObservableWithMap                   1   500  avgt    5      52277,411 �    13800,939  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxNestedObservableWithMap                   1  2000  avgt    5     216634,044 �    40241,186  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxNestedObservablesWithConcurrentMap        1   500  avgt    5      53930,921 �    12341,257  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxNestedObservablesWithConcurrentMap        1  2000  avgt    5     218410,664 �    53725,606  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxSimple                                    1   500  avgt    5   32643519,400 �  3115909,513  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxSimple                                    1  2000  avgt    5  128678908,320 �  4685607,201  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxWithCallableAction                        1   500  avgt    5     155035,688 �     2024,910  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxWithCallableAction                        1  2000  avgt    5     302288,168 �    10819,336  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxWithCollectors                            1   500  avgt    5      59974,416 �      755,456  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxWithCollectors                            1  2000  avgt    5     126474,858 �     4541,129  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxWithParallelCollectors                    1   500  avgt    5      60131,887 �     4371,216  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countRxWithParallelCollectors                    1  2000  avgt    5     127056,326 �     3090,409  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countStandardCollectorsParallel                  1   500  avgt    5    1962792,343 �   145658,296  us/op
//o.i.lab31.StatisticCounterBenchmarkRx.countStandardCollectorsParallel                  1  2000  avgt    5    7423023,970 �   408317,322  us/op