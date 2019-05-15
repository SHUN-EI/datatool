package com.ys.datatool.domain.config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mo on 2019/5/15
 */
public class ExecutorConfig {

    public static int threads = 20;

    public static ExecutorService executorService = Executors.newFixedThreadPool(threads);

    public static CountDownLatch countDownLatch = new CountDownLatch(threads - 1);
}
