package org.itmo;

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
        return result;
    }
}
