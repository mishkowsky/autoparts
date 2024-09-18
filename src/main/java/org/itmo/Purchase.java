package org.itmo;

import java.util.*;


public class Purchase {

    private final Calendar purchaseTime;
    private final PaymentMethod paymentMethod;

    private final int totalPrice;
    private final String shopAddress;

    private final List<Product> products = new ArrayList<>();

    public Purchase(Calendar purchaseTime, PaymentMethod paymentMethod, int totalPrice,
                    String shopAddress, Collection<Product> products) {
        this.purchaseTime = purchaseTime;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.shopAddress = shopAddress;
        this.products.addAll(products);
    }

    public Calendar getPurchaseTime() {
        return purchaseTime;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static class PurchaseGenerator {

        private final String[] shopAddresses = {"1", "2"};
        private final static Random random = new Random();

        public static List<Purchase> generatePurchases(int n, Set<Product> products) {
            int i = 0;
            List<Purchase> result = new ArrayList<>();
            while (i < n) {
                int uniqueProductsCount = random.nextInt(1, products.size());

                List<Product> productsToAdd = new ArrayList<>();



                Purchase purchase = new Purchase();
                result.add(purchase);
                i++;
            }
            return result;
        }
    }
}
