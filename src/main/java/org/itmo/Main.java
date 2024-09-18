package org.itmo;

import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Set<Product> products = Product.ProductGenerator.generateProducts(10);
        List<Purchase> purchases = Purchase.PurchaseGenerator.generatePurchases(10, products);
    }
}
