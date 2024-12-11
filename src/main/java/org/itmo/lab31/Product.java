package org.itmo.lab31;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public record Product(int sellingPrice, int purchasePrice, String name, Category category, int discount, long delay) {

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

//    @Override
//    public int sellingPrice() {
//        Delay.sleep(delay);
//        return sellingPrice;
//    }
//
//    @Override
//    public int purchasePrice() {
//        Delay.sleep(delay);
//        return purchasePrice;
//    }
//
//    @Override
//    public String name() {
//        Delay.sleep(delay);
//        return name;
//    }

    @Override
    public Category category() {
        Delay.sleep(delay);
        return category;
    }

//    @Override
//    public int discount() {
//        Delay.sleep(delay);
//        return discount;
//    }
//
//    @Override
//    public long delay() {
//        Delay.sleep(delay);
//        return delay;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return delay == product.delay && discount == product.discount && sellingPrice == product.sellingPrice
                && purchasePrice == product.purchasePrice && name.equals(product.name) && category == product.category;
    }

    @Override
    public int hashCode() {
        int result = sellingPrice;
        result = 31 * result + purchasePrice;
        result = 31 * result + name.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + discount;
        result = 31 * result + Long.hashCode(delay);
        return result;
    }

    public static class ProductGenerator {

        // цены в копейках
        public static int MINIMAL_SELLING_PRICE = 1000; // 10 rub
        public static int MAXIMUM_SELLING_PRICE = 10000000; // 100000 rub

        private final static Random random = new Random();

        public static List<Product> generateProducts(long delay) {
            List<Product> result = new ArrayList<>();
            Map<Category, String[]> names = Database.getAutopartsNamings();
            for (var entry : names.entrySet()) {
                String[] namesInCategory = entry.getValue();
                Category category = entry.getKey();
                for (String name : namesInCategory) {
                    int sellingPrice = random.nextInt(MINIMAL_SELLING_PRICE, MAXIMUM_SELLING_PRICE);
                    int markup = random.nextInt(10, 35);
                    int purchasePrice = sellingPrice * (100 - markup) / 100; // цена закупки
                    int discount = random.nextInt(5, markup); // discount <= markup, so no loss
                    Product p = new Product(sellingPrice, purchasePrice, name, category, discount, delay);
                    result.add(p);
                }
            }
            return result;
        }
    }
}
