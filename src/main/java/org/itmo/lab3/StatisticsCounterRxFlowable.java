package org.itmo.lab3;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatisticsCounterRxFlowable {

    static class MySubscriber implements FlowableSubscriber<Purchase> {
        private Subscription subscription;
        private final long elementsToRequest = 10;
        private float averageDelay = 0;
        private int processedElementsCount = 0;
        private final StatisticsCounterRx.Result result;
        private long prevTime;

        public MySubscriber(StatisticsCounterRx.Result result) {
            prevTime = System.nanoTime();
            this.result = result;
        }

        public void onSubscribe(Subscription s) {
            subscription = s;
            subscription.request(elementsToRequest);
        }

        public void onNext(Purchase purchase) {
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
            subscription.request(elementsToRequest);
            this.processedElementsCount++;
            this.registerDelay();
        }

        public void registerDelay() {
            long nextTime = System.nanoTime();
            long delta = nextTime - this.prevTime;
            this.averageDelay = (this.averageDelay * (this.processedElementsCount - 1) + delta) / this.processedElementsCount;
            this.prevTime = System.nanoTime();
        }

        public void onError(Throwable e) {
        }

        public void onComplete() {
//            System.out.println("\n" +
//                    "===============Done===============\n" +
//                    "Processed " + this.processedElementsCount + " element\n" +
//                    "Average delay: " + this.averageDelay);
        }
    }

    public static StatisticsCounterRx.Result count(List<Purchase> purchases) {

        StatisticsCounterRx.Result result = new StatisticsCounterRx.Result();

        MySubscriber s = new MySubscriber(result);

        Observable.fromIterable(purchases)
                .toFlowable(BackpressureStrategy.BUFFER)
//                .zipWith(
//                        Flowable.interval(1, TimeUnit.NANOSECONDS),
//                        (item, _) -> item
//                )
                .subscribeOn(Schedulers.computation())
                .blockingSubscribe(s);

        // assert result == StatisticsCounter.countForLoop(purchases);
//        System.out.println("Best buyer from RX: " + result.getBestBuyer());
//        System.out.println("Best buyer from ForLoop" + StatisticsCounter.countForLoop(purchases).getBestBuyer());
        return result;
    }

    static void run(int n) {
        int delay = 0;
        List<Purchase> purchases = Purchase.PurchaseGenerator.generatePurchases(n, delay,
                Product.ProductGenerator.generateProducts(delay),
                Buyer.BuyersGenerator.generateBuyers(100, delay));
        count(purchases);
    }

    public static void main(String[] args) {
        run(50000);
        run(100000);
        run(1000000);
        run(2000000);
        run(3000000);
        run(4000000);
        run(5000000);
    }
}

// ===============Done===============
// Processed 50000 element
// Average delay: 10572.957
// Best buyer from RX: [Buyer[fullName=Лебедева Анна Максимовна, phoneNumber=+79803456713, personalDiscount=15, delay=0], 10804595703]
// Best buyer from ForLoop[Buyer[fullName=Лебедева Анна Максимовна, phoneNumber=+79803456713, personalDiscount=15, delay=0], 10804595703]
//
// ===============Done===============
// Processed 100000 element
// Average delay: 4134.615
// Best buyer from RX: [Buyer[fullName=Александров Демид Иванович, phoneNumber=+79803456709, personalDiscount=10, delay=0], 21572397990]
// Best buyer from ForLoop[Buyer[fullName=Александров Демид Иванович, phoneNumber=+79803456709, personalDiscount=10, delay=0], 21572397990]
//
// ===============Done===============
// Processed 1000000 element
// Average delay: 4505.4014
// Best buyer from RX: [Buyer[fullName=Богданов Савелий Владимирович, phoneNumber=+79803456762, personalDiscount=15, delay=0], 229724429852]
// Best buyer from ForLoop[Buyer[fullName=Богданов Савелий Владимирович, phoneNumber=+79803456762, personalDiscount=15, delay=0], 229724429852]
//
// ===============Done===============
// Processed 2000000 element
// Average delay: 4480.283
// Best buyer from RX: [Buyer[fullName=Васильева Аделина Артемьевна, phoneNumber=+79803456730, personalDiscount=15, delay=0], 474339108011]
// Best buyer from ForLoop[Buyer[fullName=Васильева Аделина Артемьевна, phoneNumber=+79803456730, personalDiscount=15, delay=0], 474339108011]
//
// ===============Done===============
// Processed 3000000 element
// Average delay: 4719.8516
// Best buyer from RX: [Buyer[fullName=Москвина Лидия Артёмовна, phoneNumber=+79803456710, personalDiscount=15, delay=0], 641107119080]
// Best buyer from ForLoop[Buyer[fullName=Москвина Лидия Артёмовна, phoneNumber=+79803456710, personalDiscount=15, delay=0], 641107119080]
//
// ===============Done===============
// Processed 4000000 element
// Average delay: 5377.501
// Best buyer from RX: [Buyer[fullName=Макарова Дарья Ивановна, phoneNumber=+79803456753, personalDiscount=15, delay=0], 866768900241]
// Best buyer from ForLoop[Buyer[fullName=Макарова Дарья Ивановна, phoneNumber=+79803456753, personalDiscount=15, delay=0], 866768900241]
//
// ===============Done===============
// Processed 5000000 element
// Average delay: 6435.2905
// Best buyer from RX: [Buyer[fullName=Богданов Савелий Владимирович, phoneNumber=+79803456695, personalDiscount=15, delay=0], 1094256880594]
// Best buyer from ForLoop[Buyer[fullName=Богданов Савелий Владимирович, phoneNumber=+79803456695, personalDiscount=15, delay=0], 1094256880594]

