package org.itmo.lab1;

import java.util.HashMap;
import java.util.Map;

public class Database {

    public static String[] getFullNames() {
        return Utils.loadLinesFromFile("fullnames.txt");
    }

    public static String[] getShopAddresses() {
        return Utils.loadLinesFromFile("addresses.txt");
    }

    public static Map<Product.Category, String[]> getAutopartsNamings() {
        Map<Product.Category, String[]> result = new HashMap<>();
        for (Product.Category category : Product.Category.values()) {
            result.put(category, Utils.loadLinesFromFile("products_by_categories/" +
                    category.name().toLowerCase() + ".txt"));
        }
//        result.put(Product.Category.ENGINE, Utils.load("products_by_categories/engine.txt"));
//        result.put(Product.Category.CAR_BODY, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.SUSPENSION, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.STEERING, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.EXHAUST_SYSTEM, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.HEATING_AND_VENTILATION, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.ENGINE_COOLING, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.CLEANING_GLASS_AND_HEADLIGHTS, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.POWER_SUPPLY, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.BRAKE_SYSTEM, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.TRANSMISSION, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.FILTERS, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.IGNITION_SYSTEM, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.LIGHTING, Utils.load("products_by_categories/car_body.txt"));
//        result.put(Product.Category.GLASS, Utils.load("products_by_categories/car_body.txt"));
        return result;
    }
}