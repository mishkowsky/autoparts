package org.itmo;
import java.util.HashSet;
import java.util.Set;

public record Product(int price, String name, Mark mark, int discount) {
    public static class ProductGenerator {
        public static Set<Product> generateProducts(int n) {
            return new HashSet<>();
        }
    }
}
