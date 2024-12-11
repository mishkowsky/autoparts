package org.itmo.lab3;

public class Delay {
    // 1000000000 наносек (ns) = 1000000 микросек (us) = 1000 миллисек = 1 сек
    public static void sleep(long delay) { // delay in ns
        long end = System.nanoTime() + delay;
        while (System.nanoTime() < end) { /* busy waiting */ }
    }

    public static void sleep2(long delay) {
        try {
            Thread.sleep(delay / 1000000, (int) (delay - delay / 1000000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        long start = System.nanoTime();
        Delay.sleep(5);
        long end = System.nanoTime();
        System.out.println("sta " + start);
        System.out.println("end " + end);
        System.out.println("delta " + (end - start));

        start = System.nanoTime();
        Delay.sleep2(5);
        end = System.nanoTime();
        System.out.println("sta " + start);
        System.out.println("end " + end);
        System.out.println("delta " + (end - start));
    }
}



