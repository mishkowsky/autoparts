package org.itmo.lab3;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observables.GroupedObservable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class StatisticsCounterRx {

    public static class Result {
        ConcurrentMap<Buyer, Long> totalSpendPerBuyer;
        ConcurrentMap<Product, Integer> totalSellCountPerProduct;
        ConcurrentMap<Product.Category, Integer> totalSellCountPerCategory;
        long totalProfit;

        public Result() {
            this.totalSpendPerBuyer = new ConcurrentHashMap<>();
            this.totalSellCountPerCategory = new ConcurrentHashMap<>();
            this.totalSellCountPerProduct = new ConcurrentHashMap<>();
            this.totalProfit = 0L;
        }

        public Result(ConcurrentMap<Buyer, Long> totalSpendPerBuyer,
                      ConcurrentMap<Product, Integer> totalSellCountPerProduct,
                      ConcurrentMap<Product.Category, Integer> totalSellCountPerCategory,
                      long totalProfit) {
            this.totalSpendPerBuyer = totalSpendPerBuyer;
            this.totalSellCountPerCategory = totalSellCountPerCategory;
            this.totalSellCountPerProduct = totalSellCountPerProduct;
            this.totalProfit = totalProfit;
        }

        public Pair<Buyer, Long> getBestBuyer() {
            Pair<Buyer, Long> result = new Pair<>(null, -1L);
            for (ConcurrentMap.Entry<Buyer, Long> entry : this.totalSpendPerBuyer.entrySet())
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

        public void merge(Result other) {
            for (var entry : other.totalSpendPerBuyer.entrySet()) {
                if (this.totalSpendPerBuyer.containsKey(entry.getKey())) {
                    this.totalSpendPerBuyer.put(entry.getKey(), this.totalSpendPerBuyer.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSpendPerBuyer.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : other.totalSellCountPerCategory.entrySet()) {
                if (this.totalSellCountPerCategory.containsKey(entry.getKey())) {
                    this.totalSellCountPerCategory.put(entry.getKey(), this.totalSellCountPerCategory.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSellCountPerCategory.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : other.totalSellCountPerProduct.entrySet()) {
                if (this.totalSellCountPerProduct.containsKey(entry.getKey())) {
                    this.totalSellCountPerProduct.put(entry.getKey(), this.totalSellCountPerProduct.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSellCountPerProduct.put(entry.getKey(), entry.getValue());
                }
            }

            this.totalProfit += other.totalProfit;
        }

        public void mergeWithNoConcurrent(NonConcurrentResult other) {
            for (var entry : other.totalSpendPerBuyer.entrySet()) {
                if (this.totalSpendPerBuyer.containsKey(entry.getKey())) {
                    this.totalSpendPerBuyer.put(entry.getKey(), this.totalSpendPerBuyer.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSpendPerBuyer.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : other.totalSellCountPerCategory.entrySet()) {
                if (this.totalSellCountPerCategory.containsKey(entry.getKey())) {
                    this.totalSellCountPerCategory.put(entry.getKey(), this.totalSellCountPerCategory.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSellCountPerCategory.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : other.totalSellCountPerProduct.entrySet()) {
                if (this.totalSellCountPerProduct.containsKey(entry.getKey())) {
                    this.totalSellCountPerProduct.put(entry.getKey(), this.totalSellCountPerProduct.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSellCountPerProduct.put(entry.getKey(), entry.getValue());
                }
            }

            this.totalProfit += other.totalProfit;
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


    public static class NonConcurrentResult {
        Map<Buyer, Long> totalSpendPerBuyer;
        Map<Product, Integer> totalSellCountPerProduct;
        Map<Product.Category, Integer> totalSellCountPerCategory;
        long totalProfit;

        public NonConcurrentResult() {
            this.totalSpendPerBuyer = new HashMap<>();
            this.totalSellCountPerCategory = new HashMap<>();
            this.totalSellCountPerProduct = new HashMap<>();
            this.totalProfit = 0L;
        }

        public NonConcurrentResult(Map<Buyer, Long> totalSpendPerBuyer,

                                   Map<Product, Integer> totalSellCountPerProduct,
                                   Map<Product.Category, Integer> totalSellCountPerCategory,
                                   long totalProfit) {
            this.totalSpendPerBuyer = totalSpendPerBuyer;
            this.totalSellCountPerCategory = totalSellCountPerCategory;
            this.totalSellCountPerProduct = totalSellCountPerProduct;
            this.totalProfit = totalProfit;
        }

        public void merge(NonConcurrentResult other) {
            for (var entry : other.totalSpendPerBuyer.entrySet()) {
                if (this.totalSpendPerBuyer.containsKey(entry.getKey())) {
                    this.totalSpendPerBuyer.put(entry.getKey(), this.totalSpendPerBuyer.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSpendPerBuyer.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : other.totalSellCountPerCategory.entrySet()) {
                if (this.totalSellCountPerCategory.containsKey(entry.getKey())) {
                    this.totalSellCountPerCategory.put(entry.getKey(), this.totalSellCountPerCategory.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSellCountPerCategory.put(entry.getKey(), entry.getValue());
                }
            }
            for (var entry : other.totalSellCountPerProduct.entrySet()) {
                if (this.totalSellCountPerProduct.containsKey(entry.getKey())) {
                    this.totalSellCountPerProduct.put(entry.getKey(), this.totalSellCountPerProduct.get(entry.getKey()) + entry.getValue());
                } else {
                    this.totalSellCountPerProduct.put(entry.getKey(), entry.getValue());
                }
            }
            this.totalProfit += other.totalProfit;
        }

        public Pair<Buyer, Long> getBestBuyer() {
            Pair<Buyer, Long> result = new Pair<>(null, -1L);
            for (Map.Entry<Buyer, Long> entry : this.totalSpendPerBuyer.entrySet())
                if (entry.getValue() > result.getValue1())
                    result = new Pair<>(entry.getKey(), entry.getValue());
            return result;
        }
    }

    static class CallableLongAction implements Callable<Integer> {

        private final Purchase purchase;
        private final Result result;

        public CallableLongAction(Purchase purchase, Result result) {
            this.purchase = purchase;
            this.result = result;
        }

        @Override
        public Integer call() {
            long pervTotalSpendPerBuyer = result.totalSpendPerBuyer.getOrDefault(purchase.getBuyer(), 0L);
            result.totalSpendPerBuyer.put(purchase.getBuyer(), pervTotalSpendPerBuyer + purchase.getTotalPrice());
            long totalPurchaseCost = 0;
            for (Product product : purchase.getProducts().keySet()) {
                int soldCount = purchase.getProducts().get(product);
                totalPurchaseCost += product.purchasePrice() * (long) soldCount;

                int prevTotalSoldPerProduct = result.totalSellCountPerProduct.getOrDefault(product, 0);
                result.totalSellCountPerProduct.put(product, prevTotalSoldPerProduct + soldCount);

                int prevTotalSoldPerCategory = result.totalSellCountPerCategory.getOrDefault(product.category(), 0);
                result.totalSellCountPerCategory.put(product.category(), prevTotalSoldPerCategory + soldCount);
            }
            result.totalProfit += purchase.getTotalPrice() - totalPurchaseCost;
            return 0;
        }
    }

    private static Observable<Integer> processPurchase(Purchase purchase, Result res) {
        return Observable.fromCallable(new CallableLongAction(purchase, res));
    }

    public static Observable<Integer> processPurchases(List<Purchase> purchases, Result res) {
        return Observable.
                fromIterable(purchases).
                flatMap(p -> processPurchase(p, res).subscribeOn(Schedulers.computation()));
    }

    public static Result countRxWithCallableAction(List<Purchase> purchases) {
        Result result = new Result();
        var res = StatisticsCounterRx.processPurchases(purchases, result);
        res.blockingSubscribe();
        return result;
    }

    public static Result countRxSimple(List<Purchase> purchases) {
        Result result = new Result();
        Observable.
                fromIterable(purchases).
                doOnNext(purchase -> {
                    long pervTotalSpendPerBuyer = result.totalSpendPerBuyer.getOrDefault(purchase.getBuyer(), 0L);
                    result.totalSpendPerBuyer.put(purchase.getBuyer(), pervTotalSpendPerBuyer + purchase.getTotalPrice());
                    long totalPurchaseCost = 0;
                    for (Product product : purchase.getProducts().keySet()) {
                        int soldCount = purchase.getProducts().get(product);
                        totalPurchaseCost += product.purchasePrice() * (long) soldCount;

                        int prevTotalSoldPerProduct = result.totalSellCountPerProduct.getOrDefault(product, 0);
                        result.totalSellCountPerProduct.put(product, prevTotalSoldPerProduct + soldCount);

                        int prevTotalSoldPerCategory = result.totalSellCountPerCategory.getOrDefault(product.category(), 0);
                        result.totalSellCountPerCategory.put(product.category(), prevTotalSoldPerCategory + soldCount);
                    }
                    result.totalProfit += purchase.getTotalPrice() - totalPurchaseCost;
                }).
                subscribeOn(Schedulers.computation()).
                blockingSubscribe();
        return result;
    }

    static class CallableLongActionNonConcurrent implements Callable<Integer> {

        private final Purchase purchase;
        private final NonConcurrentResult result;

        public CallableLongActionNonConcurrent(Purchase purchase, NonConcurrentResult result) {
            this.purchase = purchase;
            this.result = result;
        }

        @Override
        public Integer call() {
            long pervTotalSpendPerBuyer = result.totalSpendPerBuyer.getOrDefault(purchase.getBuyer(), 0L);
            result.totalSpendPerBuyer.put(purchase.getBuyer(), pervTotalSpendPerBuyer + purchase.getTotalPrice());
            long totalPurchaseCost = 0;
            for (Product product : purchase.getProducts().keySet()) {
                int soldCount = purchase.getProducts().get(product);
                totalPurchaseCost += product.purchasePrice() * (long) soldCount;

                int prevTotalSoldPerProduct = result.totalSellCountPerProduct.getOrDefault(product, 0);
                result.totalSellCountPerProduct.put(product, prevTotalSoldPerProduct + soldCount);

                int prevTotalSoldPerCategory = result.totalSellCountPerCategory.getOrDefault(product.category(), 0);
                result.totalSellCountPerCategory.put(product.category(), prevTotalSoldPerCategory + soldCount);
            }
            result.totalProfit += purchase.getTotalPrice() - totalPurchaseCost;
            return 0;
        }
    }

    private static Observable<Integer> processNonConcurrentPurchase(Purchase purchase, NonConcurrentResult res) {
        return Observable.fromCallable(new CallableLongActionNonConcurrent(purchase, res));
    }

    public static Observable<Integer> processNonConcurrentPurchases(List<Purchase> purchases, NonConcurrentResult res) {
        return Observable.
                fromIterable(purchases).
                flatMap(p -> processNonConcurrentPurchase(p, res).subscribeOn(Schedulers.computation()));
    }

    public static NonConcurrentResult countNonConcurrentRxWithCallable(List<Purchase> purchases) {
        Map<Buyer, Long> totalSpendPerBuyer = new ConcurrentHashMap<>();
        Map<Product, Integer> totalSellCountPerProduct = new ConcurrentHashMap<>();
        Map<Product.Category, Integer> totalSellCountPerCategory = new ConcurrentHashMap<>();
        NonConcurrentResult result = new NonConcurrentResult(totalSpendPerBuyer, totalSellCountPerProduct, totalSellCountPerCategory, 0L);
        var res = StatisticsCounterRx.processNonConcurrentPurchases(purchases, result);
        res.blockingSubscribe();
        return result;
    }

    public static Result countRxNestedObservablesWithConcurrentMap(List<Purchase> purchases) {

        ConcurrentMap<Buyer, Long> totalSpendPerBuyer = new ConcurrentHashMap<>();
        ConcurrentMap<Product, Integer> totalSellCountPerProduct = new ConcurrentHashMap<>();
        ConcurrentMap<Product.Category, Integer> totalSellCountPerCategory = new ConcurrentHashMap<>();
        AtomicLong totalProfit = new AtomicLong();

        Observable.
                fromIterable(purchases).
                flatMap(p -> Observable.
                        just(p).
                        subscribeOn(Schedulers.computation()).
                        doOnNext((p_) -> {
                            AtomicLong totalPurchasePrice = new AtomicLong();
                            Observable.
                                    fromIterable(p_.getProducts().entrySet()).
                                    subscribeOn(Schedulers.computation()).
                                    doOnNext((e) -> {
                                        totalSellCountPerProduct.put(e.getKey(), totalSellCountPerProduct.getOrDefault(e.getKey(), 0) + e.getValue());
//                                        System.out.println(Thread.currentThread().getName());
                                    }).
                                    subscribe();
                            Observable.
                                    fromIterable(p_.getProducts().entrySet()).
                                    subscribeOn(Schedulers.computation()).
                                    doOnNext((e) -> {
                                        totalSellCountPerCategory.put(e.getKey().category(), totalSellCountPerCategory.getOrDefault(e.getKey().category(), 0) + e.getValue());
//                                        System.out.println(Thread.currentThread().getName());
                                    }).
                                    subscribe();

                            Observable.
                                    fromIterable(p_.getProducts().entrySet()).
                                    subscribeOn(Schedulers.computation()).
                                    doOnNext((e) -> {
                                        totalPurchasePrice.addAndGet((long) e.getKey().purchasePrice() * e.getValue());
//                                        System.out.println(Thread.currentThread().getName());
                                    }).
                                    subscribe();

                            totalSpendPerBuyer.put(p_.getBuyer(), totalSpendPerBuyer.getOrDefault(p_.getBuyer(), 0L) + p_.getTotalPrice());

                            totalProfit.addAndGet(p_.getTotalPrice() - totalPurchasePrice.get());

                        })).
                blockingSubscribe();
        return new Result(totalSpendPerBuyer, totalSellCountPerProduct, totalSellCountPerCategory, totalProfit.get());
    }

    public static NonConcurrentResult countRxNestedObservableWithMap(List<Purchase> purchases) {

        Map<Buyer, Long> totalSpendPerBuyer = new ConcurrentHashMap<>();
        Map<Product, Integer> totalSellCountPerProduct = new ConcurrentHashMap<>();
        Map<Product.Category, Integer> totalSellCountPerCategory = new ConcurrentHashMap<>();
        AtomicLong totalProfit = new AtomicLong();

        Observable.
                fromIterable(purchases).
                flatMap(p -> Observable.
                        just(p).
                        subscribeOn(Schedulers.computation()).
                        doOnNext((p_) -> {
                            AtomicLong totalPurchasePrice = new AtomicLong();
                            Observable.
                                    fromIterable(p_.getProducts().entrySet()).
                                    subscribeOn(Schedulers.computation()).
                                    doOnNext((e) -> {
                                        totalSellCountPerProduct.put(e.getKey(), totalSellCountPerProduct.getOrDefault(e.getKey(), 0) + e.getValue());
//                                        System.out.println(Thread.currentThread().getName());
                                    }).
                                    subscribe();
                            Observable.
                                    fromIterable(p_.getProducts().entrySet()).
                                    subscribeOn(Schedulers.computation()).
                                    doOnNext((e) -> {
                                        totalSellCountPerCategory.put(e.getKey().category(), totalSellCountPerCategory.getOrDefault(e.getKey().category(), 0) + e.getValue());
//                                        System.out.println(Thread.currentThread().getName());
                                    }).
                                    subscribe();

                            Observable.
                                    fromIterable(p_.getProducts().entrySet()).
                                    subscribeOn(Schedulers.computation()).
                                    doOnNext((e) -> {
                                        totalPurchasePrice.addAndGet((long) e.getKey().purchasePrice() * e.getValue());
//                                        System.out.println(Thread.currentThread().getName());
                                    }).
                                    subscribe();

                            totalSpendPerBuyer.put(p_.getBuyer(), totalSpendPerBuyer.getOrDefault(p_.getBuyer(), 0L) + p_.getTotalPrice());

                            totalProfit.addAndGet(p_.getTotalPrice() - totalPurchasePrice.get());

                        })).
                blockingSubscribe();
        return new NonConcurrentResult(totalSpendPerBuyer, totalSellCountPerProduct, totalSellCountPerCategory, totalProfit.get());
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

        return new Result(totalSpendPerBuyer, totalSellCountPerProduct,
                totalSellCountPerCategory, totalProfit);
    }

    public static NonConcurrentResult countStandardCollectors(List<Purchase> purchases) {

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

        return new NonConcurrentResult(
                totalSpendPerBuyer, totalSellCountPerProduct, totalSellCountPerCategory, totalProfit);
    }

    public static Result countRxWithParallelCollectors(List<Purchase> purchases) {
        var result = new Result();
        Observable.fromIterable(purchases).
                flatMap(
                        p -> Observable.
                                just(p).
                                subscribeOn(Schedulers.computation()).
                                map((p_) -> countStandardCollectorsParallel(List.of(p_)))
                ).
                doOnNext(result::merge).
                blockingSubscribe();
        return result;
    }

    public static Result countRxWithCollectors(List<Purchase> purchases) {
        var result = new Result();
        Observable.fromIterable(purchases).
                flatMap(
                        p -> Observable.
                                just(p).
                                subscribeOn(Schedulers.computation()).
                                map((p_) -> countStandardCollectors(List.of(p_)))
                ).
                doOnNext(result::mergeWithNoConcurrent).
                blockingSubscribe();
        return result;
    }

    public static NonConcurrentResult countRxGroupByCategory(List<Purchase> purchases) {
        var result = new NonConcurrentResult();
        Observable.fromIterable(purchases).
                flatMap(purchase -> Observable.
                        fromIterable(purchase.getProducts().entrySet()).
                        map(entry -> new Pair<>(entry, purchase.getBuyer()))
                ).
                groupBy(pair -> pair.getValue0().getKey().category()).
                flatMapSingle(
                        (GroupedObservable<Product.Category, Pair<Map.Entry<Product, Integer>, Buyer>> groupObservable) ->
                                groupObservable.
                                        observeOn(Schedulers.computation()).
                                        collectInto(new NonConcurrentResult(), (nonConcurrentResult, c) -> {
                                            Product product = c.getValue0().getKey();
                                            int productCount = c.getValue0().getValue();
                                            Buyer buyer = c.getValue1();
                                            int productsSellPrice = (product.sellingPrice() * productCount * (100 - product.discount() - buyer.personalDiscount())) / 100;
                                            int profitPerProducts = productsSellPrice - product.purchasePrice() * productCount;
                                            nonConcurrentResult.totalSellCountPerProduct.put(
                                                    product, nonConcurrentResult.totalSellCountPerProduct.
                                                            getOrDefault(product, 0) + productCount);
                                            nonConcurrentResult.totalSpendPerBuyer.put(
                                                    buyer, nonConcurrentResult.totalSpendPerBuyer.
                                                            getOrDefault(buyer, 0L) + productsSellPrice);
                                            nonConcurrentResult.totalSellCountPerCategory.put(
                                                    groupObservable.getKey(), nonConcurrentResult.totalSellCountPerCategory.
                                                            getOrDefault(groupObservable.getKey(), 0) + productCount);
                                            nonConcurrentResult.totalProfit += profitPerProducts;
                                        })
                ).doOnNext(result::merge).blockingSubscribe();
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        long delay = 5;
        int n = 10;
        List<Purchase> purchases = Purchase.PurchaseGenerator.generatePurchases(n, delay,
                Product.ProductGenerator.generateProducts(delay),
                Buyer.BuyersGenerator.generateBuyers(100, delay));

        StatisticsCounter.Result expected = StatisticsCounter.countForLoop(purchases);

        System.out.println(expected.getBestBuyer());
        System.out.println(countRxSimple(purchases).getBestBuyer());
        System.out.println(countRxWithCallableAction(purchases).getBestBuyer());
        System.out.println(countNonConcurrentRxWithCallable(purchases).getBestBuyer());
        System.out.println(countRxWithCollectors(purchases).getBestBuyer());
        System.out.println(countRxWithParallelCollectors(purchases).getBestBuyer());

        System.out.println(countRxGroupByCategory(purchases).getBestBuyer());

        System.out.println(countRxNestedObservablesWithConcurrentMap(purchases).getBestBuyer());
        System.out.println(countRxNestedObservableWithMap(purchases).getBestBuyer());

    }
}
