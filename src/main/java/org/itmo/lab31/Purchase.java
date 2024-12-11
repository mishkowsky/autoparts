package org.itmo.lab31;

import java.text.SimpleDateFormat;
import java.util.*;

public class Purchase {

    public enum PaymentMethod { CASH, CARD, }

    private final Date purchaseDate;
    private final PaymentMethod paymentMethod;

    private final long totalPrice;
    private final String shopAddress;
    private final Buyer buyer;
    private final Map<Product, Integer> products = new HashMap<>();

    private final long delay;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public Purchase(Date purchaseDate, PaymentMethod paymentMethod, int totalPrice,
                    String shopAddress, Map<Product, Integer> products, Buyer buyer, long delay) {

        this.purchaseDate = purchaseDate;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.shopAddress = shopAddress;
        this.buyer = buyer;
        this.delay = delay;
        this.products.putAll(products);
    }

//    public Date getPurchaseTime() {
//        Delay.sleep(delay);
//        return purchaseDate;
//    }

    public PaymentMethod getPaymentMethod() {
        Delay.sleep(delay);
        return paymentMethod;
    }

    public long getTotalPrice() {
//        Delay.sleep(delay);
        return totalPrice;
    }

    public String getShopAddress() {
//        Delay.sleep(delay);
        return shopAddress;
    }

    public Map<Product, Integer> getProducts() {
        Delay.sleep(delay);
        return products;
    }

    public Buyer getBuyer() {
        Delay.sleep(delay);
        return buyer;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Purchase purchase)) return false;
        return getTotalPrice() == purchase.getTotalPrice() && delay == purchase.delay
                && purchaseDate.equals(purchase.purchaseDate) && getPaymentMethod() == purchase.getPaymentMethod()
                && getShopAddress().equals(purchase.getShopAddress()) && getBuyer().equals(purchase.getBuyer())
                && getProducts().equals(purchase.getProducts());
    }

    @Override
    public int hashCode() {
        int result = purchaseDate.hashCode();
        result = 31 * result + getPaymentMethod().hashCode();
        result = 31 * result + Long.hashCode(getTotalPrice());
        result = 31 * result + getShopAddress().hashCode();
        result = 31 * result + getBuyer().hashCode();
        result = 31 * result + getProducts().hashCode();
        result = 31 * result + Long.hashCode(delay);
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "%s,%s,%d,%s,%s,%s\n",
                products, buyer, totalPrice, paymentMethod, dateFormat.format(purchaseDate), shopAddress);
    }

    public static class PurchaseGenerator {


        private final static int MAX_UNIQUE_ITEMS_IN_PURCHASE = 25;
        private final static int MAX_COUNT_PER_PRODUCT = 25;

        private final static Random random = new Random();

        public static List<Purchase> generatePurchases(
                int n, long delay, List<Product> products, List<Buyer> buyers,
                Calendar startPeriodDate,
                Calendar endPeriodDate) {

            String[] shopAddresses = Database.getShopAddresses();

            List<Purchase> result = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int totalPurchasePrice = 0;
                Map<Product, Integer> productsMap = new HashMap<>();
                Buyer buyer = buyers.get(i % buyers.size());
                PaymentMethod pm;
                if (random.nextBoolean()) pm = PaymentMethod.CARD;
                else pm = PaymentMethod.CASH;

                int uniqueProductsCount =
                        random.nextInt(1, Math.min(MAX_UNIQUE_ITEMS_IN_PURCHASE, products.size()));
                Collections.shuffle(products);
                for (int j = 0; j < uniqueProductsCount; j++) {
                    Product p = products.get(j);
                    int count = random.nextInt(1, MAX_COUNT_PER_PRODUCT);
                    int productsPrice = (p.sellingPrice() * (100 - p.discount() - buyer.personalDiscount()) * count) / 100;
                    totalPurchasePrice += productsPrice;
                    productsMap.put(p, count);
                }

                Date purchaseDate = Utils.between(startPeriodDate.getTime(), endPeriodDate.getTime());

                Purchase purchase = new Purchase(
                        purchaseDate, pm, totalPurchasePrice,
                        shopAddresses[i % shopAddresses.length], productsMap, buyer, delay);

                result.add(purchase);
            }
            return result;
        }

        public static List<Purchase> generatePurchases(
                int n, long delay, List<Product> products, List<Buyer> buyers) {
            Calendar startPeriodDate = Calendar.getInstance();
            Calendar endPeriodDate = Calendar.getInstance();
            endPeriodDate.add(Calendar.MONTH, 1);
            return generatePurchases(n, delay, products, buyers, startPeriodDate, endPeriodDate);
        }
    }
}