// #######################    2    ####################
// ===============Done===============
//Processed 50000 element
//Average delay: 12708.162
//Best buyer from RX: [Buyer[fullName=Журавлев Тимофей Владимирович, phoneNumber=+79803456703, personalDiscount=10, delay=0], 11658651425]
//Best buyer from ForLoop[Buyer[fullName=Журавлев Тимофей Владимирович, phoneNumber=+79803456703, personalDiscount=10, delay=0], 11658651425]
//
//===============Done===============
//Processed 100000 element
//Average delay: 4504.9653
//Best buyer from RX: [Buyer[fullName=Соколова Александра Савельевна, phoneNumber=+79803456716, personalDiscount=15, delay=0], 27540244429]
//Best buyer from ForLoop[Buyer[fullName=Соколова Александра Савельевна, phoneNumber=+79803456716, personalDiscount=15, delay=0], 27540244429]
//
//===============Done===============
//Processed 1000000 element
//Average delay: 4396.9683
//Best buyer from RX: [Buyer[fullName=Климова Алиса Михайловна, phoneNumber=+79803456772, personalDiscount=15, delay=0], 237938973818]
//Best buyer from ForLoop[Buyer[fullName=Климова Алиса Михайловна, phoneNumber=+79803456772, personalDiscount=15, delay=0], 237938973818]
//
//===============Done===============
//Processed 2000000 element
//Average delay: 4662.663
//Best buyer from RX: [Buyer[fullName=Попова Дарья Львовна, phoneNumber=+79803456743, personalDiscount=10, delay=0], 401686386099]
//Best buyer from ForLoop[Buyer[fullName=Попова Дарья Львовна, phoneNumber=+79803456743, personalDiscount=10, delay=0], 401686386099]
//
//===============Done===============
//Processed 3000000 element
//Average delay: 4130.431
//Best buyer from RX: [Buyer[fullName=Филатова Нина Максимовна, phoneNumber=+79803456748, personalDiscount=15, delay=0], 607720770755]
//Best buyer from ForLoop[Buyer[fullName=Филатова Нина Максимовна, phoneNumber=+79803456748, personalDiscount=15, delay=0], 607720770755]
//
//===============Done===============
//Processed 4000000 element
//Average delay: 4783.56
//Best buyer from RX: [Buyer[fullName=Лебедева Анна Максимовна, phoneNumber=+79803456787, personalDiscount=15, delay=0], 927886993840]
//Best buyer from ForLoop[Buyer[fullName=Лебедева Анна Максимовна, phoneNumber=+79803456787, personalDiscount=15, delay=0], 927886993840]
//
//===============Done===============
//Processed 5000000 element
//Average delay: 6006.516
//Best buyer from RX: [Buyer[fullName=Филатова Нина Максимовна, phoneNumber=+79803456704, personalDiscount=15, delay=0], 1134527582724]
//Best buyer from ForLoop[Buyer[fullName=Филатова Нина Максимовна, phoneNumber=+79803456704, personalDiscount=15, delay=0], 1134527582724]
