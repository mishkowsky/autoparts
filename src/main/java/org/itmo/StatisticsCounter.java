package org.itmo;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StatisticsCounter {

    private final Map<Buyer, Long> totalSpendPerBuyer;
    private final Map<Product, Integer> totalSellCountPerProduct;
    private final Map<Product.Category, Integer> totalSellCountPerCategory;
    private final long totalProfit;

    public StatisticsCounter(List<Purchase> purchases) {
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

        Collector<Purchase, ?, Map<Product.Category, Integer>> totalSellCountPerCategoryCollector = Collectors.flatMapping(
                purchase -> purchase.getProducts().entrySet().stream(),
                Collectors.toMap(
                        m -> m.getKey().category(),
                        Map.Entry::getValue,
                        Integer::sum
                )
        );

        Collector<Purchase, ?, Long> totalProfitCollector = Collectors.summingLong(Purchase::getTotalPrice);

        totalSpendPerBuyer = purchases.stream().collect(totalSpendPerBuyerCollector);
        totalSellCountPerProduct = purchases.stream().collect(totalSellCountPerProductCollector);
        totalSellCountPerCategory = purchases.stream().collect(totalSellCountPerCategoryCollector);
        totalProfit = purchases.stream().collect(totalProfitCollector);
    }

    public Map<Buyer, Long> getTotalSpendPerBuyer() {
        return totalSpendPerBuyer;
    }

    public Map<Product, Integer> getTotalSellCountPerProduct() {
        return totalSellCountPerProduct;
    }

    public Map<Product.Category, Integer> getTotalSellCountPerCategory() {
        return totalSellCountPerCategory;
    }

    public long getTotalProfit() {
        return totalProfit;
    }
}
