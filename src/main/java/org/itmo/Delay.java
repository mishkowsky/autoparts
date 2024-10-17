package org.itmo;

public class Delay {
    // 1000000000 наносек (ns) = 1000000 микросек (us) = 1000 миллисек = 1 сек
    public static void sleep(long delay) { // delay in ns
        long end = System.nanoTime() + delay;
        while (System.nanoTime() < end) { /* busy waiting */ }
    }
}
