package org.itmo;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        measureTime(5000);
        measureTime(50000);
        measureTime(250000);
        // RESULTS:
        // =====================================================================================
        // Measure #01; N: 5000; Total profit: 447461846.63 rub; Elapsed time: 47656700 ns;
        // Measure #02; N: 5000; Total profit: -628917180.95 rub; Elapsed time: 10379900 ns;
        // Measure #03; N: 5000; Total profit: -245288211.52 rub; Elapsed time: 10648300 ns;
        // Measure #04; N: 5000; Total profit: 360243775.95 rub; Elapsed time: 11775600 ns;
        // Measure #05; N: 5000; Total profit: -691873249.86 rub; Elapsed time: 9307800 ns;
        // Measure #06; N: 5000; Total profit: 954083338.89 rub; Elapsed time: 10550600 ns;
        // Measure #07; N: 5000; Total profit: -83363318.85 rub; Elapsed time: 9626500 ns;
        // Measure #08; N: 5000; Total profit: 15743846.42 rub; Elapsed time: 12859500 ns;
        // Measure #09; N: 5000; Total profit: -1514854279.29 rub; Elapsed time: 12746700 ns;
        // Measure #10; N: 5000; Total profit: -172243056.50 rub; Elapsed time: 10340100 ns;
        // Measure #11; N: 5000; Total profit: -887009704.78 rub; Elapsed time: 10305000 ns;
        // Measure #12; N: 5000; Total profit: 2301019968.83 rub; Elapsed time: 9937200 ns;
        // Measure #13; N: 5000; Total profit: 831399252.47 rub; Elapsed time: 10662200 ns;
        // Measure #14; N: 5000; Total profit: -178043226.79 rub; Elapsed time: 10362100 ns;
        // Measure #15; N: 5000; Total profit: 1348311922.10 rub; Elapsed time: 10210800 ns;
        // Average time: 13157933,33 ns;
        //
        // =====================================================================================
        // Measure #01; N: 50000; Total profit: 4044129006.53 rub; Elapsed time: 73842400 ns;
        // Measure #02; N: 50000; Total profit: -993275601.01 rub; Elapsed time: 76991000 ns;
        // Measure #03; N: 50000; Total profit: -4721667127.21 rub; Elapsed time: 108508000 ns;
        // Measure #04; N: 50000; Total profit: 3496435015.76 rub; Elapsed time: 130470900 ns;
        // Measure #05; N: 50000; Total profit: 9124828131.77 rub; Elapsed time: 98334700 ns;
        // Measure #06; N: 50000; Total profit: -3357039158.45 rub; Elapsed time: 117047200 ns;
        // Measure #07; N: 50000; Total profit: 7020862115.87 rub; Elapsed time: 72906400 ns;
        // Measure #08; N: 50000; Total profit: 848387566.89 rub; Elapsed time: 132595500 ns;
        // Measure #09; N: 50000; Total profit: 457690405.43 rub; Elapsed time: 119277100 ns;
        // Measure #10; N: 50000; Total profit: 1703425033.86 rub; Elapsed time: 73808800 ns;
        // Measure #11; N: 50000; Total profit: 2329275954.54 rub; Elapsed time: 74386500 ns;
        // Measure #12; N: 50000; Total profit: 8380690678.46 rub; Elapsed time: 81186000 ns;
        // Measure #13; N: 50000; Total profit: 4174142817.19 rub; Elapsed time: 83396100 ns;
        // Measure #14; N: 50000; Total profit: -42202362.94 rub; Elapsed time: 81330500 ns;
        // Measure #15; N: 50000; Total profit: 5878886013.26 rub; Elapsed time: 81847900 ns;
        // Average time: 93728600,00 ns;
        //
        // =====================================================================================
        // Measure #01; N: 250000; Total profit: 12788972824.40 rub; Elapsed time: 514821700 ns;
        // Measure #02; N: 250000; Total profit: 23691244842.66 rub; Elapsed time: 516938900 ns;
        // Measure #03; N: 250000; Total profit: 8077856619.82 rub; Elapsed time: 491607400 ns;
        // Measure #04; N: 250000; Total profit: 19662938954.61 rub; Elapsed time: 503293200 ns;
        // Measure #05; N: 250000; Total profit: 23618213589.77 rub; Elapsed time: 367914600 ns;
        // Measure #06; N: 250000; Total profit: 21280867434.48 rub; Elapsed time: 374528600 ns;
        // Measure #07; N: 250000; Total profit: 21163627675.52 rub; Elapsed time: 524417400 ns;
        // Measure #08; N: 250000; Total profit: 16662261673.16 rub; Elapsed time: 461020500 ns;
        // Measure #09; N: 250000; Total profit: 11619614677.87 rub; Elapsed time: 401201700 ns;
        // Measure #10; N: 250000; Total profit: 11661735850.55 rub; Elapsed time: 487315500 ns;
        // Measure #11; N: 250000; Total profit: 21230392048.87 rub; Elapsed time: 556515200 ns;
        // Measure #12; N: 250000; Total profit: 30912486940.69 rub; Elapsed time: 373890000 ns;
        // Measure #13; N: 250000; Total profit: 29453371728.96 rub; Elapsed time: 531513400 ns;
        // Measure #14; N: 250000; Total profit: 14690501823.64 rub; Elapsed time: 410955200 ns;
        // Measure #15; N: 250000; Total profit: 14004073605.71 rub; Elapsed time: 437342900 ns;
        // Average time: 463551746,67 ns;
    }

    public final static int SAMPLES_COUNT = 15;

    public static void measureTime(int n) {
        System.out.println("=====================");
        long acc = 0;
        for (int i = 0; i < SAMPLES_COUNT; i++) {
            List<Product> products = Product.ProductGenerator.generateProducts();
            List<Buyer> buyers = Buyer.BuyersGenerator.generateBuyers(100);
            List<Purchase> purchases = Purchase.PurchaseGenerator.generatePurchases(n, products, buyers);
            long start = System.nanoTime();
            StatisticsCounter sc = new StatisticsCounter(purchases);
            long finish = System.nanoTime();
            System.out.printf(
                    "Measure #%02d; N: %d; Total profit: %d.%02d rub; Elapsed time: %d ns;%n",
                    i + 1, n, sc.getTotalProfit() / 100, Math.abs(sc.getTotalProfit() % 100), finish - start);
            acc += finish - start;
        }
        System.out.printf("Average time: %.2f ns;%n%n", (acc * 1.0) / SAMPLES_COUNT);
    }
}
