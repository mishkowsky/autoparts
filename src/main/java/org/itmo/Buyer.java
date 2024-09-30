package org.itmo;

import java.util.*;

public record Buyer(String fullName, String phoneNumber, int personalDiscount) {

    public static class BuyersGenerator {
        public static List<Buyer> generateBuyers(int n) {
            String[] fullNames = Database.getFullNames();
            List<Buyer> result = new ArrayList<>();
            List<Long> phoneNumbers = new ArrayList<>();
            int[] discounts = new int[]{5, 10, 15};
            for (int i = 0; i < n; i++) phoneNumbers.add(9803456789L - n + i);
            Collections.shuffle(phoneNumbers);
            for (int i = 0; i < n; i++) {
                String fullName = fullNames[i % fullNames.length];
                String phoneNumber = "+7" + phoneNumbers.get(i);
                int personalDiscount = discounts[i % discounts.length];
                Buyer b = new Buyer(fullName, phoneNumber, personalDiscount);
                result.add(b);
            }
            return result;
        }
    }

    @Override
    public int hashCode() {
        return phoneNumber.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        return false;
    }
}
