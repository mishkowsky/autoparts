package org.itmo;

import org.itmo.lab2.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Map.entry;

public class StatisticsCounterTest {

    private final StatisticsCounter.Result result;
    private final List<Purchase> purchases;

    public StatisticsCounterTest() {
        Product p1 = new Product(100, 50, "testProduct1Name",
                Product.Category.ENGINE, 15, 0);
        Product p2 = new Product(500, 100, "testProduct2Name",
                Product.Category.CAR_BODY, 0, 0);
        Product p3 = new Product(100, 50, "testProduct3Name",
                Product.Category.GLASS, 0, 0);

        Buyer b1 = new Buyer("name1", "+71234567890", 0, 0);
        Buyer b2 = new Buyer("name2", "+71234567891", 15, 0);
        Calendar startDate = Calendar.getInstance();
        Calendar endPeriodDate = Calendar.getInstance();
        endPeriodDate.add(Calendar.MONTH, 1);
        Date d = Utils.between(startDate.getTime(), endPeriodDate.getTime());
        Purchase pu1 = new Purchase(
                d,
                Purchase.PaymentMethod.CASH,
                p1.sellingPrice() * 2 * (100 - p1.discount()) / 100 + p2.sellingPrice(),
                "address",
                Map.ofEntries(
                        entry(p1, 2),
                        entry(p2, 1)
                ), b1, 0);

        Purchase pu2 = new Purchase(
                d,
                Purchase.PaymentMethod.CASH,
                p2.sellingPrice() * 2 + p3.sellingPrice() * 3,
                "address",
                Map.ofEntries(
                        entry(p2, 2),
                        entry(p3, 3)
                ), b1, 0);

        Purchase pu3 = new Purchase(
                d,
                Purchase.PaymentMethod.CARD,
                (p2.sellingPrice() * 5 + p3.sellingPrice()) * (100 - b2.personalDiscount()) / 100,
                "address",
                Map.ofEntries(
                        entry(p2, 5),
                        entry(p3, 1)
                ), b2, 0);

        this.purchases = Arrays.asList(pu1, pu2, pu3);

        this.result = new StatisticsCounter.Result(
                Map.ofEntries(
                        entry(b1, 1970L),
                        entry(b2, 2210L)
                ),
                Map.ofEntries(
                        entry(p1, 2),
                        entry(p2, 8),
                        entry(p3, 4)
                ),
                Map.ofEntries(
                        entry(Product.Category.ENGINE, 2),
                        entry(Product.Category.CAR_BODY, 8),
                        entry(Product.Category.GLASS, 4)
                ),
                3080L
        );
    }

    @Test
    void countForLoopTest() {
        StatisticsCounter.Result result = StatisticsCounter.countForLoop(this.purchases);
        Assertions.assertEquals(this.result, result);
    }

    @Test
    void countStandardCollectorsTest() {
        StatisticsCounter.Result result = StatisticsCounter.countStandardCollectors(this.purchases);
        Assertions.assertEquals(this.result, result);
    }

    @Test
    void countStandardCollectorsParallelTest() {
        StatisticsCounter.Result result = StatisticsCounter.countStandardCollectorsParallel(this.purchases);
        Assertions.assertEquals(this.result, result);
    }

    @Test
    void countForkJoinTest() {
        StatisticsCounter.Result result = StatisticsCounter.countForkJoin(this.purchases);
        Assertions.assertEquals(this.result, result);
    }

    @Test
    void countCustomCollectorTest() {
        StatisticsCounter.Result result = StatisticsCounter.countCustomCollector(this.purchases);
        Assertions.assertEquals(this.result, result);
    }
}
