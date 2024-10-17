package org.itmo;

import org.javatuples.Pair;

import java.util.List;

public class Lab1 {

    static int EQUALS_STRING_LEN = 93;

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Counting complex statistics");
        System.out.println();
        measureTimeComplexStats(5000);
        measureTimeComplexStats(50000);
        measureTimeComplexStats(250000);
        //
        // Counting complex statistics
        //
        // =============================================================================================
        // For Loop.			N: 5000; Elapsed time: 37550500 ns;
        //						Best buyer: Артамонов Михаил Маркович (225032051.36 rub);
        //						Most popular product: Прочее: блок цилиндров (380);
        //						Most popular category: ENGINE (12689);
        //						Total profit: 915698634.04 rub;
        // Standard collectors.	N: 5000; Elapsed time: 55674900 ns;
        //						Best buyer: Артамонов Михаил Маркович (225032051.36 rub);
        //						Most popular product: Прочее: блок цилиндров (380);
        //						Most popular category: ENGINE (12689);
        //						Total profit: 915698634.04 rub;
        // Custom collector.	N: 5000; Elapsed time: 19553200 ns;
        //						Best buyer: Артамонов Михаил Маркович (225032051.36 rub);
        //						Most popular product: Прочее: блок цилиндров (380);
        //						Most popular category: ENGINE (12689);
        //						Total profit: 915698634.04 rub;
        // =============================================================================================
        // For Loop.			N: 50000; Elapsed time: 133659000 ns;
        //						Best buyer: Денисова Василиса Даниловна (539349383.45 rub);
        //						Most popular product: Катализаторы автомобильные (3174);
        //						Most popular category: ENGINE (123924);
        //						Total profit: 6409167161.20 rub;
        // Standard collectors.	N: 50000; Elapsed time: 139630800 ns;
        //						Best buyer: Денисова Василиса Даниловна (539349383.45 rub);
        //						Most popular product: Катализаторы автомобильные (3174);
        //						Most popular category: ENGINE (123924);
        //						Total profit: 6409167161.20 rub;
        // Custom collector.	N: 50000; Elapsed time: 62931100 ns;
        //						Best buyer: Денисова Василиса Даниловна (539349383.45 rub);
        //						Most popular product: Катализаторы автомобильные (3174);
        //						Most popular category: ENGINE (123924);
        //						Total profit: 6409167161.20 rub;
        // =============================================================================================
        // For Loop.			N: 250000; Elapsed time: 348278300 ns;
        //						Best buyer: Климова Алиса Михайловна (1765068384.03 rub);
        //						Most popular product: Насосы вакуумные (15463);
        //						Most popular category: ENGINE (621764);
        //						Total profit: 9797805530.78 rub;
        // Standard collectors.	N: 250000; Elapsed time: 652558600 ns;
        //						Best buyer: Климова Алиса Михайловна (1765068384.03 rub);
        //						Most popular product: Насосы вакуумные (15463);
        //						Most popular category: ENGINE (621764);
        //						Total profit: 9797805530.78 rub;
        // Custom collector.	N: 250000; Elapsed time: 314775600 ns;
        //						Best buyer: Климова Алиса Михайловна (1765068384.03 rub);
        //						Most popular product: Насосы вакуумные (15463);
        //						Most popular category: ENGINE (621764);
        //						Total profit: 9797805530.78 rub;

