package org.itmo;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.javatuples.Quartet;
import org.javatuples.Pair;

public class StatisticsCounter {

    public record Result(
            Map<Buyer, Long> totalSpendPerBuyer,
            Map<Product, Integer> totalSellCountPerProduct,
            Map<Product.Category, Integer> totalSellCountPerCategory,
            long totalProfit
    ) {
        public Pair<Buyer, Long> getBestBuyer() {
            Pair<Buyer, Long> result = new Pair<>(null, -1L);
            for (Map.Entry<Buyer, Long> entry : this.totalSpendPerBuyer.entrySet())
                if (entry.getValue() > result.getValue1())
                    result = new Pair<>(entry.getKey(), entry.getValue());
            return result;
        }

        public Pair<Product, Integer> getMostPopularProduct() {
            Pair<Product, Integer> result = new Pair<>(null, -1);
            for (Map.Entry<Product, Integer> entry : this.totalSellCountPerProduct.entrySet())
                if (entry.getValue() > result.getValue1())
                    result = new Pair<>(entry.getKey(), entry.getValue());
            return result;
        }

        public Pair<Product.Category, Integer> getMostPopularCategory() {
            Pair<Product.Category, Integer> result = new Pair<>(null, -1);
            for (Map.Entry<Product.Category, Integer> entry : this.totalSellCountPerCategory.entrySet())
                if (entry.getValue() > result.getValue1())
                    result = new Pair<>(entry.getKey(), entry.getValue());
            return result;
        }

        public int hashCode() {
            return (int) (this.totalSpendPerBuyer.hashCode() + this.totalSellCountPerProduct.hashCode() +
                    this.totalSellCountPerCategory.hashCode() + this.totalProfit);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Result result)) return false;

