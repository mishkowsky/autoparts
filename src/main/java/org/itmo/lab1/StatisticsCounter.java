package org.itmo.lab1;

import java.util.*;
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
    }

    public static Result countForLoop(List<Purchase> purchases) {

        Map<Buyer, Long> totalSpendPerBuyer = new HashMap<>();
        Map<Product, Integer> totalSoldPerProduct = new HashMap<>();
        Map<Product.Category, Integer> totalSoldPerCategory = new HashMap<>();
        long totalProfit = 0;

        for (Purchase purchase : purchases) {
            totalSpendPerBuyer.put(
                    purchase.getBuyer(),
                    totalSpendPerBuyer.getOrDefault(purchase.getBuyer(), 0L) + purchase.getTotalPrice());

            for (Product product : purchase.getProducts().keySet()) {
                totalSoldPerProduct.put(
                        product, totalSoldPerProduct.getOrDefault(product, 0) + 1);
                totalSoldPerCategory.put(
                        product.category(), totalSoldPerCategory.getOrDefault(product.category(), 0) + 1);
            }

            totalProfit += purchase.getTotalPrice();
        }

        return new Result(totalSpendPerBuyer, totalSoldPerProduct, totalSoldPerCategory, totalProfit);
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
                        e -> 1,
                        Integer::sum
                )
        );

        Collector<Purchase, ?, Map<Product.Category, Integer>>
                totalSellCountPerCategoryCollector = Collectors.flatMapping(
                purchase -> purchase.getProducts().entrySet().stream(),
                Collectors.toMap(
                        m -> m.getKey().category(),
                        e -> 1,
                        Integer::sum
                )
        );

        Collector<Purchase, ?, Long> totalProfitCollector = Collectors.summingLong(Purchase::getTotalPrice);

        Map<Buyer, Long> totalSpendPerBuyer = purchases.stream().collect(totalSpendPerBuyerCollector);
        Map<Product, Integer> totalSellCountPerProduct = purchases.stream().collect(totalSellCountPerProductCollector);
        Map<Product.Category, Integer> totalSellCountPerCategory =
                purchases.stream().collect(totalSellCountPerCategoryCollector);
        long totalProfit = purchases.stream().collect(totalProfitCollector);

        return new Result(totalSpendPerBuyer, totalSellCountPerProduct, totalSellCountPerCategory, totalProfit);
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
                sumPerBuyer.put(purchase.getBuyer(),
                        sumPerBuyer.getOrDefault(purchase.getBuyer(), 0L) + purchase.getTotalPrice());

                Map<Product, Integer> counterPerProduct = quartet.getValue1();
                Map<Product.Category, Integer> counterPerCategory = quartet.getValue2();

                for (Product product : purchase.getProducts().keySet()) {
                    counterPerProduct.put(product, counterPerProduct.getOrDefault(product, 0) + 1);
                    Product.Category category = product.category();
                    counterPerCategory.put(category, counterPerCategory.getOrDefault(category, 0) + 1);
                }
                long sum = quartet.getValue3().getValue();
                sum += purchase.getTotalPrice();
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