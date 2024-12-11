package org.itmo.lab31;

import io.reactivex.rxjava3.core.Observable;

import static java.util.concurrent.TimeUnit.SECONDS;

public class sandbox {


    public static void main(String[] args) {
        Observable
                .fromArray(new String[]{"Hello,", " World!"})
                .delay(1, SECONDS)
                .blockingSubscribe(System.out::println);
    }
}
