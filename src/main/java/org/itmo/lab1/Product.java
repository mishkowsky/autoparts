package org.itmo.lab1;

import java.util.*;

public record Product(int sellingPrice, int purchasePrice, String name, Category category, int discount) {

    public enum Category {
        ENGINE,
        CAR_BODY,
        SUSPENSION,
        STEERING,
        EXHAUST_SYSTEM,
        HEATING_AND_VENTILATION,
        ENGINE_COOLING,
        CLEANING_GLASS_AND_HEADLIGHTS,
        POWER_SUPPLY,
        BRAKE_SYSTEM,
        TRANSMISSION,
        FILTERS,
        IGNITION_SYSTEM,
        LIGHTING,
        GLASS
    }

    public static class ProductGenerator {

        // цены в копейках
        public static int MINIMAL_SELLING_PRICE = 1000; // 10 rub
        public static int MAXIMUM_SELLING_PRICE = 10000000; // 100000 rub

        private final static Random random = new Random();

        public static List<Product> generateProducts() {
            List<Product> result = new ArrayList<>();
            Map<Category, String[]> names = Database.getAutopartsNamings();
            for (var entry : names.entrySet()) {
                String[] namesInCategory = entry.getValue();
                Category category = entry.getKey();
                for (String name : namesInCategory) {
                    int sellingPrice = random.nextInt(MINIMAL_SELLING_PRICE, MAXIMUM_SELLING_PRICE);
                    int markup = random.nextInt(10, 35);
                    int purchasePrice = sellingPrice * (100 - markup); // цена закупки
                    int discount = random.nextInt(5, markup); // discount <= markup, so no loss
                    Product p = new Product(sellingPrice, purchasePrice, name, category, discount);
                    result.add(p);
                }
            }
            return result;
        }
    }
}