        System.out.println();
        System.out.println("Counting only total profit");
        System.out.println();
        measureTimeTotalProfit(5000);
        measureTimeTotalProfit(50000);
        measureTimeTotalProfit(250000);
        //
        // Counting only total profit
        //
        // =============================================================================================
        // For Loop.			N: 5000; Elapsed time: 716100 ns;
        //						Total profit: 791097810.14 rub;
        // Standard collectors.	N: 5000; Elapsed time: 1927100 ns;
        //						Total profit: 791097810.14 rub;
        // Custom collector.	N: 5000; Elapsed time: 2904100 ns;
        //						Total profit: 791097810.14 rub;
        // =============================================================================================
        // For Loop.			N: 50000; Elapsed time: 7683100 ns;
        //						Total profit: 3073485296.97 rub;
        // Standard collectors.	N: 50000; Elapsed time: 1992000 ns;
        //						Total profit: 3073485296.97 rub;
        // Custom collector.	N: 50000; Elapsed time: 1447700 ns;
        //						Total profit: 3073485296.97 rub;
        // =============================================================================================
        // For Loop.			N: 250000; Elapsed time: 7274900 ns;
        //						Total profit: 16326204462.67 rub;
        // Standard collectors.	N: 250000; Elapsed time: 7224600 ns;
        //						Total profit: 16326204462.67 rub;
        // Custom collector.	N: 250000; Elapsed time: 8106000 ns;
        //						Total profit: 16326204462.67 rub;
    }

    public static void measureTimeComplexStats(int n) {
        System.out.println("=".repeat(EQUALS_STRING_LEN));
        List<Product> products = Product.ProductGenerator.generateProducts(0);
        List<Buyer> buyers = Buyer.BuyersGenerator.generateBuyers(100, 0);
        List<Purchase> purchases = Purchase.PurchaseGenerator.generatePurchases(n, 0, products, buyers);
        long start = System.nanoTime();
        StatisticsCounter.Result result = StatisticsCounter.countForLoop(purchases);
        long finish = System.nanoTime();
        printReport(finish - start, result, n, "For Loop.\t\t\t");

        start = System.nanoTime();
        result = StatisticsCounter.countStandardCollectors(purchases);
        finish = System.nanoTime();
        printReport(finish - start, result, n, "Standard collectors.\t");

        start = System.nanoTime();
        result = StatisticsCounter.countCustomCollector(purchases);
        finish = System.nanoTime();
        printReport(finish - start, result, n, "Custom collector.\t\t");
    }

    public static void printReport(long time, StatisticsCounter.Result result, int n, String methodName) {
        Pair<Buyer, Long> bestBuyer = result.getBestBuyer();
        Pair<Product, Integer> mostPopularProduct = result.getMostPopularProduct();
        Pair<Product.Category, Integer> mostPopularCategory = result.getMostPopularCategory();
        System.out.printf(
                "%sN: %d; Elapsed time: %d ns;%n" +
                        "\t\t\t\t\t\tBest buyer: %s (%d.%02d rub);%n" +
                        "\t\t\t\t\t\tMost popular product: %s (%d);%n" +
                        "\t\t\t\t\t\tMost popular category: %s (%d);%n" +
                        "\t\t\t\t\t\tTotal profit: %d.%02d rub;%n",
                methodName, n, time,
                bestBuyer.getValue0().fullName(), bestBuyer.getValue1() / 100, bestBuyer.getValue1() % 100,
                mostPopularProduct.getValue0().name(), mostPopularProduct.getValue1(),
                mostPopularCategory.getValue0(), mostPopularCategory.getValue1(),
                result.totalProfit() / 100, Math.abs(result.totalProfit() % 100)
        );
    }

    public static void measureTimeTotalProfit(int n) {
        System.out.println("=".repeat(EQUALS_STRING_LEN));
        List<Product> products = Product.ProductGenerator.generateProducts(0);
        List<Buyer> buyers = Buyer.BuyersGenerator.generateBuyers(100, 0);
        List<Purchase> purchases = Purchase.PurchaseGenerator.generatePurchases(n, 0, products, buyers);
        long start = System.nanoTime();
        long result = StatisticsCounter.countTotalProfitForLoop(purchases);
        long finish = System.nanoTime();
        System.out.printf(
                "For Loop.\t\t\t\tN: %d; Elapsed time: %d ns;%n\t\t\t\t\t\tTotal profit: %d.%02d rub;%n",
                n, finish - start, result / 100, Math.abs(result % 100)
        );

        start = System.nanoTime();
        result = StatisticsCounter.countTotalProfitStandardCollector(purchases);
        finish = System.nanoTime();
        System.out.printf(
                "Standard collectors.\tN: %d; Elapsed time: %d ns;%n\t\t\t\t\t\tTotal profit: %d.%02d rub;%n",
                n, finish - start, result / 100, Math.abs(result % 100)
        );

        start = System.nanoTime();
        result = StatisticsCounter.countTotalProfitCustomCollector(purchases);
        finish = System.nanoTime();
        System.out.printf(
                "Custom collector.\t\tN: %d; Elapsed time: %d ns;%n\t\t\t\t\t\tTotal profit: %d.%02d rub;%n",
                n, finish - start, result / 100, Math.abs(result % 100)
        );
    }
}
