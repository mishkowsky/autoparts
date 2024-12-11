package org.itmo.lab3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Buyer(String fullName, String phoneNumber, int personalDiscount, long delay) {

    public static class BuyersGenerator {
        public static List<Buyer> generateBuyers(int n, long delay) {
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
                Buyer b = new Buyer(fullName, phoneNumber, personalDiscount, delay);
                result.add(b);
            }
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buyer buyer)) return false;
        return delay == buyer.delay && personalDiscount == buyer.personalDiscount
                && fullName.equals(buyer.fullName) && phoneNumber.equals(buyer.phoneNumber);
    }

    @Override
    public int hashCode() {
        int result = fullName.hashCode();
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + personalDiscount;
        result = 31 * result + Long.hashCode(delay);
        return result;
    }

    @Override
    public String fullName() {
        Delay.sleep(delay);
        return fullName;
    }

    @Override
    public String phoneNumber() {
        Delay.sleep(delay);
        return phoneNumber;
    }

    @Override
    public int personalDiscount() {
        Delay.sleep(delay);
        return personalDiscount;
    }

    @Override
    public long delay() {
        Delay.sleep(delay);
        return delay;
    }
}