            return totalProfit == result.totalProfit
                    && totalSpendPerBuyer.equals(result.totalSpendPerBuyer)
                    && totalSellCountPerProduct.equals(result.totalSellCountPerProduct)
                    && totalSellCountPerCategory.equals(result.totalSellCountPerCategory);
        }
    }

    public static StatisticsCounter.Result countForLoop(List<Purchase> purchases) {

        Map<Buyer, Long> totalSpendPerBuyer = new HashMap<>();
        Map<Product, Integer> totalSoldPerProduct = new HashMap<>();
        Map<Product.Category, Integer> totalSoldPerCategory = new HashMap<>();
        long totalProfit = 0;

        for (Purchase purchase : purchases) {
            long pervTotalSpendPerBuyer = totalSpendPerBuyer.getOrDefault(purchase.getBuyer(), 0L);
            totalSpendPerBuyer.put(purchase.getBuyer(), pervTotalSpendPerBuyer + purchase.getTotalPrice());

            long totalPurchaseCost = 0;
            for (Product product : purchase.getProducts().keySet()) {
                int soldCount = purchase.getProducts().get(product);
                totalPurchaseCost += product.purchasePrice() * (long) soldCount;

                int prevTotalSoldPerProduct = totalSoldPerProduct.getOrDefault(product, 0);
                totalSoldPerProduct.put(product, prevTotalSoldPerProduct + soldCount);

                int prevTotalSoldPerCategory = totalSoldPerCategory.getOrDefault(product.category(), 0);
                totalSoldPerCategory.put(product.category(), prevTotalSoldPerCategory + soldCount);
            }
            totalProfit += purchase.getTotalPrice() - totalPurchaseCost;
        }
        return new StatisticsCounter.Result(totalSpendPerBuyer, totalSoldPerProduct, totalSoldPerCategory, totalProfit);
    }

    public static Result countStandardCollectors(List<Purchase> purchases) {

        Collector<Purchase, ?, Map<Buyer, Long>> totalSpendPerBuyerCollector =
                Collectors.toMap(
                        Purchase::getBuyer,
                        Purchase::getTotalPrice,
                        Long::sum);

        Collector<Purchase, ?, Map<Product, Integer>> totalSellCountPerProductCollector = Collectors.flatMapping(
                purchase -> purchase.getProducts().entrySet().stream(),
                Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                )
        );

        Collector<Purchase, ?, Map<Product.Category, Integer>>
                totalSellCountPerCategoryCollector = Collectors.flatMapping(
                purchase -> purchase.getProducts().entrySet().stream(),
                Collectors.toMap(
                        m -> m.getKey().category(),
                        Map.Entry::getValue,
                        Integer::sum
                )
        );

        Collector<Purchase, ?, Long> totalProfitCollector =
                Collectors.summingLong(
                        p -> p.getTotalPrice() - p.getProducts().entrySet().stream().mapToLong(
                                e -> (long) e.getKey().purchasePrice() * e.getValue()).sum());

        Map<Buyer, Long> totalSpendPerBuyer = purchases.stream().collect(totalSpendPerBuyerCollector);
        Map<Product, Integer> totalSellCountPerProduct = purchases.stream().collect(totalSellCountPerProductCollector);
        Map<Product.Category, Integer> totalSellCountPerCategory =
                purchases.stream().collect(totalSellCountPerCategoryCollector);
        long totalProfit = purchases.stream().collect(totalProfitCollector);

        return new StatisticsCounter.Result(
                totalSpendPerBuyer, totalSellCountPerProduct, totalSellCountPerCategory, totalProfit);
    }

    public static Result countStandardCollectorsParallel(List<Purchase> purchases) {

        Collector<Purchase, ?, ConcurrentMap<Buyer, Long>>
                totalSpendPerBuyerCollector =
                Collectors.toConcurrentMap(
                        Purchase::getBuyer,
                        Purchase::getTotalPrice,
                        Long::sum);

        Collector<Purchase, ?, ConcurrentMap<Product, Integer>>
                totalSellCountPerProductCollector =
                Collectors.flatMapping(
                        purchase -> purchase.getProducts().entrySet().stream(),
                        Collectors.toConcurrentMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                Integer::sum
                        )
                );

        Collector<Purchase, ?, ConcurrentMap<Product.Category, Integer>>
                totalSellCountPerCategoryCollector = Collectors.flatMapping(
                purchase -> purchase.getProducts().entrySet().stream(),
                Collectors.toConcurrentMap(
                        m -> m.getKey().category(),
                        Map.Entry::getValue,
                        Integer::sum
                )
        );

        Collector<Purchase, ?, Long> totalProfitCollector =
                Collectors.summingLong(
                        p -> p.getTotalPrice() - p.getProducts().entrySet().stream().mapToLong(
                                e -> (long) e.getKey().purchasePrice() * e.getValue()).sum());

        ConcurrentMap<Buyer, Long>
                totalSpendPerBuyer = purchases.parallelStream().collect(totalSpendPerBuyerCollector);
        ConcurrentMap<Product, Integer>
                totalSellCountPerProduct = purchases.parallelStream().collect(totalSellCountPerProductCollector);
        ConcurrentMap<Product.Category, Integer>
                totalSellCountPerCategory =
                purchases.parallelStream().collect(totalSellCountPerCategoryCollector);
        long totalProfit = purchases.parallelStream().collect(totalProfitCollector);

        return new StatisticsCounter.Result(totalSpendPerBuyer, totalSellCountPerProduct,
                totalSellCountPerCategory, totalProfit);
    }

    public static class MyRecursiveTask extends RecursiveTask<Result> {

        private final List<Purchase> purchases;

        public MyRecursiveTask(List<Purchase> purchases) {
            this.purchases = purchases;
        }

        @Override
        protected Result compute() {
            //if work is above threshold, break tasks up into smaller tasks
            if (this.purchases.size() > 16) {

                int subLen = this.purchases.size() / 2;

                MyRecursiveTask subtask1 = new MyRecursiveTask(this.purchases.subList(0, subLen));
                MyRecursiveTask subtask2 = new MyRecursiveTask(this.purchases.subList(subLen, this.purchases.size()));

                subtask1.fork();
                subtask2.fork();

                Result result1 = subtask1.join();
                Result result2 = subtask2.join();

                for (Map.Entry<Buyer, Long> e : result2.totalSpendPerBuyer.entrySet()) {
                    Long prevValue = result1.totalSpendPerBuyer.getOrDefault(e.getKey(), 0L);
                    result1.totalSpendPerBuyer.put(e.getKey(), prevValue + e.getValue());
                }

                for (Map.Entry<Product, Integer> e : result2.totalSellCountPerProduct.entrySet()) {
                    Integer prevValue = result1.totalSellCountPerProduct.getOrDefault(e.getKey(), 0);
                    result1.totalSellCountPerProduct.put(e.getKey(), prevValue + e.getValue());
                }

                for (Map.Entry<Product.Category, Integer> e : result2.totalSellCountPerCategory.entrySet()) {
                    Integer prevValue = result1.totalSellCountPerCategory.getOrDefault(e.getKey(), 0);
                    result1.totalSellCountPerCategory.put(e.getKey(), prevValue + e.getValue());
                }

                return new Result(result1.totalSpendPerBuyer,
                        result1.totalSellCountPerProduct, result1.totalSellCountPerCategory,
                        result1.totalProfit + result2.totalProfit);
            } else {
                return StatisticsCounter.countForLoop(purchases);
            }
        }
    }

    public static Result countForkJoin(List<Purchase> purchases) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        MyRecursiveTask myRecursiveAction = new MyRecursiveTask(purchases);
        return forkJoinPool.invoke(myRecursiveAction);
    }

    public static Result countCustomCollector(List<Purchase> purchases) {
        MyCollector c = new MyCollector();
        Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>
                q = purchases.stream().collect(c);
        return new Result(q.getValue0(), q.getValue1(), q.getValue2(), q.getValue3().getValue());
    }

    public static class MyCollector implements Collector<
            Purchase,
            Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>,
            Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>> {

        @Override
        public Supplier<
                Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>>
        supplier() {
            return () -> new Quartet<>(
                    new HashMap<Buyer, Long>(),
                    new HashMap<Product, Integer>(),
                    new HashMap<Product.Category, Integer>(),
                    new Utils.LongWrapper(0L));
        }

        @Override
        public BiConsumer<
                Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>,
                Purchase>
        accumulator() {
            return (quartet, purchase) -> {
                Map<Buyer, Long> sumPerBuyer = quartet.getValue0();
                long prevSumPerBuyer = sumPerBuyer.getOrDefault(purchase.getBuyer(), 0L);
                sumPerBuyer.put(purchase.getBuyer(), prevSumPerBuyer + purchase.getTotalPrice());

                Map<Product, Integer> countPerProduct = quartet.getValue1();
                Map<Product.Category, Integer> counterPerCategory = quartet.getValue2();

                for (Map.Entry<Product, Integer> e : purchase.getProducts().entrySet()) {
                    Product product = e.getKey();
                    Product.Category category = product.category();

                    int sellCount = e.getValue();
                    int prevProductCount = countPerProduct.getOrDefault(product, 0);
                    countPerProduct.put(product, prevProductCount + sellCount);

                    int prevCategoryCount = counterPerCategory.getOrDefault(category, 0);
                    counterPerCategory.put(category, prevCategoryCount + sellCount);
                }
                long sum = quartet.getValue3().getValue();
                sum += purchase.getTotalPrice() - purchase.getProducts().entrySet().stream()
                        .mapToLong(e -> (long) e.getKey().purchasePrice() * e.getValue()).sum();
                quartet.getValue3().setValue(sum);
            };
        }

        @Override
        public BinaryOperator<
                Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>>
        combiner() {
            return (q1, q2) -> {
                q1.getValue0().keySet().stream().map(
                        k1 -> q2.getValue0().merge(k1, q1.getValue0().get(k1), Long::sum));
                q1.getValue1().keySet().stream().map(
                        k1 -> q2.getValue1().merge(k1, q1.getValue1().get(k1), Integer::sum));
                q1.getValue2().keySet().stream().map(
                        k1 -> q2.getValue2().merge(k1, q1.getValue2().get(k1), Integer::sum));
                q2.getValue3().setValue(q1.getValue3().getValue() + q2.getValue3().getValue());
                return q2;
            };
        }

        @Override
        public Function<
                Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>,
                Quartet<Map<Buyer, Long>, Map<Product, Integer>, Map<Product.Category, Integer>, Utils.LongWrapper>>
        finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }

    public static long countTotalProfitForLoop(List<Purchase> purchases) {
        long totalProfit = 0;
        for (Purchase purchase : purchases)
            totalProfit += purchase.getTotalPrice();
        return totalProfit;
    }

    public static long countTotalProfitStandardCollector(List<Purchase> purchases) {
        Collector<Purchase, ?, Long> totalProfitCollector = Collectors.summingLong(Purchase::getTotalPrice);
        return purchases.stream().collect(totalProfitCollector);
    }

    public static long countTotalProfitCustomCollector(List<Purchase> purchases) {
        MyTotalProfitCollector myTotalProfitCollector = new MyTotalProfitCollector();
        return purchases.stream().collect(myTotalProfitCollector).getValue();
    }

    public static class MyTotalProfitCollector implements
            Collector<Purchase, Utils.LongWrapper, Utils.LongWrapper> {
        @Override
        public Supplier<Utils.LongWrapper> supplier() {
            return () -> new Utils.LongWrapper(0L);
        }

        @Override
        public BiConsumer<Utils.LongWrapper, Purchase> accumulator() {
            return (aLong, purchase) -> aLong.setValue(aLong.getValue() + purchase.getTotalPrice());
        }

        @Override
        public BinaryOperator<Utils.LongWrapper> combiner() {
            return (l1, l2) -> {
                l1.setValue(l1.getValue() + l2.getValue());
                return l1;
            };
        }

        @Override
        public Function<Utils.LongWrapper, Utils.LongWrapper> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of();
        }
    }
}
