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
public class FlowableBenchmark {

    @Param({"50000", "100000", "1000000", "2000000", "3000000", "4000000", "5000000"})
    private int n = 50000;

    private List<Purchase> purchases;

    @Setup(Level.Iteration)
    public void setupPurchases() {
        int delay = 0;
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
    public void benchmarkRun(Blackhole bh) {
        StatisticsCounterRx.Result result = StatisticsCounterRxFlowable.count(purchases);
        bh.consume(result);
    }
}

//C:\Users\aleks\.jdks\openjdk-23\bin\java.exe "-javaagent:C:\Users\aleks\AppData\Local\JetBrains\IntelliJ IDEA Community Edition 2024.2.1\lib\idea_rt.jar=9314:C:\Users\aleks\AppData\Local\JetBrains\IntelliJ IDEA Community Edition 2024.2.1\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath C:\Users\aleks\IdeaProjects\autoparts\target\classes;C:\Users\aleks\.m2\repository\org\openjdk\jmh\jmh-core\1.36\jmh-core-1.36.jar;C:\Users\aleks\.m2\repository\net\sf\jopt-simple\jopt-simple\5.0.4\jopt-simple-5.0.4.jar;C:\Users\aleks\.m2\repository\org\apache\commons\commons-math3\3.2\commons-math3-3.2.jar;C:\Users\aleks\.m2\repository\io\reactivex\rxjava3\rxjava\3.1.9\rxjava-3.1.9.jar;C:\Users\aleks\.m2\repository\org\reactivestreams\reactive-streams\1.0.4\reactive-streams-1.0.4.jar;C:\Users\aleks\.m2\repository\org\javatuples\javatuples\1.2\javatuples-1.2.jar org.openjdk.jmh.Main org.itmo.lab3.FlowableBenchmark.benchmarkRun$
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 50000)
//
//# Run progress: 0,00% complete, ETA 00:09:20
//# Fork: 1 of 1
//# Warmup Iteration   1: 168779,788 us/op
//# Warmup Iteration   2: 170469,615 us/op
//# Warmup Iteration   3: 173644,516 us/op
//Iteration   1: 172493,373 us/op
//Iteration   2: 171058,198 us/op
//Iteration   3: 169309,035 us/op
//Iteration   4: 170896,176 us/op
//Iteration   5: 168114,493 us/op
//
//
//Result "org.itmo.lab3.FlowableBenchmark.benchmarkRun":
//  170374,255 �(99.9%) 6521,147 us/op [Average]
//  (min, avg, max) = (168114,493, 170374,255, 172493,373), stdev = 1693,521
//  CI (99.9%): [163853,108, 176895,403] (assumes normal distribution)
//
//
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 100000)
//
//# Run progress: 14,29% complete, ETA 00:08:20
//# Fork: 1 of 1
//# Warmup Iteration   1: 356294,969 us/op
//# Warmup Iteration   2: 338280,560 us/op
//# Warmup Iteration   3: 337425,587 us/op
//Iteration   1: 326611,274 us/op
//Iteration   2: 331729,139 us/op
//Iteration   3: 334013,257 us/op
//Iteration   4: 344753,427 us/op
//Iteration   5: 339535,567 us/op
//
//
//Result "org.itmo.lab3.FlowableBenchmark.benchmarkRun":
//  335328,533 �(99.9%) 27037,166 us/op [Average]
//  (min, avg, max) = (326611,274, 335328,533, 344753,427), stdev = 7021,466
//  CI (99.9%): [308291,367, 362365,698] (assumes normal distribution)
//
//
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 1000000)
//
//# Run progress: 28,57% complete, ETA 00:07:03
//# Fork: 1 of 1
//# Warmup Iteration   1: 3874598,300 us/op
//# Warmup Iteration   2: 3479340,233 us/op
//# Warmup Iteration   3: 3994781,267 us/op
//Iteration   1: 3746306,767 us/op
//Iteration   2: 3826079,767 us/op
//Iteration   3: 3756199,233 us/op
//Iteration   4: 3633363,933 us/op
//Iteration   5: 3352273,175 us/op
//
//
//Result "org.itmo.lab3.FlowableBenchmark.benchmarkRun":
//  3662844,575 �(99.9%) 719489,650 us/op [Average]
//  (min, avg, max) = (3352273,175, 3662844,575, 3826079,767), stdev = 186849,177
//  CI (99.9%): [2943354,925, 4382334,225] (assumes normal distribution)
//
//
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 2000000)
//
//# Run progress: 42,86% complete, ETA 00:06:58
//# Fork: 1 of 1
//# Warmup Iteration   1: 13529150,600 us/op
//# Warmup Iteration   2: <failure>
//
//java.lang.OutOfMemoryError: Java heap space
//	at java.base/java.util.HashMap.newNode(HashMap.java:1910)
//	at java.base/java.util.HashMap.putVal(HashMap.java:649)
//	at java.base/java.util.HashMap.putMapEntries(HashMap.java:523)
//	at java.base/java.util.HashMap.putAll(HashMap.java:792)
//	at org.itmo.lab3.Purchase.<init>(Purchase.java:30)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:129)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:143)
//	at org.itmo.lab3.FlowableBenchmark.setupPurchases(FlowableBenchmark.java:30)
//	at org.itmo.lab3.jmh_generated.FlowableBenchmark_benchmarkRun_jmhTest.benchmarkRun_AverageTime(FlowableBenchmark_benchmarkRun_jmhTest.java:184)
//	at java.base/java.lang.invoke.DirectMethodHandle$Holder.invokeSpecial(DirectMethodHandle$Holder)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x000001ec9f080800.invoke(LambdaForm$MH)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x000001ec9f080c00.invokeExact_MT(LambdaForm$MH)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(DirectMethodHandleAccessor.java:155)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
//	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:475)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:458)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
//	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
//	at java.base/java.lang.Thread.runWith(Thread.java:1588)
//	at java.base/java.lang.Thread.run(Thread.java:1575)
//
//
//
//
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 3000000)
//
//# Run progress: 57,14% complete, ETA 00:04:47
//# Fork: 1 of 1
//# Warmup Iteration   1: 13833997,600 us/op
//# Warmup Iteration   2: <failure>
//
//java.lang.OutOfMemoryError: Java heap space
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:110)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:143)
//	at org.itmo.lab3.FlowableBenchmark.setupPurchases(FlowableBenchmark.java:30)
//	at org.itmo.lab3.jmh_generated.FlowableBenchmark_benchmarkRun_jmhTest.benchmarkRun_AverageTime(FlowableBenchmark_benchmarkRun_jmhTest.java:184)
//	at java.base/java.lang.invoke.DirectMethodHandle$Holder.invokeSpecial(DirectMethodHandle$Holder)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x0000016899080800.invoke(LambdaForm$MH)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x0000016899080c00.invokeExact_MT(LambdaForm$MH)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(DirectMethodHandleAccessor.java:155)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
//	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:475)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:458)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
//	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
//	at java.base/java.lang.Thread.runWith(Thread.java:1588)
//	at java.base/java.lang.Thread.run(Thread.java:1575)
//
//
//
//
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 4000000)
//
//# Run progress: 71,43% complete, ETA 00:03:00
//# Fork: 1 of 1
//# Warmup Iteration   1: <failure>
//
//java.lang.OutOfMemoryError: Java heap space
//	at java.base/java.util.HashMap.newNode(HashMap.java:1910)
//	at java.base/java.util.HashMap.putVal(HashMap.java:638)
//	at java.base/java.util.HashMap.put(HashMap.java:619)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:124)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:143)
//	at org.itmo.lab3.FlowableBenchmark.setupPurchases(FlowableBenchmark.java:30)
//	at org.itmo.lab3.jmh_generated.FlowableBenchmark_benchmarkRun_jmhTest.benchmarkRun_AverageTime(FlowableBenchmark_benchmarkRun_jmhTest.java:184)
//	at java.base/java.lang.invoke.DirectMethodHandle$Holder.invokeSpecial(DirectMethodHandle$Holder)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x0000021b02080800.invoke(LambdaForm$MH)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x0000021b02080c00.invokeExact_MT(LambdaForm$MH)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(DirectMethodHandleAccessor.java:155)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
//	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:475)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:458)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
//	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
//	at java.base/java.lang.Thread.runWith(Thread.java:1588)
//	at java.base/java.lang.Thread.run(Thread.java:1575)
//
//
//
//
//# JMH version: 1.36
//# VM version: JDK 23, OpenJDK 64-Bit Server VM, 23+37-2369
//# VM invoker: C:\Users\aleks\.jdks\openjdk-23\bin\java.exe
//# VM options: -Xms2G -Xmx2G
//# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
//# Warmup: 3 iterations, 10 s each
//# Measurement: 5 iterations, 10 s each
//# Timeout: 10 min per iteration
//# Threads: 1 thread, will synchronize iterations
//# Benchmark mode: Average time, time/op
//# Benchmark: org.itmo.lab3.FlowableBenchmark.benchmarkRun
//# Parameters: (n = 5000000)
//
//# Run progress: 85,71% complete, ETA 00:01:23
//# Fork: 1 of 1
//# Warmup Iteration   1: <failure>
//
//java.lang.OutOfMemoryError: Java heap space
//	at java.base/java.util.HashMap.newNode(HashMap.java:1910)
//	at java.base/java.util.HashMap.putVal(HashMap.java:638)
//	at java.base/java.util.HashMap.putMapEntries(HashMap.java:523)
//	at java.base/java.util.HashMap.putAll(HashMap.java:792)
//	at org.itmo.lab3.Purchase.<init>(Purchase.java:30)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:129)
//	at org.itmo.lab3.Purchase$PurchaseGenerator.generatePurchases(Purchase.java:143)
//	at org.itmo.lab3.FlowableBenchmark.setupPurchases(FlowableBenchmark.java:30)
//	at org.itmo.lab3.jmh_generated.FlowableBenchmark_benchmarkRun_jmhTest.benchmarkRun_AverageTime(FlowableBenchmark_benchmarkRun_jmhTest.java:184)
//	at java.base/java.lang.invoke.DirectMethodHandle$Holder.invokeSpecial(DirectMethodHandle$Holder)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x000001d7b6080800.invoke(LambdaForm$MH)
//	at java.base/java.lang.invoke.LambdaForm$MH/0x000001d7b6080c00.invokeExact_MT(LambdaForm$MH)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invokeImpl(DirectMethodHandleAccessor.java:155)
//	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
//	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:475)
//	at org.openjdk.jmh.runner.BenchmarkHandler$BenchmarkTask.call(BenchmarkHandler.java:458)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:572)
//	at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:317)
//	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
//	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
//	at java.base/java.lang.Thread.runWith(Thread.java:1588)
//	at java.base/java.lang.Thread.run(Thread.java:1575)
//
//
//
//
//# Run complete. Total time: 00:09:11
//
//REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
//why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
//experiments, perform baseline and negative tests that provide experimental control, make sure
//the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
//Do not assume the numbers tell you what you want them to tell.
//
//NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
//extra caution when trusting the results, look into the generated code to check the benchmark still
//works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
//different JVMs are already problematic, the performance difference caused by different Blackhole
//modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.
//
//Benchmark                           (n)  Mode  Cnt        Score        Error  Units
//FlowableBenchmark.benchmarkRun    50000  avgt    5   170374,255 �   6521,147  us/op
//FlowableBenchmark.benchmarkRun   100000  avgt    5   335328,533 �  27037,166  us/op
//FlowableBenchmark.benchmarkRun  1000000  avgt    5  3662844,575 � 719489,650  us/op
//
//Process finished with exit code 0