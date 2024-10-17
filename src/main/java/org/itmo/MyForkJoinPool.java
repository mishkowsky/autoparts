package org.itmo;
import java.util.concurrent.ForkJoinPool;

public class MyForkJoinPool {

    private static final int THREADS_ = Runtime.getRuntime().availableProcessors();//Кол-во доступных проц. в пк
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS_);

    public static ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }
    
}